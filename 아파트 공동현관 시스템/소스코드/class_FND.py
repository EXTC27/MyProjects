import smbus2 as smbus
import time

addr=0x20
config_port=0x06
out_port=0x02
data = (0xFC,0x60,0xDA,0xF2,0x66,0xB6,0x3E,0xE0,0xFE,0xF6)
data_2 = (0xFC,0xCE,0x9E,0xEC)
data_3 = (0x9C,0xEE,0x1C,0x1C)
data_4 = (0x9C,0x6E,0xEE,0xEC,0xF6,0x9E)
L=0x1D
dash=0x02
digit=(0x7F,0xBF,0xDF,0xEF,0xF7,0xFB)
out_disp=0
none=0x00        
number=[None]*6

class my_FND:
    def __init__(self):
        self.bus = smbus.SMBus(1) 
        self.bus.write_word_data(addr,config_port,0x0000)
        
    def fnd_break(self):
        self.bus.write_word_data(addr,out_port,0x00FF)
        time.sleep(0.00001)

    def fnd_disp(self, num, timeout):
        #global addr,config_port,out_port,data,digit,out_disp        
        num_str=str(num) #change to string
        #print(num_str[1])
        for i in range(0,len(num_str),1):
            number[i+(len(number)-len(num_str))]=num_str[i]
        number[0]='L'
        start=time.time()
        stop=time.time()
        while stop-start <= timeout:
            for j in range(0,6,1):
                if number[j] == None:
                    out_disp=none<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == 'L':
                    out_disp=L<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                else:
                    out_disp=data[int(number[j])]<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                time.sleep(0.002)
                self.fnd_break()
            stop=time.time()
        #self.bus.write_word_data(addr,out_port,0x00FF)
            
    def p_fnd_disp(self, num, delay):
        number = [' ']*6
        for i in range(0,len(num),1):
            number[i+(len(number)-len(num))]=num[i]    
        for j in range(0,6,1):
                if number[j] == ' ':
                    out_disp=none<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == '-':
                    out_disp=dash<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == '-.':
                    out_disp=(dash+1)<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == 'Open':
                    out_disp=data_2[j-2]<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == 'Call':
                    out_disp=data_3[j-2]<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                elif number[j] == 'Change':
                    out_disp=data_4[j]<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)
                else:
                    out_disp=data[int(number[j])]<<8 | digit[j]
                    self.bus.write_word_data(addr,out_port,out_disp)                
                time.sleep(delay)
                self.fnd_break()
        
    