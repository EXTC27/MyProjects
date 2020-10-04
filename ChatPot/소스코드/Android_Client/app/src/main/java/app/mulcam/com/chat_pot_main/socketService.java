package app.mulcam.com.chat_pot_main;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class socketService extends Service {
    public Queue<String> sendQue = new LinkedList<String>();
    public Queue<String> receiveQue = new LinkedList<String>();
    public Queue<String> jsonQue = new LinkedList<String>();

    public String test_string1 = "";
    public String test_string2 = "";
    public String test_string3 = "";

    public String jsonstr = "";
    public String loginID = "";
    public String loginTrueFalse = "";

    SocketClient client; // 만들 쓰레드
    ReceiveThread receive; // 만들 쓰레드
    SendThread send; // 만들 쓰레드
    Socket socket;
    Context context;
    public String IP = "70.12.107.174"; // 내꺼, 167은 해주꺼
    public int PORT = 8282;
    public String test_string = "{\"header\":{\"self\":\"USERID\",\"command\":\"login\",\"option\":{\"password\":\"password\"}}";

    private final IBinder mBinder = new MyBinder();

    public void socketClose() {
        try {
            socket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void socketService_start() {
        String ip = "70.12.107.167";
        int port = 8282;

        client = new SocketClient(ip, port);
        client.start();
    }

    class SocketClient extends Thread {
        String ip;
        int port;
        OutputStream outputStream = null;
        DataOutputStream output = null;

        public SocketClient(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(ip, port);
                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket);
                send = new SendThread(socket);
                receive.start();
                send.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class ReceiveThread extends Thread {
        Socket socket;
        DataInputStream input;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            while(true) {
                try {
                    byte[] totalReceiveByte = new byte[2048];
                    input.read(totalReceiveByte, 0, 2048);
                    String totalReceiveString = new String(totalReceiveByte);
                    receiveQue.offer(totalReceiveString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class SendThread extends Thread {
        Socket socket;
        DataOutputStream output;

        public SendThread(Socket socket) {
            this.socket = socket;
            try{
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while(true) {
                try {
                    //String sendProtocol = "hihi";
                    String sendProtocol = sendQue.poll();
                    //Toast.makeText(getApplicationContext(), sendProtocol, Toast.LENGTH_SHORT).show();
                    socket.getOutputStream().write(sendProtocol.getBytes());// 왜 while문에서 벗어나는거지??
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyBinder extends Binder {
        socketService getService() {
            return socketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.socketService_start();
        Toast.makeText(getApplicationContext(), "Socket Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String ID_Protocol;
    String id;
    String checkProtocol;
    String password;
    public String login;

    void jsonSend() throws JSONException, InterruptedException {
        // 서버에게 보낼 때 반드시 3개의 명령어를 송신하고 수신해야한다. 그래야 밑에 format을 맞출 수 있음
        String checkID = jsonQue.poll();
        loginID = checkID;
        String command_sort = jsonQue.poll();
        String option = jsonQue.poll();
        for (int i = 0; i<100; i++) {
            if(i == Integer.parseInt(checkID)) {
                checkID = Integer.toString(i);
                break;
            }
        }
        if (command_sort.equals("login")) {
            login= String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {}}",
                    checkID, "login", option);
        }
        // 중간테스트용 refresh //
        else if (command_sort.equals("refresh")) {
            login= String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {}}",
                    checkID, "refresh", option);
        }
        sendQue.offer(login);
    }

    void jsonParsing() throws JSONException {
        ////////////////////// 중간테스트용 코드/////////////////////
        /*String plantpot = "";
        //String plantpotsensor = "";
        plantpot = receiveQue.poll();
        //plantpotsensor = receiveQue.poll();  해주가 값을 2번 나눠서 보낼 때!!*/
        ////////////////////// 중간테스트용 코드/////////////////////
        jsonstr = receiveQue.poll();
        test_string1 = jsonstr;
        try {
            // header
            JSONObject header_temp = new JSONObject(jsonstr);
            JSONObject header = header_temp.getJSONObject("header");
            String self = header.getString("self");
            String command = header.getString("command");
            String optionary = header.getString("option");

            // header - option 값 추출하는 방법
            JSONObject option_temp = header.getJSONObject("option");
            String login = option_temp.getString("login");

            if (login.equals("true")) {
                loginTrueFalse = "Welcome : Login Accepted";
                //user = new user(loginID, jsonstr);
            } else if (login.equals("false")) {
                loginTrueFalse = "Please input right ID or Password";
            } else {
                loginTrueFalse = "Error : Any data don't be received";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void jsonParsing2() throws JSONException {
        String plantpot_refresh = "";
        plantpot_refresh = receiveQue.poll();
        try {
            JSONObject pr = new JSONObject(plantpot_refresh);
            String temp2 = pr.getString("Temp");
            String deviceid2 = pr.getString("DeviceID");
            String humi2 = pr.getString("Humi");
            String ligh2 = pr.getString("Ligh");
            String time2 = pr.getString("Time");
            String grou2 = pr.getString("Grou");
            String nutr2 = pr.getString("Nutr");
            test_string3 = "온도 : " + temp2 + "\n" + "디바이스 : " + deviceid2 + "\n" + "습도 : " + humi2 + "\n" + "빛 : " + ligh2 + "\n" + "갱신시간 : " + time2 + "\n" + "흙 습도 : " + grou2 +  "\n" + "영양 : " + nutr2 + "\n";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}