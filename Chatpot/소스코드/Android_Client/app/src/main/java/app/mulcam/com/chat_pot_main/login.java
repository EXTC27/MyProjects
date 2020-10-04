package app.mulcam.com.chat_pot_main;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

public class login extends AppCompatActivity {
    EditText edit_id, edit_pw, inputid, inputpw;
    Button login_button, make_button;
    //TextView show;
    socketService socketservice;
    View createView;
    //user user;
    userService userservice;


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
            userService.MyBinder us = (userService.MyBinder) service;
            userservice = us.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplicationContext(), "서비스 연결 해제", Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_button = (Button) findViewById(R.id.login_button);
        make_button = (Button) findViewById(R.id.make_button);
        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_pw = (EditText) findViewById(R.id.edit_pw);

        Intent intent_socketservice = null;
        intent_socketservice = new Intent(this, socketService.class);
        bindService(intent_socketservice, conn, Context.BIND_AUTO_CREATE);

        Intent intent_userservice = null;
        intent_userservice = new Intent(this, userService.class);
        bindService(intent_userservice, conn2, Context.BIND_AUTO_CREATE);
    }

    public void onClick(View v) throws InterruptedException, JSONException {
        Intent intent_login = null;
        Intent intent_user = null;

        switch (v.getId()) {
            case R.id.login_button:

                String id = edit_id.getText().toString();
                String pw = edit_pw.getText().toString();
                intent_login = new Intent(this, menu.class);

                intent_login.putExtra("id", id);
                intent_login.putExtra("pw", pw);

                socketservice.jsonQue.offer(id);
                socketservice.jsonQue.offer("login");
                socketservice.jsonQue.offer(pw);

                socketservice.jsonSend();

                Thread.sleep(500);

                socketservice.jsonParsing();
                Toast.makeText(this, socketservice.loginTrueFalse, Toast.LENGTH_LONG).show();
                startActivity(intent_login);
                break;

            case R.id.make_button :

                createView = (View) View.inflate(this, R.layout.createuserdialog, null);
                AlertDialog.Builder createview = new AlertDialog.Builder(this);
                createview.setTitle("아이디 생성하기");
                createview.setView(createView);
                createview.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputid = (EditText) createView.findViewById(R.id.inputid);
                        inputpw = (EditText) createView.findViewById(R.id.inputpw);

                        String id = inputid.getText().toString();
                        String pw = inputpw.getText().toString();

                        String create = String.format("{\"header\" : {\"self\" : \"none\", \"command\" : \"%s\", \"option\" : \"none\"}, \"data\" : {\"Id\" : \"%s\", \"Pwd\" : \"%s\"}}",
                                "user_regi", id, pw);
                        socketservice.sendQue.offer(create);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                createview.setNegativeButton("취소", null);
                createview.show();
                break;
        }
    }
}