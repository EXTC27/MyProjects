import sqlite3
import time
from datetime import datetime,date

class DB_temp:
    __dataBasePath = "CP_Database.db"
    

    def __init__(self):
        self.dataBase = sqlite3.connect(self.__dataBasePath)
        with self.dataBase:
            csr = self.dataBase.cursor()
            #csr.execute("DELETE from Data")
            csr.execute( ' CREATE TABLE if not exists Data (DeviceID int, Temp char(8),Humi char(8),Ligh char(8),Nutr char(8),Grou char(8), Time date)' )
            print("Connected to the database")

            self.dataBase.commit()
        self.dataBase.close()
        print("Database init finished")
        
    def stock_figure(self,id,dev_Data):
        self.dataBase = sqlite3.connect(self.__dataBasePath)
        
        with self.dataBase:
            csr = self.dataBase.cursor()
            csr.execute("INSERT INTO Data (DeviceID,Temp,Humi,Ligh,Nutr,Grou,Time)VALUES(?,?,?,?,?,?,?)", (int(id),dev_Data["Temp"],dev_Data["Humi"],dev_Data["Ligh"],dev_Data["Nutr"],dev_Data["Grou"],datetime.strftime(datetime.now(),'%Y-%m-%d %H-%M-%S')))
            
            csr.execute("SELECT Time from Data")
            list_time=csr.fetchall()
            time=[]
            
            for i in range(len(list_time)):
                time.append(list_time[i][0])
            #print(len(time))
            
            delete_row=min(time)
        
            if(len(time)>100):
                sql=("DELETE from Data WHERE Time= ?")
                csr.execute(sql,(delete_row,))
                #print(len(time))