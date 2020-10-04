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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class loginandmain_temp extends AppCompatActivity {
    EditText edit_id, edit_chat;
    Button send_id, renew, send_chat;
    TextView show, txtMessage;
    socketService socketservice;
    //################
    chatService chatservice;
    //################

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            socketService.MyBinder ss = (socketService.MyBinder) service;
            socketservice = ss.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            socketservice.socketClose();

            Toast.makeText(getApplicationContext(), "서비스 연결 해제", Toast.LENGTH_LONG).show();
        }
    };
    ServiceConnection conn2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //################
            chatService.MyBinder cs = (chatService.MyBinder) service;
            chatservice = cs.getService();
            //################
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //################
            chatservice.socketClose();
            //################
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginandmain_temp);

        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_chat = (EditText) findViewById(R.id.edit_chat);
        send_id = (Button) findViewById(R.id.send_id);
        renew = (Button) findViewById(R.id.renew);
        send_chat = (Button) findViewById(R.id.send_chat);
        show = (TextView) findViewById(R.id.show);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        // socketservice의 intent와 bindService는 반드시 onCreate에서 작성할 것!!
        // 이것들을 onClick에서 작성을 하니깐 socketService.java에 메소드든, 변수든 가져오기만 하면 에러가 발생함
        // 아마 서비스 구동의 시간적인 여유를 주지 않고 바로 socketService.java에 접근을 해서 그런가..? 라고 생각함.
        Intent intent_socketservice = null;
        intent_socketservice = new Intent(this, socketService.class);
        bindService(intent_socketservice, conn, Context.BIND_AUTO_CREATE);
    }

    public void onClick(View v) throws InterruptedException, JSONException {
        String id = edit_id.getText().toString();
        String chat = edit_chat.getText().toString();
        //################
        Intent intent_chatservice = null;
        //################

        switch (v.getId()) {
            case R.id.send_id:
                //################
                intent_chatservice = new Intent(this, chatService.class);
                bindService(intent_chatservice, conn2, Context.BIND_AUTO_CREATE);
                //################
                //socketservice.sendQue.offer(id);
                // Thread를 잠시 멈춰줘야함. sendQue에 명령어를 넣고. receiveQue에 값을 받을 때까지 어느정도의 시간이 필요함
                // sleep이 없으면 2번 요청해야 값을 받을 수 있음. 즉 1번 요청 때에는 receiveQue에 소켓에서 값을 받을 시간이 부족함
                //Thread.sleep(50);
                //String receive_data = socketservice.receiveQue.peek();
                //Toast.makeText(this, "아이디를 보냈습니다.", Toast.LENGTH_SHORT).show();
                /*socketservice.jsonQue.offer(id);
                socketservice.jsonQue.offer("refresh");
*/

                socketservice.sendQue.offer("{\"header\" : {\"self\" : \"10\", \"command\" : \"refresh\", \"option\" : \"123\"}, \"data\" : {}}");
                //socketservice.jsonSend();
                Thread.sleep(500);

                //socketservice.jsonParsing();
                //String Temp = "";
                //Temp = socketservice.test_string1 + socketservice.test_string2;

                //Thread.sleep(50);

                /*String self_a = socketservice.jsonQue.poll();
                String login = socketservice.jsonQue.poll();
                String option = socketservice.jsonQue.poll();
                String total = self_a + "\n" + login + "\n" + option;*/

                //String Temp = socketservice.receiveQue.poll() +socketservice.receiveQue.poll();

                /*while ( (socketservice.test_string1.equals("")) || (socketservice.test_string2.equals("")) || (Temp.equals("")) ) {
                    continue;
                }*//*
                while ( Temp.equals("")) {
                    continue;
                }*/

                show.setText(socketservice.receiveQue.poll());
                //intent.putExtra("total", test_string);
                //intent.putExtra("name", arr[0]);
                break; // ★★★★★ break; 넣는거 절대 까먹지마세요
            case R.id.renew :
                socketservice.jsonQue.offer(id);
                socketservice.jsonQue.offer("refresh");

                socketservice.jsonSend();
                Thread.sleep(500);
                /*
                socketservice.jsonParsing2();
                String Temp2 = "";
                Temp2 = socketservice.test_string3;*/

                //show.setText(socketservice.receiveQue.poll());
                //show.setText(socketservice.receiveQue.poll());

                //socketservice.jsonParsing();

                /*String self_a2 = socketservice.jsonQue.poll();
                String login2 = socketservice.jsonQue.poll();
                String option2 = socketservice.jsonQue.poll();
                String total2 = self_a2 + "\n" + login2 + "\n" + option2;
                */
                //String Temp2 = socketservice.test_string1 + socketservice.test_string2;
                //                //show.setText(Temp2);
                break;

            case R.id.send_chat :
                //################
                edit_chat.setText("");
                chatservice.jsonQue2.offer(id);
                chatservice.jsonQue2.offer(chat);
                txtMessage.append("주인 : " + chat + "\n");

                chatservice.jsonSend();
                Thread.sleep(300);

                while ( !chatservice.receiveQue2.equals(null)) {
                    break;
                    //continue;
                }
                txtMessage.append("화분 : " + chatservice.receiveQue2.poll() + "\n");
                break;

                //################
        }
    }
}