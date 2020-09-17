import RPi.GPIO as GPIO
import time

AIN1,AIN2,PWMA = 4,25,12

class my_Motor:
    
    def __init__(self):        
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(AIN1,GPIO.OUT,initial=GPIO.LOW)
        GPIO.setup(AIN2,GPIO.OUT,initial=GPIO.LOW)
        GPIO.setup(PWMA,GPIO.OUT)
        self.p=GPIO.PWM(PWMA,100)
        self.p.start(0)

    def open(self): 
        GPIO.output(AIN2, GPIO.LOW)
        GPIO.output(AIN1, GPIO.HIGH)
        self.p.ChangeDutyCycle(15)
        
    def stop(self):
        GPIO.output(AIN1, GPIO.LOW)
        GPIO.output(AIN2, GPIO.LOW)    
        self.p.ChangeDutyCycle(0)  
    
    def close(self):
        GPIO.output(AIN2, GPIO.HIGH)
        self.p.ChangeDutyCycle(7)          
        
    def reopen(self, time_):
        self.stop()
        self.open()        
        time.sleep(time_)
        self.stop()
        time.sleep(1)
        
        
        
    

        
    
    
    
