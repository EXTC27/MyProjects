import sqlite3
from CE_CLASS_Chatbot import Response
from datetime import datetime, date

class Database():
    __dataBasePath = "SV_Database.db"

    '''String temp = "";
    temp = get(response);
    que.put(temp)'''


    global_temp_dic={}

    def __init__(self):

        self.chat_bot = Response()

        print("Chatbot init finished")

        dataBase = sqlite3.connect(self.__dataBasePath)
        with dataBase:
            csr = dataBase.cursor()
            csr.execute( ' CREATE TABLE if not exists Data (DataNumber integer primary key autoincrement , DeviceID int, Temp char(8),Humi char(8),Ligh char(8),Nutr char(8),Grou char(8), Time date)' )
            print("Connected to the database[PlantPotSensor]")

            dataBase.commit()
        dataBase.close()
        print("Database[PlantPotSensor] init finished")

        dataBase = sqlite3.connect(self.__dataBasePath)
        with dataBase:
            csr = dataBase.cursor()
            csr.execute(
                ' CREATE TABLE if not exists User (UserID char(8),Pwd char(8),PotAmount char(8),HeartTot char(8),RegDate char(8))')
            print("Connected to the database[User]")

            dataBase.commit()
        dataBase.close()
        print("Database[User] init finished")

        dataBase = sqlite3.connect(self.__dataBasePath)
        with dataBase:
            csr = dataBase.cursor()
            csr.execute(
                'CREATE TABLE if not exists ChatPot (PotID char(8),UserID char(8),PotName char(8),PotNumber integer primary key autoincrement,Heart char(8),'
                'Species char(8),AutoWater char(8),RegDate char(8),GrowDate char(8))')
            print("Connected to the database[ChatPot]")

            dataBase.commit()
        dataBase.close()
        print("Database[ChatPot] init finished")
        #self.user_data_init()
        #print("User data init completed")

    def user_data_init(self):

        dataBase = sqlite3.connect(self.__dataBasePath)

        user_id = "10"
        password = "1234"
        with dataBase:
            csr = dataBase.cursor()
            csr.execute("SELECT Pwd FROM User WHERE UserID = "+user_id+"")
            result = csr.fetchone()
            if result:
                pass
            else:
                csr.execute("INSERT INTO User (UserID,Pwd)VALUES(?,?)", (user_id,password))
                csr.execute("INSERT INTO ChatPot (PotID,UserID,PotName,Heart,Species,AutoWater,RegDate)VALUES(?,?,?,?,?,?,?)",
                            ("100", user_id,"noname", "1", "Sansevieria", "off", datetime.strftime(datetime.now(),'%Y-%m-%d %H-%M-%S')))
                csr.execute("INSERT INTO ChatPot (PotID,UserID,PotName,Heart,Species,AutoWater,RegDate)VALUES(?,?,?,?,?,?,?)",
                            ("200", user_id,"noname", "1", "Sansevieria", "off",
                             datetime.strftime(datetime.now(), '%Y-%m-%d %H-%M-%S')))


            dataBase.commit()
        dataBase.close()

    def login_chekced(self,dbLock,loginQ):

        dbLock.acquire()

        dataBase = sqlite3.connect(self.__dataBasePath)

        user_id = "10"
        password = "1234"

        UserID = loginQ[0]
        Password = loginQ[2]

        print(UserID)
        print(Password)

        print("login checking")

        dataList=[]

        with dataBase:
                    csr = dataBase.cursor()
                    csr.execute("SELECT Pwd FROM User WHERE UserID = "+UserID+"")

                    for row in csr.fetchall():
                        temp_list = []

                        for comp in row:
                            temp_list.append(str(comp))
                            #print(comp)

                        dataList.append(temp_list)

                    dataBase.commit()

        '''with dataBase:
            csr = dataBase.cursor()
            csr.execute("IF NOT EXISTS ( SELECT "+PotID+" FROM PlantPot WHERE UserID = "+UserID+") BEGIN INSERT INTO PlantPot (UserID, PotID) VALUES ("+UserID+", "+PotID+") END")

            dataBase.commit()'''

        print("Password : ",Password)
        print("dataList[0][0]",dataList[0][0])

        if Password == dataList[0][0]:
            print("로그인 확인")
        else:
            print("로그인 실패")

        dataBase.close()
        dbLock.release()

    def get_potID(self,dbLock,loginQ):

        print("get_potID begin")

        dbLock.acquire()

        dataBase = sqlite3.connect(self.__dataBasePath)

        UserID = loginQ[0]
        PotID = loginQ[1]

        with dataBase:
            csr = dataBase.cursor()
            csr.execute(
                "SELECT PotID FROM ChatPot where UserID = "+UserID+"")
            if csr.fetchall:
                m_templist = []
                for row in csr.fetchall():


                    for comp in row:
                        m_templist.append(str(comp))

                print("found_PotIDs : ", m_templist)

            dataBase.commit()

        dataBase.close()
        dbLock.release()

        return m_templist

    def getSpecificElement(self,getQ,dbLock):

        dbLock.acquire()
        dataBase = sqlite3.connect(self.__dataBasePath)

        element = getQ[0]
        dbname = getQ[1]
        devname = getQ[2]

        with dataBase:
            temp_str =""
            data_list=[]
            temp_list=[]
            csr = dataBase.cursor()

            csr.execute("SELECT "+element+" FROM "+dbname+" where PotID\
                 = ?", (devname,))
            for row in csr.fetchall():
                m_templist = []

                for comp in row:
                    temp_list.append(str(comp))
                    #print("comp: ", comp)

                data_list.append(temp_list)
            dataBase.commit()
        dbLock.release()

        return data_list[0][0]




    def getDB(self, getQ, dbLock, sendQ,outputQ,setQ):
        print("getDB running")

        while True:

            if ( not getQ.empty() ):

                m_getQ_data = getQ.get()

                devSocket = m_getQ_data[0]
                devCommand = m_getQ_data[1]
                if devCommand =="login_ok":
                    devID = m_getQ_data[2]["devID"]
                    potID_list = m_getQ_data[2]["PotID_list"]
                else:
                    devID = m_getQ_data[2]
                    potID_list = "none"
                    PotID = "none"
                devOption = m_getQ_data[3]
                if devCommand == "search":
                    s_element = devOption[0]
                    s_dbname = devOption[1]
                devData = m_getQ_data[4]

                if devCommand == "login":
                    print("login start")
                    tuple_tmp = (devID,"100",devOption)
                    self.login_chekced(dbLock,tuple_tmp)
                    potID_list = self.get_potID(dbLock,tuple_tmp)


                    ID={"devID":devID,"PotID_list":potID_list}
                    getQ_tuple = (devSocket, "login_ok",ID,devOption,devData)
                    getQ.put(getQ_tuple)
                    #getQ_tuple = (devSocket, "login_ok_PlantPotSensor", key, devOption, devData)
                    #getQ.put(getQ_tuple)

                    continue

                login_process = ["login_ok_User","login_ok_ChatPot","login_ok_PlantPotSensor"]



                print("getQ : ", m_getQ_data)
                dataBase = sqlite3.connect(self.__dataBasePath)

                tempstr=""


                sel_query={"get":"SELECT Temp,Humi,Ligh,Nutr,Grou,DeviceID,Time FROM Data where DeviceID\
                 = ? ORDER BY Time DESC LIMIT 3",\
                           "login_ok_ChatPot":"SELECT PotID,PotName,PotNumber,Heart,Species,AutoWater,RegDate,GrowDate from ChatPot where PotID = ?",\
                           "login_ok_PlantPotSensor":"SELECT Temp,Humi,Ligh,Nutr,Grou,DeviceID,Time FROM Data where DeviceID\
                 = ? ORDER BY Time DESC LIMIT 3",\
                           "chat":"SELECT Temp,Humi,Ligh,Nutr,Grou FROM Data where DeviceID\
                 = ? ORDER BY Time DESC LIMIT 3",\
                           "refresh":"SELECT PotID FROM ChatPot where UserID = ?",\
                           "refresh_set":"SELECT UserID FROM ChatPot where PotID = ?",\
                           "login_ok_User":"SELECT UserID,PotAmount,HeartTot,RegDate FROM User where UserID = ?"}
                sel_target = {"get":devID,"login_ok_ChatPot":PotID,"login_ok_PlantPotSensor":PotID,"chat":devID,"refresh":devID,\
                              "refresh_set":devID,"login_ok_User":devID}
                sel_DicList = {"get":["Temp","Humi","Ligh","Nutr","Grou","DeviceID","Time"],"login_ok_ChatPot":["PotID","PotName","PotNumber","Heart","Species","AutoWater","RegDate","GrowDate"],"login_ok_PlantPotSensor":["Temp","Humi","Ligh","Nutr","Grou","DeviceID","Time"],\
                               "chat":["Temp","Humi","Ligh","Nutr","Grou"],\
                               "refresh":["PotID"],\
                               "refresh_set":["UserID"],\
                               "login_ok_User":["UserID","PotAmount","HeartTot","RegDate"]}
                sel_Dic = {"get":{"Temp": "", "Humi": "", "Ligh": "", "Nutr": "", "Grou": "","DeviceID":"","Time":""},"login_ok_ChatPot":{"PotID":"","PotName":"","PotNumber": "", "Heart": "", "Species": "", "AutoWater": "", "RegDate": "","GrowDate":""},"login_ok_PlantPotSensor":{"Temp": "", "Humi": "", "Ligh": "", "Nutr": "", "Grou": "","DeviceID":"","Time":""},\
                           "chat":{"Temp": "", "Humi": "", "Ligh": "", "Nutr": "", "Grou": "","Heart":"","Species":""},\
                           "refresh":{"PotID":""},"refresh_set":{"UserID":""},\
                           "login_ok_User":{"UserID":"","PotAmount":"","HeartTot":"","RegDate":""}}
                
                #print(sel_query[devCommand])

                loginData_by_PotID=[]
                loginP_output={"login_ok_User":"","login_ok_ChatPot":"","login_ok_PlantPotSensor":""}

                dataList = []



                if devCommand == "login_ok":

                    sel_target_2 = {"get": devID, "login_ok_ChatPot": PotID, "login_ok_PlantPotSensor": PotID,
                                    "chat": devID, "refresh": devID,
                                    "refresh_set": devID, "login_ok_User": devID}

                    loginP_output = {"login_ok_User": "", "login_ok_ChatPot": "",
                                     "login_ok_PlantPotSensor": ""}

                    devCommand = "login_ok_User"

                    with dataBase:
                        dbLock.acquire()
                        dataList = []

                        DicList = sel_DicList[devCommand]
                        print("DicList", DicList)

                        csr = dataBase.cursor()

                        csr.execute(sel_query[devCommand], (sel_target_2[devCommand],))
                        print(sel_query[devCommand])
                        for row in csr.fetchall():
                            m_templist = []
                            dataDic = sel_Dic[devCommand]
                            # print("dataDIc : ",dataDic)
                            a = 0

                            for comp in row:
                                dataDic[DicList[a]] = str(comp)
                                # print("comp: ",comp)
                                a = a + 1

                            dataList.append(dataDic)
                            # print("m_tempstr : ", m_templist, ", len : ", len(m_templist))
                        dataBase.commit()
                    dbLock.release()
                    loginP_output[devCommand] = dataList[0]


                    for PotID in potID_list:

                        sel_target_2 = {"get": devID, "login_ok_ChatPot": PotID, "login_ok_PlantPotSensor": PotID,
                                      "chat": devID, "refresh": devID,
                                      "refresh_set": devID, "login_ok_User": devID}

                        loginP_output = {"login_ok_User": "", "login_ok_ChatPot": "",
                                         "login_ok_PlantPotSensor": ""}

                        for key in login_process:
                            devCommand = key


                            with dataBase:
                                dbLock.acquire()
                                dataList = []

                                DicList = sel_DicList[devCommand]
                                print("DicList" , DicList)

                                csr = dataBase.cursor()

                                csr.execute(sel_query[devCommand], (sel_target_2[devCommand],))
                                print(sel_query[devCommand])
                                for row in csr.fetchall():
                                    m_templist = []
                                    dataDic = sel_Dic[devCommand]
                                    #print("dataDIc : ",dataDic)
                                    a = 0

                                    for comp in row:
                                        dataDic[DicList[a]] = str(comp)
                                        #print("comp: ",comp)
                                        a = a + 1

                                    dataList.append(dataDic)
                                    # print("m_tempstr : ", m_templist, ", len : ", len(m_templist))
                                dataBase.commit()
                            dbLock.release()
                            loginP_output[devCommand]=dataList[0]

                        loginData_by_PotID.append(str(loginP_output))
                        print("loginP_output : ",loginP_output)
                        print("logindata : ",loginData_by_PotID)


                elif devCommand == "search":


                    with dataBase:
                        dbLock.acquire()
                        dataList = []

                        #DicList = sel_DicList[devCommand]
                        #print("DicList", DicList)

                        csr = dataBase.cursor()

                        if s_dbname == "ChatPot":
                            targetname = "PotID"

                        csr.execute("SELECT "+s_element+" FROM "+s_dbname+" where "+targetname+"\
                 = ?", (devID,))
                        #print(sel_query[devCommand])
                        for row in csr.fetchall():
                            m_templist = []

                            for comp in row:
                                m_templist.append(str(comp))
                                print("comp: ", comp)

                            dataList.append(m_templist)
                            # print("m_tempstr : ", m_templist, ", len : ", len(m_templist))
                        dataBase.commit()
                    dbLock.release()
                    print("search data : ",dataList[0][0])
                    outputQ.put(dataList[0][0])

                else:

                    with dataBase:
                        dbLock.acquire()
                        dataList = []

                        DicList = sel_DicList[devCommand]

                        csr = dataBase.cursor()


                        csr.execute(sel_query[devCommand], (sel_target[devCommand],))
                        for row in csr.fetchall():
                            m_templist = []
                            dataDic = sel_Dic[devCommand]
                            a=0

                            for comp in row:
                                dataDic[DicList[a]] = str(comp)
                                #print("comp: ",comp)
                                a = a + 1



                            dataList.append(dataDic)
                            #print("m_tempstr : ", m_templist, ", len : ", len(m_templist))
                        dataBase.commit()
                    dbLock.release()
                #tempstr += str(dataList[0])
                #dataList_len = len(dataList)



                if dataList:
                    dataDictmp = dataList[0]
                else:
                    dataDictmp = {}

                #print(tempstr)

                print("devOption : ",devOption)
                print("devCommand : ",devCommand)

                if devCommand == "login_ok_User":
                    devCommand = "login_ok_PlantPotSensor"


                if devCommand == "login_ok_ChatPot":
                    sendData = (devSocket, "to_avd_alldata", dataDictmp)
                    print("sendData : ", sendData)
                    outputQ.put(dataDictmp)
                    #sendQ.put(sendData)


                if devCommand == "login_ok_PlantPotSensor":

                    '''while True:
                        if (not outputQ.empty()):
                            Dic_tmp = outputQ.get()
                            print("Didc_PlantPot = ",Dic_tmp)
                            break'''

                    User_Dic = loginP_output["login_ok_User"]
                    ##PlantPot_Dic = loginP_output["login_ok_PlantPot"]
                    #PlantPotS_Dic = loginP_output["login_ok_PlantPotSensor"]

                    finalDic = {"header": {"self": "server", "command": "login", "option": {"login": "true"}}, \
                                "data": {"User": User_Dic}}
                    print("potID_List:",potID_list)

                    count=0
                    PotAmount = str(len(potID_list))
                    HeartTot = 0

                    for potID in potID_list:
                        eval_dic = eval(loginData_by_PotID[count])
                        PlantPot_Dic = eval_dic["login_ok_ChatPot"]
                        PlantPotS_Dic = eval_dic["login_ok_PlantPotSensor"]
                        temp_dic={potID:{"ChatPot":PlantPot_Dic,"PlantPotSensor":PlantPotS_Dic}}
                        HeartTot = HeartTot + int(PlantPot_Dic["Heart"])

                        finalDic["data"].update(temp_dic)
                        print("finalDic : ",finalDic)

                        count=count+1



                    UserUpdateDic = {"HeartTot":HeartTot,"PotAmount":PotAmount}
                    UserUpdateList =["HeartTot","PotAmount"]

                    UserID = devID
                    print("User ID: ",UserID)

                    #UserID = PlantPot_Dic["UserID"]

                    for key in UserUpdateList:
                        itemname = key
                        updatedata = UserUpdateDic[key]
                        temp_tuple = (UserID, "update_user", itemname, updatedata)
                        setQ.put(temp_tuple)


                    sendData = (devSocket, "to_avd_alldata", finalDic)
                    print("sendData : ", sendData)
                    sendQ.put(sendData)

                    #dataDictmp.update(Dic_tmp)



                if devOption == "all":
                    sendData = (devSocket, "to_avd_alldata", dataDictmp)
                    print("all command : ",devCommand)
                    print("all dataDictmp : ",dataDictmp)
                    print("sendData : ", sendData)
                    sendQ.put(sendData)


                if devCommand == "chat":
                    print("Chat process in getdb starting...")

                    element="Heart"
                    dbname="ChatPot"
                    temp_tuple=(element,dbname,devID)
                    dataDictmp["Heart"] = (self.getSpecificElement(temp_tuple,dbLock))

                    #element="Species"
                    #dbname="ChatPot"
                    #temp_tuple = (element,dbname,devID)
                    #dataDictmp["Species"] = (self.getSpecificElement(temp_tuple,dbLock))

                    dataDictmp["Species"] = 'Sansevieria'

                    print(self.chat_bot.chatbot(devData,dataDictmp))


                    while True:
                        sendmsg = self.chat_bot.chatbot(devData,dataDictmp)
                        if sendmsg:
                            break;
                        else:
                            pass
                    sendData = (devSocket,"to_avd_alldata",sendmsg)
                    sendQ.put(sendData)

                if devCommand == "refresh":
                    print("refresh requesting to dev...")
                    outputQ.put(dataDictmp["PotID"])

                if devCommand == "refresh_set":
                    print("refresh_set confirmed...")
                    outputQ.put(dataDictmp["UserID"])





            else :
                pass

    def setDB(self, setQ, getQ,dbLock,outputQ):
        print("setDB running")
        while True:
            if ( not setQ.empty() ):
                print("db writing")
                dbLock.acquire()
                setQData = setQ.get()

                dev_ID = setQData[0]
                devCommand = setQData[1]
                devSocket = setQData[2]
                dev_Data = setQData[3]

                dataBase = sqlite3.connect(self.__dataBasePath)
                with dataBase:
                    csr = dataBase.cursor()

                    if devCommand == "update_user":
                        element = devSocket
                        modi = dev_Data
                        updated_str = str(modi)

                        input_str = "UPDATE User SET " + str(element) + " = \'" + str(
                            updated_str) + "\' where UserID = " + str(dev_ID)
                        print(input_str)

                        csr.execute(input_str)


                    elif devCommand == "edit_table":

                        element = devSocket

                        csr.execute("SELECT "+element+" FROM  ChatPot where PotID\
                                         = ?", (dev_ID,))

                        temp = csr.fetchone()

                        print("element  :",element)

                        if (element == "Heart"):
                            modi = int(dev_Data)

                            current_val = int(temp[0])
                            print("current heart = ",current_val)
                            current_val = current_val+modi
                            if current_val<1:
                                current_val = 1
                            elif current_val>3:
                                current_val = 3
                            updated_str = str(current_val)
                            print("updated heart = ", updated_str)
                        else:
                            modi = dev_Data
                            updated_str = str(modi)
                        dbLock.release()
                        dbLock.acquire()
                        print("element = ",element)
                        print("modi = ",updated_str)
                        print("dev_id = ",dev_ID)

                        #csr.execute("UPDATE ChatPot SET PotName = 'hihi' Where PotID = '100'")

                        input_str = "UPDATE ChatPot SET "+str(element)+" = \'"+str(updated_str)+"\' where PotID = "+str(dev_ID)
                        print(input_str)

                        csr.execute(input_str)

                    elif devCommand == "user_regi":

                        print("zz")

                        newID = devSocket
                        newPwd = dev_Data

                        print("user registering...")

                        csr.execute(
                            "INSERT INTO User (UserID,Pwd,RegDate)VALUES(?,?,?)",
                            (newID, newPwd,datetime.strftime(datetime.now(), '%Y-%m-%d %H-%M-%S')))

                        print("user register complete")


                    elif devCommand == "pot_regi":

                        csr.execute(
                            "INSERT INTO ChatPot (PotID,UserID,PotName,Heart,Species,AutoWater,RegDate,GrowDate)VALUES(?,?,?,?,?,?,?,?)",
                            (devSocket, dev_ID, dev_Data["PotName"], "1", dev_Data["Species"], "off",
                             datetime.strftime(datetime.now(), '%Y-%m-%d %H-%M-%S'),dev_Data["GrowDate"]))

                        print("pot register complete")


                    else:

                        csr.execute("INSERT INTO Data (DeviceID,Temp,Humi,Ligh,Nutr,Grou,Time)VALUES(?,?,?,?,?,?,?)", (int(dev_ID),dev_Data["Temp"],dev_Data["Humi"],dev_Data["Ligh"],dev_Data["Nutr"],dev_Data["Grou"],datetime.strftime(datetime.now(),'%Y-%m-%d %H-%M-%S')))


                    dataBase.commit()
                dbLock.release()

                if devCommand == "refresh_set":

                    temp_tuple = (devSocket,"get",dev_ID,"all","none")
                    getQ.put(temp_tuple)

            else :
                pass



if __name__ == '__main__':
    pass

