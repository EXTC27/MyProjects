import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

d=[17,18,27,22]
rs=23
en=26
width=16
chr=True
cmd=False
line1=0x80
line2=0xC0
e_pulse=0.0005
e_delay=0.0005

class my_TLCD:
    def __init__(self):
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(en,GPIO.OUT)
        GPIO.setup(rs,GPIO.OUT)
        GPIO.setup(d,GPIO.OUT)    
    
        self.lcd_init()   
    
    def lcd_init(self):
        self.lcd_byte(0x33,cmd);    self.lcd_byte(0x32,cmd)
        self.lcd_byte(0x06,cmd);    self.lcd_byte(0x0C,cmd)
        self.lcd_byte(0x28,cmd);    self.lcd_byte(0x01,cmd)
        time.sleep(e_delay)
    
    def lcd_byte(self,bits,mode):
        GPIO.output(rs,mode)
        GPIO.output(d[0],False);    GPIO.output(d[1],False)
        GPIO.output(d[2],False);    GPIO.output(d[3],False)
        
        if bits & 0x10 == 0x10:
            GPIO.output(d[0],True)
        if bits & 0x20 == 0x20:
            GPIO.output(d[1],True)
        if bits & 0x40 == 0x40:
            GPIO.output(d[2],True)
        if bits & 0x80 == 0x80:
            GPIO.output(d[3],True)
        self.lcd_toggle_enable()
        
        GPIO.output(d[0],False);    GPIO.output(d[1],False)
        GPIO.output(d[2],False);    GPIO.output(d[3],False)
        
        if bits & 0x01 == 0x01:
            GPIO.output(d[0],True)
        if bits & 0x02 == 0x02:
            GPIO.output(d[1],True)
        if bits & 0x04 == 0x04:
            GPIO.output(d[2],True)
        if bits & 0x08 == 0x08:
            GPIO.output(d[3],True)
        self.lcd_toggle_enable()
        
    def lcd_toggle_enable(self):
        time.sleep(e_delay);    GPIO.output(en,True)
        time.sleep(e_pulse);    GPIO.output(en,False)
        time.sleep(e_delay)
    
    def lcd_string(self,message,line):
        self.lcd_byte(line,cmd)
        for i in range(len(message)):
            self.lcd_byte(ord(message[i]),chr)
        
        
    