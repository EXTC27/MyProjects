from CP_CLASS_Project import Process_Client
from CP_CLASS_Project import Process_Sensors
from CP_CLASS_Project import Process_Pir
from CP_CLASS_Project import Process_Recv
from CP_CLASS_Project import DCMOTOR
import time
from multiprocessing import Queue
id='300'
try:    
    loop=True
    q=Queue()
    q_pir=Queue()
    
    comq = Queue()
    dataq = Queue()
    tempq=Queue()
    q_refresh=Queue()
    
    client=Process_Client(id,loop,q,q_pir,tempq,q_refresh)
    recv=Process_Recv(id,loop,q,q_pir, dataq, comq,tempq,q_refresh)
    sensors=Process_Sensors(id,loop,q, dataq, comq,q_refresh)
    pir=Process_Pir(id,loop,q_pir,comq,q_refresh)
    
    sensors.start_detect()
    client.start_client()
    recv.start_listen()
    pir.start_pir()
    
except KeyboardInterrupt:
    client.stop_client()
    recv.stop_listen()
    sensors.stop_detect()
    pir.stop_pir()
    spi.close()
    sys.exit()
    




