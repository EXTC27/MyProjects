import select, socket, sys
from multiprocessing import Process, Lock, Queue
from SV_Database import Database
import json

SERVER_IP = '70.12.107.167'

class Server():
    server_ip = ''
    backlog = 5
    devID = {}
    devMap = {}
    devNum = 0
    socketDict = {}

    def __init__(self, getip,getport):
        self.server_ip = getip
        self.server_port = getport
        self.server = socket.socket ( socket.AF_INET, socket.SOCK_STREAM)
        self.server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        print("[Server IP : {0:15s} - {1:5d}".format(self.server_ip,self.server_port))
        self.server_address = (self.server_ip,self.server_port)
        self.server.bind(self.server_address)
        self.server.listen(self.backlog)
        self.inputs = [self.server]
        self.clientNumDic ={}
        self.clientIP = {}
        self.clientSocket = {}
        self.clientNum = 0

    def standBy(self, comQ):
        while True :
            print("standBy Activating...")
            readable, writable, exceptional = select.select(self.inputs, [], [])
            try :
                for s in readable :
                    if s is self.server :
                        connection, self.client_address = s.accept()
                        print("[Connection from %s port %s ]" % self.client_address)

                        self.clientIP[self.clientNum] = self.client_address
                        self.clientSocket[self.clientNum] = connection
                        self.clientNumDic[connection] = self.clientNum
                        self.clientNum = self.clientNum + 1
                        print("clientSocket when s is self.server : ", self.clientSocket, ", clientNum : ", self.clientNum)
                        self.inputs.append( connection )
                        m_tempTuple = (self.clientSocket)

                    else :
                        self.data = s.recv(2048)
                        if self.data :
                            self.data = self.data.decode()

                            print(self.data)

                            #self.data = self.data.strip(' ')

                            #print("stripped : ",self.data)

                            data_json=eval(self.data)
                            devID = data_json["header"]["self"]



                            #json_acceptable_string = self.data.replace("'", "\"")
                            #data_json = json.loads(json_acceptable_string)

                            print("whole data : ", data_json)

                            #devID = data_json["header"]["self"]

                            if not self.devMap.get(devID) :
                                self.devMap[devID] = self.clientSocket[self.clientNumDic[s]]
                                print("adding to devMap!", self.devMap)

                            m_tempData = (s,data_json,self.devMap)

                            print(m_tempData)

                            comQ.put(m_tempData)
                            print(comQ)

                        else :
                            self.inputs.remove(s)
                            s.close()
                            print("[Client Discononected]")

            except KeyboardInterrupt :
                self.server.close()
                s.close()

    def chatBot(self,chatQ,datasendQ):
        pass

    def create_msg(self,type,data):
        temp_dic = {"header":{"self":"","command":"","option":""}}
        temp_dic["header"]["self"] = "server"
        if type=="to_dev_refresh":
            temp_dic["header"]["command"] = "refresh"
            temp_dic["header"]["option"] = "none"
            return str(temp_dic)
        if type=="to_dev_water":
            temp_dic["header"]["command"] = "water"
            temp_dic["header"]["option"] = "none"
            return str(temp_dic)
        if type=="to_avd_alldata":
            return data







    def dataSend(self, datasendQ):
        print("dataSend Activating")

        while True:
            if( datasendQ.empty() ):
                continue

            dataSend = datasendQ.get()

            m_tempSock = dataSend[0]
            send_type = dataSend[1]
            data = dataSend[2]

            send_msg = str(self.create_msg(send_type,data))

            print("Sending Socket : ")
            print(m_tempSock)

            print("Sending Data : ")
            print(send_msg)


            #m_tempSock.sendall(_len.encode())  # send the length of the data 4byte
            #m_tempSock.sendall(send_msg.encode())
            m_tempSock.sendall(("{:<1300}".format(send_msg).encode()))

    def __del__(self):
        self.server.close()




def main():

    db = Database()
    procs = []
    server = Server(SERVER_IP,8282)
    chat_server = Server(SERVER_IP,8181)

    commandQ = Queue()
    dataGetQ = Queue()
    dataSetQ = Queue()
    dataSendQ = Queue()
    chatbotQ = Queue()
    outputQ = Queue()
    dbLock = Lock()
    Qs = []

    procServer = Process(target=server.standBy, args=(commandQ,))
    procChat_Server = Process(target=chat_server.standBy, args=(commandQ,))
    procDataSend = Process(target=server.dataSend, args=(dataSendQ,))
    procDataGet = Process(target=db.getDB, args=(dataGetQ, dbLock, dataSendQ,outputQ,dataSetQ))
    procDataSet = Process(target=db.setDB, args=(dataSetQ,dataGetQ,dbLock,outputQ))

    procs.append(procServer)
    procs.append(procChat_Server)
    procs.append(procDataGet)
    procs.append(procDataSet)
    procs.append(procDataSend)

    Qs.append(commandQ)
    Qs.append(dataGetQ)
    Qs.append(chatbotQ)
    Qs.append(dataSetQ)
    Qs.append(dataSendQ)
    Qs.append(outputQ)

    try:
        for proc in procs:
            proc.start()

        while True:
            command_interpretor(commandQ, dataGetQ, dataSetQ,chatbotQ,dataSendQ,outputQ)

    except KeyboardInterrupt:
        for proc in procs:
            proc.join()
        for proc in procs:
            proc.terminate()

        for q in Qs:
            q.close()
        for q in Qs:
            q.join_thread()

        sys.exit()

    finally:
        for proc in procs:
            proc.join()
        for proc in procs:
            proc.terminate()

        for q in Qs:
            q.close()
        for q in Qs:
            q.join_thread()

        sys.exit()

def command_interpretor(comQ,getQ,setQ,chatQ,sendQ,outputQ):

    if comQ.empty():
        return


    cmd_tuple = comQ.get()

    print(cmd_tuple)

    devSocket = cmd_tuple[0]
    data_json = cmd_tuple[1]
    devMap = cmd_tuple[2]


    devID = data_json["header"]["self"]
    devCommand = data_json["header"]["command"]
    devOption = data_json["header"]["option"]
    devData = data_json["data"]

    print("devOption : ")
    print(devOption)

    print("command inter")

    if devCommand == "login":
        print("login requested")
        temp_tuple = (devSocket, devCommand, devID, devOption, devData)
        getQ.put(temp_tuple)

    if devCommand == "refresh_set":
        print("refresh_set confirmed")

        search_list = ["AutoWater","ChatPot"]
        temp_tuple = (devSocket,"search",devID,search_list,devData)
        getQ.put(temp_tuple)

        while True:
            if (not outputQ.empty()):
                AutoWater = outputQ.get()
                print("current AutoWater: ",AutoWater)
                break

        if (float(devData["Grou"])<1):
            print("low Grou now...")
            if AutoWater == "on":
                print("AutoWater is on.. send Water command")
                send_tuple = (devSocket,"to_dev_water","water")
                sendQ.put(send_tuple)
            else:
                pass

        print("hihi")

        temp_tuple = (devID, "set", devSocket, devData)
        setQ.put(temp_tuple)

        temp_tuple = (devSocket, devCommand, devID, "none", devData)

        print("refresh_data send",temp_tuple)
        getQ.put(temp_tuple)

        while True:
            if (not outputQ.empty()):
                UserID = outputQ.get()
                print(UserID)
                break

        devSocket_tmp = devMap[str(UserID)]

        temp_tuple = (devID,devCommand,devSocket_tmp,devData)
        setQ.put(temp_tuple)

    if devCommand == "edit_table":

        PotID = devOption
        itemname = devData["itemname"]
        updatedata = devData["updatedata"]
        temp_tuple= (PotID, devCommand,itemname,updatedata)
        setQ.put(temp_tuple)


    if devCommand == "heart_up":
        print("heart up command received")
        temp_tuple = (devID, "edit_table","Heart", 1)

        setQ.put(temp_tuple)

    if devCommand == "heart_down":
        print("heart down command received")
        temp_tuple = (devID, "edit_table", "Heart", -1)
        setQ.put(temp_tuple)

    if devCommand  == "set":
        print("set_data process beginning")

        search_list = ["AutoWater", "ChatPot"]
        temp_tuple = (devSocket, "search", devID, search_list, devData)
        getQ.put(temp_tuple)

        while True:
            if (not outputQ.empty()):
                AutoWater = outputQ.get()
                print("current AutoWater: ", AutoWater)
                break

        if (float(devData["Grou"]) < 1):
            print("low Grou now...")
            if AutoWater == "on":
                print("AutoWater is on.. send Water command")
                send_tuple = (devSocket, "to_dev_water", "water")
                sendQ.put(send_tuple)
            else:
                pass

        temp_tuple = (devID,devCommand,devSocket,devData)
        setQ.put(temp_tuple)

    if devCommand  == "get":
        print("get_data process beginning")
        temp_tuple = (devSocket,devCommand,devID, devOption, devData)
        getQ.put(temp_tuple)

    if devCommand == "chat":
        print("chat process beginning")

        temp_tuple = (devSocket, "refresh", devID, devOption, devData)
        getQ.put(temp_tuple)

        while True:
            if (not outputQ.empty()):
                PotID = outputQ.get()
                print(PotID)
                break

        temp_tuple = (devSocket,devCommand,PotID,devOption,devData)
        getQ.put(temp_tuple)

    if devCommand == "pot_regi":

        potID = devOption

        temp_tuple = (devID, devCommand, potID, devData)

        setQ.put(temp_tuple)

    if devCommand == "user_regi":

        print("user register beginning...")

        new_ID = devData["Id"]
        new_Pwd = devData["Pwd"]

        temp_tuple = (devID,devCommand,new_ID,new_Pwd)

        setQ.put(temp_tuple)


    if devCommand == "water":
        print("auto-water process beginning")
        send_tuple = (devSocket,"to_dev_water","water")
        sendQ.put(send_tuple)

    if devCommand == "refresh":
        print("refresh process beginning")
        print("devID :",int(devID))

        # AVD
        # devSocket_tmp = devMap[devOption]
        # print("단말 소켓 : ",devSocket_tmp)
        '''temp_tuple = (devSocket, devCommand, devID, devOption, devData)
        getQ.put(temp_tuple)

        while True:
            if (not outputQ.empty()):
                PotID = outputQ.get()
                print(PotID)
                break'''

        PotID = devOption

        devSocket_tmp = devMap[str(PotID)]
        print(devSocket_tmp)
        print("메롱")
        send_tuple_tmp = (devSocket_tmp, "to_dev_refresh", "none")

        sendQ.put(send_tuple_tmp)

        # send_tuple=(devSocket_tmp,"to_dev_refresh","refresh")

        '''if (int(devID)<100):
            print("hi")
            #AVD
            #devSocket_tmp = devMap[devOption]
            #print("단말 소켓 : ",devSocket_tmp)
            temp_tuple = (devSocket,devCommand,devID,devOption,devData)
            getQ.put(temp_tuple)

            while True:
                if (not outputQ.empty()):
                    PotID = outputQ.get()
                    print(PotID)
                    break

            devSocket_tmp = devMap[str(PotID)]
            print(devSocket_tmp)
            print("메롱")
            send_tuple_tmp = (devSocket_tmp,"to_dev_refresh","none")

            sendQ.put(send_tuple_tmp)

            #send_tuple=(devSocket_tmp,"to_dev_refresh","refresh")
            pass
        else:#POT
            pass
            '''

        #sendQ.put(send_tuple)


    '''if devCommand  == "chat_bot":
        print("chat_bot process beginning")
        temp_tuple = (devSocket,"안녕하세요!")
        sendQ.put(temp_tuple)
        #chatQ.put(temp_tuple)'''


if __name__ == "__main__":
    main()