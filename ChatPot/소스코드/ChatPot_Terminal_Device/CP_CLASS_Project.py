from CP_CLASS_GPIO_Library import DCMOTOR
from CP_CLASS_GPIO_Library import TempHumi
from CP_CLASS_GPIO_Library import Light
from CP_CLASS_GPIO_Library import SPI
from CP_CLASS_GPIO_Library import PIR
from multiprocessing import Process
from CP_CLASS_DB import DB_temp
import sys
import spidev
import time
import sqlite3
import socket
import picamera
import datetime
import json

class Process_Client(Process):
    
    server_ip='70.12.107.167'
    server_port=8282
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    server_address = (server_ip, server_port)
    print("[Connecting to %s port %s" % server_address)

    sock.connect(server_address)

    print("[Connected!]")
        
    def __init__(self,id,loop,q,q_pir,tempq,q_refresh):
        
        self.p=Process.__init__(self)
        self.id=id
        self.looop=loop
        self.que=q
        self.q_pir=q_pir
        self.tempq=tempq
        self.q_refresh=q_refresh
    
    def ProcessData(self):
        data=self.que.get()
        data=str(data.ljust(1024))
        self.sock.send(data.encode())
        print(data)
        #print(self.que.empty())
        while self.looop:
            
            '''if self.tempq.empty()==False:
                print("asd 1")
                data=self.q_refresh.get()
                
                data=data.ljust(1024)
                self.sock.send(data.encode())
                print(data)
                print(self.q_refresh.empty())                
                throway=self.tempq.get()
            else:
                pass'''
            
            #print("que",self.que.empty())
            #print("qq_refresh",self.q_refresh.empty())
            if self.q_refresh.empty()==True:
                
                if self.que.empty()==True:
                    #print("asd 4")
                    #print(self.q_pir.empty())
                    if self.q_pir.empty()==True:
                        pass
                    else:
                        print("asd 5")
                        data_pir=self.q_pir.get()
                        #print(data_pir)
                        data_pir=data_pir.ljust(1024)
                        self.sock.send(data_pir.encode())
                        pass
                    #print("??????")
                else:
                    #print("tempq",self.tempq.empty())
                    print("asd 2")
                    data=self.que.get()
                    data=data.ljust(1024)
                    self.sock.send(data.encode())
                    print("sensor", data)
                    pass
            else:
                data=self.q_refresh.get()
                data=data.ljust(1024)
                print("recv",data)
                self.sock.send(data.encode())
                print("send ok")
                
            
            '''if not self.tempq.empty():
                data_pir=self.q_pir.get()
                #print(data_pir)
                data_pir=data_pir.ljust(1024)
                self.sock.send(data_pir.encode())
                #print("???????")
                while(self.q_refresh.empty()):
                    data_pir=self.q_pir.get()
                    #print(data_pir)
                    data_pir=data_pir.ljust(1024)
                    self.sock.send(data_pir.encode())
                    print(data_pir) 
                    pass'''
                
            
        
    def Cutting(self,data,number):
        if len(str(data))>number:
            data=str(data)[0:number]
        elif len(str(data))<number:
            data=str(data).ljust(10,'0')
        return data
    
    def start_client(self):
        self.p=Process(target=self.ProcessData)
        self.p.start()
        
    def stop_client(self):
        self.p.stop()




class SetEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, set):
            return list(obj)
        return json.JSONEncoder.default(self, obj)

class Process_Sensors(Process):
    def __init__(self,id,loop,q, dataq, comq,q_refresh):
        self.th= TempHumi()
        self.light= Light()
        self.spi=SPI()
        self.p=Process.__init__(self)
        
        self.id=id
        self.looop=loop
        self.que=q
        self.dataque = dataq
        self.comque = comq
        self.q_refresh=q_refresh
        self.db=DB_temp()
    
    
    def ProcessSensor(self):
        
        dic_temp=''
        dic_humi=''
        dic_light=''
        dic_cds=''
        dic_vr=''
        #set_encoder=Se
        while self.looop:
            
            dic_temp=self.th.measure_tmp()
         
            dic_humi=self.th.measure_humi()
            
            dic_light=self.light.readLight()
            
            dic_cds=self.spi.printcds()
            
            dic_vr=self.spi.printvr()
            
            templist = [dic_temp, dic_humi, dic_light, dic_cds, dic_vr]
            
            while self.dataque.empty():
                self.dataque.put(templist)
                continue
                
            print("dataque empty?",self.dataque.empty())
            print(templist)
            
            j1 = {'header': {
                'self': self.id,
                'command':'set',
                'option':'all'
                }
                ,'data':{
                    'Temp': dic_temp,
                    'Humi': dic_humi,
                    'Ligh': dic_light,
                    'Nutr':dic_cds,
                    'Grou': dic_vr}}
            
            d1=json.dumps(j1,cls=SetEncoder)
            self.db.stock_figure(self.id,j1['data'])
            self.que.put(d1)
            delay_=10
            
            while delay_>0:
                #print("delay entered")
                if self.comque.empty():
                    
                    #print("delay 60")
                
                    time.sleep(1)
                    delay_-=1
                    
                    while not self.dataque.empty():
                        a=self.dataque.get()
                        
                    templist=[]
                    
                else:
                    throwaway=self.comque.get()
                    print("delay_", delay_)
                    break
            
            '''timer = 7
            while timer>0 :
                time.sleep(1)
                timer = timer -1
                throwaway = self.comque.get()
                break'''
            
        
    def start_detect(self):
        self.p=Process(target=self.ProcessSensor)
        self.p.start()
        
    def stop_detect(self):
        self.p.stop()

class Process_Pir(Process):
    
    def __init__(self,id,loop,q_pir,comq,q_refresh):
        self.p=Process.__init__(self)
        self.pir=PIR(24,'in')
        self.id=id
        self.loop=loop
        self.q_pir=q_pir
        self.comq=comq
        self.count=0
        self.q_refresh=q_refresh
    def run_pir(self):
        time_prev=int(time.time())
        while self.loop:  
            if not self.q_refresh.empty():
                pass
            else:
                if self.pir.PIR_motionDetection()==True:
                    self.count+=1
                    print(self.count)
                    
                    if self.count>=5:
                        self.count=0
                        pir_data='friendly +1'
                        j1 = {'header': {
                                'self': self.id,
                                'command':'heart_up',
                                'option':'pir'
                                }
                                ,'data':{
                                    'pir': pir_data}}
                        d1=json.dumps(j1,cls=SetEncoder)
                        self.q_pir.put(d1)
                        print("친밀도 1 상승")
                        
                        time_prev=int(time.time())
                        
                    '''with picamera.PiCamera() as camera:
                        camera.resolution=(320,240)
                        filename=str(datetime.datetime.now())
                        camera.start_preview()

                        time.sleep(1)

                        camera.stop_preview()
                        camera.capture(filename+'.jpg')'''
                time_now=int(time.time())
                #print("시간",time_now-time_prev)
                #print(time_now-time_prev)
                if (time_now-time_prev)>15:
                    
                    #print("친밀도 1 하락")
                    pir_data='friendly -1'
                    j1 = {'header': {
                            'self': self.id,
                            'command':'heart_down',
                            'option':'pir'
                            }
                            ,'data':{
                                'pir': pir_data}}
                    d1=json.dumps(j1,cls=SetEncoder)
                    self.q_pir.put(d1)
                    time_prev=int(time.time())
                time.sleep(3)

    def start_pir(self):
        self.p=Process(target=self.run_pir)
        self.p.start()
        
    def stop_pir(self):
        self.p.stop()
        
class Process_Recv(Process_Client):
    
    def __init__(self,id,loop,q, q_pir, dataq, comq,tempq,q_refresh):
        super().__init__(id,loop,q,q_pir,tempq,q_refresh)
        self.BUFSIZE=1300
        self.que = q
        self.q_pir=q_pir
        self.dataque = dataq
        self.comque = comq
        self.tempq=tempq
        self.q_refresh=q_refresh
        
    def run_listen(self):
        dc_pin=(4,25,12)
        dc= DCMOTOR(dc_pin,'out')
        recv_data=self.sock.recv(self.BUFSIZE).decode()
        
        
        while recv_data!=b'0':
            if recv_data=='0':
                print("[disconnected]")
                sys.exit()
                self.sock.close()
            else:
                print("success")
                print(recv_data)
            
                #json_string=recv_data.replace("'","\"")
                #data_json=json.loads(json_string)
                json_string=recv_data
                data_json = eval(json_string)
                recv_comm=data_json["header"]["command"]
                temp=[]
                temp.append("data")
                self.tempq.put(temp)
                #print(self.tempq.empty())
                
                print(recv_comm)
                if recv_comm=='water':
                    print("water called!")
                    #tempstr = 'water'
                    #self.comque.put(tempstr)
                    
                    dc.DCMOTOR_forward()
                    time.sleep(2)
                    dc.DCMOTOR_stop()
                    time.sleep(1)
                    dc.DCMOTOR_stop()
                    data_json=''
                    recv_comm=''
                    recv_data=''
                    #self.comque.
                    
                elif recv_comm=='refresh':
                    print("refresh called!")
                    tempstr = 'refresh'
                    self.comque.put(tempstr)
                    
                    all_data=self.dataque.get()
                    print(all_data)
                    
                    dic_temp=all_data[0]
         
                    dic_humi=all_data[1]
            
                    dic_light=all_data[2]
            
                    dic_cds=all_data[3]
            
                    dic_vr=all_data[4]
                    
                    
                    
                    j1 = {'header': {
                        'self': self.id,
                        'command':'refresh_set',
                        'option':'all'
                        }
                        ,'data':{
                            'Temp': dic_temp,
                            'Humi': dic_humi,
                            'Ligh': dic_light,
                            'Nutr':dic_cds,
                            'Grou': dic_vr}}
                    print("refresh j1 = ", j1)
                    d1=json.dumps(j1,cls=SetEncoder)
                    #self.db.stock_figure('100',j1['data'])
                    self.q_refresh.put(d1)
                    print("put ok")
                    
                    pass
                else:
                    dc.DCMOTOR_stop()
            
            recv_data=self.sock.recv(self.BUFSIZE).decode()
            
        print("deconnected")
        self.sock.close()
            
    def start_listen(self):
        self.p=Process(target=self.run_listen)
        self.p.start()
    
    def stop_listen(self):
        self.p.stop()



