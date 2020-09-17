import RPi.GPIO as GPIO
import time

LEFT,RIGHT,CENTER,UP,DOWN=16,20,21,5,6 #left,right,center,up,down

class my_JOG:
    
    def __init__(self):        
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(LEFT, GPIO.IN)
        GPIO.setup(RIGHT, GPIO.IN)
        GPIO.setup(CENTER, GPIO.IN)
        GPIO.setup(UP, GPIO.IN)
        GPIO.setup(DOWN, GPIO.IN)
        '''
        GPIO.add_event_detect(JOG[0], GPIO.RISING, callback = self.JOG_in)
        GPIO.add_event_detect(JOG[1], GPIO.RISING, callback = self.JOG_in)
        GPIO.add_event_detect(JOG[2], GPIO.RISING, callback = self.JOG_in)
        GPIO.add_event_detect(JOG[3], GPIO.RISING, callback = self.JOG_in)
        GPIO.add_event_detect(JOG[4], GPIO.RISING, callback = self.JOG_in)
        
        
    def JOG_in(self,channel):
        if channel==JOG[0]:
            return 'LEFT'
        elif channel==JOG[1]:
            return 'RIGHT'
        elif channel==JOG[2]:
            return 'CENTER'
        elif channel==JOG[3]:
            return 'UP'
        elif channel==JOG[4]:
            return 'DOWN'
        
    '''
    

        
    
    
    