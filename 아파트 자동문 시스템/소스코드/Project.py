import RPi.GPIO as GPIO
import time
from class_FND import*
from class_Light import*
from class_TLCD import*
from class_JOG import*
from class_Motor import*
from threading import Thread

GPIO.setwarnings(False)
p_jog = my_JOG()
p_motor = my_Motor()
p_tlcd = my_TLCD()
p_fnd = my_FND()
p_light = my_Light()

init_disp     = 0
phase         = [0, 0, 0]
PASS_NUM      = [0, 0, 0, 0]
PASS_NUM_temp = [0, 0, 0, 0]
PASSWORD      = '000000'
fnd_delay     = 0.3
shift         = 0
shift_array   = [0, 0, 0, 0, 0, 0]
wrong         = 0
#####################################################
def initial_state():
    global phase, PASSWORD, fnd_delay, shift, wrong 
    PASSWORD  = '      '
    fnd_delay = 0.0001
    time.sleep(0.01)
    wrong = 0
    shift = 0
    phase = [0,0,0]
    PASSWORD  = '000000'
    fnd_delay = 0.5    
    p_tlcd.lcd_byte(0x01,cmd)
    p_tlcd.lcd_string('Select Menu',line1)
    phase_0()
    
def phase_0():
    global phase, init_disp, PASSWORD, fnd_delay, shift, shift_array, wrong 
    init_disp = 0    
    if phase[0] == 0:
        p_tlcd.lcd_string('1.Input Password',line2)
    elif phase[0] == 1:
        shift = 0
        shift_array = [0,0,1,0,0,0]
        PASSWORD = [0,0,0,0]
        fnd_delay = 0.0015
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('Password ?',line1)
        p_tlcd.lcd_string('Back: LEFT',line2)       
        check()
    elif phase[0] == 2:
        wrong = 0
        PASSWORD = ['Open']*4
        fnd_delay = 0.01
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('Open',line1)
        p_tlcd.lcd_string('the Door',line2)        
        open_door()

def phase_1():
    global phase, init_disp, PASSWORD, fnd_delay, wrong, shift, shift_array
    init_disp = 1
    if phase[1] == 0:
        p_tlcd.lcd_string('2.Call          ',line2)
    elif phase[1] == 1:
        shift = 0
        shift_array = [0,0,0,1,0,0]
        PASSWORD = [0, 0, 0]
        fnd_delay = 0.0015
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('Room Number ?',line1)
        p_tlcd.lcd_string('Back: LEFT',line2)
        check()
    elif phase[1] == 2:
        PASSWORD = ['Call']*4
        fnd_delay = 0.01
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('Calling Now',line1)
        p_tlcd.lcd_string('Cancel: LEFT',line2)
        answer = False
        for i in range(5):
            if p_light.readLight() < 100:
                i = 5
                answer = True
            time.sleep(1)
        if answer == True:
            phase[1] = 0
            phase[0] = 2
            phase_0()
        else:
            phase[1] = 1
            phase_1()                
    
def phase_2():
    global phase, init_disp, PASSWORD, fnd_delay, wrong, shift, shift_array
    init_disp = 2
    if phase[2] == 0:
        p_tlcd.lcd_string('3.Option        ',line2)
        
    elif phase[2] == 1:
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('Chage Password ?',line1)
        p_tlcd.lcd_string('Y:ENTER / N:LEFT',line2)
        check()
        
    elif phase[2] == 2:
        shift = 0
        shift_array = [0,0,1,0,0,0]
        PASSWORD = [0,0,0,0]
        fnd_delay = 0.0015
        p_tlcd.lcd_byte(0x01,cmd)        
        p_tlcd.lcd_string('Password ?',line1)
        p_tlcd.lcd_string('Back: LEFT',line2)
        check()
        
    elif phase[2] == 3:
        shift = 0
        shift_array = [0,0,1,0,0,0]
        wrong = 0
        PASSWORD = [0,0,0,0]
        fnd_delay = 0.0015
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('Input New',line1)
        p_tlcd.lcd_string('Password',line2)
        check()
        
    elif phase[2] == 4:
        shift = 0
        shift_array = [0,0,1,0,0,0]
        PASSWORD = [0,0,0,0]
        fnd_delay = 0.0015
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('One More Time', line1)
        p_tlcd.lcd_string('Please', line2)    
        check()
        
    elif phase[2] == 5:
        PASSWORD = ['Change']*6
        fnd_delay = 0.0015
        p_tlcd.lcd_byte(0x01,cmd)
        p_tlcd.lcd_string('Password', line1)
        p_tlcd.lcd_string('Changed !!', line2)
        time.sleep(1.5)
        initial_state()
        
##################################################### JOG controll
def down(Channel):    
    global init_disp, PASSWORD, shift
    if sum(phase) == 0:
        if init_disp == 0:
            phase_1()
        elif init_disp == 1:
            phase_2()
        else:pass        
    if phase[0] == 1 or phase[1] == 1 or phase[2] == 2 or phase[2] == 3 or phase[2] == 4:
        PASSWORD = counter(PASSWORD, shift, down)
    
def up(Channel):
    global init_disp, PASSWORD, shift
    if sum(phase) == 0:
        if init_disp == 1:
            phase_0()
        elif init_disp == 2:
            phase_1()
        else:pass        
    if phase[0] == 1 or phase[1] == 1 or phase[2] == 2 or phase[2] == 3 or phase[2] == 4:
        PASSWORD = counter(PASSWORD, shift, up)    
    
def enter(channel):
    global phase, init_disp, shift, shift_array, wrong, PASSWORD, fnd_delay, PASS_NUM, PASS_NUM_temp
    if sum(phase) == 0:       
        if init_disp == 0:
            phase[0] = 1
            phase_0()
        elif init_disp == 1:
            phase[1] = 1
            phase_1()
        elif init_disp == 2:
            phase[2] = 1
            phase_2()
    
    if phase[0] == 1 or phase[2] == 2:
        if PASSWORD == PASS_NUM:                        
            if phase[0] == 1:
                phase[0] = 2
                phase_0()
            elif phase[2] == 2:
                phase[2] = 3
                phase_2()                
        else:
            PASSWORD = ['-.','-',' ']
            fnd_delay = 0.002
            p_tlcd.lcd_string('Wrong Number!!',line1)
            p_tlcd.lcd_string(('%d Times Left' %(2-wrong)),line2)
            wrong += 1
            #shift = 0
            time.sleep(1.5)            
            if wrong == 3:
                initial_state()
            else :
                if phase[0] == 1:
                    phase_0()
                else:
                    phase_2()

    elif phase[1] == 1:
        shift = 0
        phase[1] = 2
        phase_1()
        
    elif phase[2] == 1:
        shift = 0
        phase[2] = 2
        phase_2()
        
    elif phase[2] == 3:
        if PASSWORD == PASS_NUM:
            PASSWORD = ['-.','-',' ']
            fnd_delay = 0.002
            p_tlcd.lcd_byte(0x01,cmd)
            p_tlcd.lcd_string('Same Password', line1)
            p_tlcd.lcd_string('Try Again', line2)
            time.sleep(1.5)
            phase_2()
        else:
            PASS_NUM_temp = PASSWORD
            phase[2] = 4       
            phase_2()
    
    elif phase[2] == 4:
        if PASSWORD == PASS_NUM_temp:
            PASS_NUM = PASS_NUM_temp
            phase[2] = 5
            phase_2()
        else:
            PASSWORD = ['-.','-',' ']
            fnd_delay = 0.002
            p_tlcd.lcd_string('Wrong Number!!',line1)
            p_tlcd.lcd_string(('%d times left' %(2-wrong)),line2)
            wrong += 1
            shift = 0
            time.sleep(1.5)            
            if wrong == 3:
                initial_state()
            else :
                if phase[0] == 1:
                    phase_0()
                else:
                    phase_2()            
    shift = 0
    time.sleep(0.001)
    
def back(Channel):
    global PASSWORD, fnd_delay
    if sum(phase) == 1: 
        initial_state()
    if phase[1] == 2:
        phase[1] = 1
        phase_1()
    if phase[2] == 2:
        initial_state()

#####################################################  Door controll
emer = 0
def close_door():
    global count, emer    
    while count != 7:
        emer = p_light.readLight()
        if emer > 250 :
            p_motor.close()
            time.sleep(0.5)
            count += 1
        else:
            p_motor.reopen(count >> 1)
            count = 0
    p_motor.stop()
    print('check')
    initial_state()

def open_door():
    global count, emer
    emer = 0
    p_motor.open()
    time.sleep(2)
    p_motor.stop()
    time.sleep(1)
    count = 0
    t_door = Thread(target=close_door)
    t_door.start()
    
##################################################### etc
def shift_(Channel):
    global shift, shift_array
    shift_array = [0, 0, 0, 0, 0, 0]
    shift += 1
    if phase[1] == 1:
        if shift == 3:
            shift = 0
        shift_array[shift+3] = 1    
    else:
        if shift == 4:
            shift = 0
        shift_array[shift+2] = 1
            
def shift_reset(Channel):
    global shift
    shift = 0

def counter(password, shift, up_down):
    if up_down == up:
        if password[shift] == 9:
            password[shift] = 0
        else:
            password[shift] += 1
    else:
        if password[shift] == 0:
            password[shift] = 9
        else:
            password[shift] -= 1
    return password
        
    
##################################################### main
GPIO.add_event_detect(DOWN, GPIO.RISING, callback = down)
GPIO.add_event_detect(UP, GPIO.RISING, callback = up)
GPIO.add_event_detect(CENTER, GPIO.RISING, callback = enter)
GPIO.add_event_detect(LEFT, GPIO.RISING, callback = back)
GPIO.add_event_detect(RIGHT, GPIO.RISING, callback = shift_)

try:
    initial_state()    
    while True:
        #p_fnd.p_fnd_disp(PASSWORD,fnd_delay) 
            
        number = [' ']*6
        for i in range(0,len(PASSWORD),1):
            
            number[i+(len(number)-len(PASSWORD))] = PASSWORD[i]    
        for j in range(0,6,1):
                if number[j] == ' ':
                    out_disp=none<<8 | digit[j]
                    p_fnd.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == '-':
                    out_disp=dash<<8 | digit[j]
                    p_fnd.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == '-.':
                    out_disp=(dash+1)<<8 | digit[j]
                    p_fnd.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == 'Open':
                    out_disp=data_2[j-2]<<8 | digit[j]
                    p_fnd.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == 'Call':
                    out_disp=data_3[j-2]<<8 | digit[j]
                    p_fnd.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == 'Change':
                    out_disp=data_4[j]<<8 | digit[j]
                    p_fnd.bus.write_word_data(addr,out_port,out_disp)
                else:
                    out_disp=(data[int(number[j])]+shift_array[j])<<8 | digit[j]
                    p_fnd.bus.write_word_data(addr,out_port,out_disp)                
                time.sleep(fnd_delay)
                p_fnd.fnd_break()
        
    
finally:
    pass
