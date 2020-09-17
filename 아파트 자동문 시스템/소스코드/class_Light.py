import smbus2 as smbus
import time

device=0x23
power_down=0x00
power_on=0x01
reset=0x07
continuous_low_res_mode=0x13
continuous_high_res_mode_1=0x10
continuous_high_res_mode_2=0x11
one_time_high_res_mode_1=0x20
one_time_high_res_mode_2=0x21
one_time_low_res_mode=0x23
    
class my_Light:
    bus=0
    data=0
    val=0
    def __init__(self):        
        self.bus=smbus.SMBus(1)
        
    def convertToNumber(self, data):
        return ((data[1]+(256*data[0]))/1.2)

    def readLight(self, addr = device):
        self.data=self.bus.read_i2c_block_data(addr,continuous_high_res_mode_1,2)
        time.sleep(0.01)
        self.val=self.convertToNumber(self.data) 
        return self.val
    



