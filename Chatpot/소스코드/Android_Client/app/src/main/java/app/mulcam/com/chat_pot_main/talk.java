package app.mulcam.com.chat_pot_main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class talk extends AppCompatActivity {
    TextView chatview, chatpotname;
    EditText inputchat;
    ImageButton sendbutton;
    Button chathistory;

    String loginID, DeviceID, PotName;

    chatService chatservice;

    ServiceConnection conn3 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatService.MyBinder cs = (chatService.MyBinder) service;
            chatservice = cs.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            chatservice.socketClose();
            Toast.makeText(getApplicationContext(), "서비스 연결 해제", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);

        chatview = (TextView) findViewById(R.id.chatview);
        chatpotname = (TextView) findViewById(R.id.chatpotname);
        inputchat = (EditText) findViewById(R.id.inputchat);
        sendbutton = (ImageButton) findViewById(R.id.sendbutton);
        chathistory = (Button) findViewById(R.id.chathistory);

        Intent talk = getIntent();
        loginID = talk.getStringExtra("loginID");
        DeviceID = talk.getStringExtra("DeviceID");
        PotName = talk.getStringExtra("PotName");

        Intent intent_chatservice = null;
        intent_chatservice = new Intent(this, chatService.class);
        bindService(intent_chatservice, conn3, Context.BIND_AUTO_CREATE);

        chatpotname.setText(PotName + "이랑 대화하기");

    }

    class chatThread extends Thread {
        public chatThread() {
        }

        @Override
        public void run() {
            for(int i = 0; i<chatservice.chatstroage.size(); i++) {
                chatview.append(chatservice.chatstroage.get(i));
            }
        }
    }

    public void onClick(View v) throws JSONException, InterruptedException {
        String chat = inputchat.getText().toString();
        switch (v.getId()) {
            case R.id.sendbutton:
                inputchat.setText("");


                chatservice.chatstroage.add("주인 : " + chat);
                chatservice.jsonQue2.offer(loginID);
                chatservice.jsonQue2.offer(DeviceID);
                chatservice.jsonQue2.offer(chat);
                chatview.append("주인 : " + chat + "\n");

                chatservice.jsonSend();
                Thread.sleep(300);

                while ( !chatservice.receiveQue2.equals(null)) {
                    break;
                    //continue;
                }
                chatview.append("화분 : " + chatservice.receiveQue2.poll() + "\n");
                break;
            case R.id.chathistory:
                chatview.setText("");
                for(int i = 0; i<chatservice.chatstroage.size(); i++) {
                    chatview.append(chatservice.chatstroage.get(i) + "\n");
                }
                break;
        }
    }
}
