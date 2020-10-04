package app.mulcam.com.chat_pot_main;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class chatService extends Service {
    public Queue<String> sendQue2 = new LinkedList<String>();
    public Queue<String> receiveQue2 = new LinkedList<String>();
    public Queue<String> jsonQue2 = new LinkedList<String>();

    public ArrayList<String> chatstroage = new ArrayList<String>();

    SocketClient client; // 만들 쓰레드
    ReceiveThread receive; // 만들 쓰레드
    SendThread send; // 만들 쓰레드
    Socket socket;
    Context context;
    public String IP = "70.12.107.167"; // 내꺼, 167은 해주꺼
    public int PORT = 8282;

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
        int port = 8181;

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
                    receiveQue2.offer(totalReceiveString);
                    chatstroage.add("화분 : " + totalReceiveString);
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
                    String sendProtocol = sendQue2.poll();
                    socket.getOutputStream().write(sendProtocol.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyBinder extends Binder {
        chatService getService() {
            return chatService.this;
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
    public String test;

    void jsonSend() throws JSONException, InterruptedException {
        checkProtocol = jsonQue2.poll();
        String potid = jsonQue2.poll();
        String chatdata = jsonQue2.poll();
        for (int i = 0; i<100; i++) {
            if(i == Integer.parseInt(checkProtocol)) {
                checkProtocol = Integer.toString(i);
                password = checkProtocol;
                break;
            }
        }
        test= String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"chat\", \"option\" : {\"%s\"}}, \"data\" : {\"%s\"}}",
                checkProtocol, potid, chatdata);

        sendQue2.offer(test);

    }

    void jsonParsing() throws JSONException {
        String jsonstr = receiveQue2.peek();
        try {
            // header

            JSONObject header_temp = new JSONObject(jsonstr);
            JSONObject header = header_temp.getJSONObject("header");
            String self = header.getString("self");
            String command = header.getString("command");
            String optionary = header.getString("option");

            // header - option 값 추출하는 방법
            JSONObject option_temp = header.getJSONObject("option");
            String option = option_temp.getString("login");

            //test용 넣기
            jsonQue2.offer(command);
            jsonQue2.offer(self);
            jsonQue2.offer(optionary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
