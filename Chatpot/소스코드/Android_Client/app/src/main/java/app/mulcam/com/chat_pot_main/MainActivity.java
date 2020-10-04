package app.mulcam.com.chat_pot_main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    socketService socketservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View v) {
        Intent intent_logintemp = null;
        Intent intent_socketservice = null;
        Intent intent_enter = null;
        switch (v.getId()) {
            case R.id.enter :
                intent_socketservice = new Intent(this, socketService.class);
                bindService(intent_socketservice, conn, Context.BIND_AUTO_CREATE);

                intent_enter = new Intent(this, login.class);
                intent_logintemp = new Intent(this, loginandmain_temp.class);
                break;
        }
        startActivity(intent_enter);
    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}