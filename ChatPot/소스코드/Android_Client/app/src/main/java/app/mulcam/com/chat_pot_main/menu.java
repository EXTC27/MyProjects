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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class menu extends AppCompatActivity {
    // USER 부분
    TextView user_id, user_friendly, user_totalpot;
    // 화분 목록 부분1
    TextView potinfo1_1, potinfo1_2, potinfo1_3;
    ImageButton ImageButton1_1, ImageButton1_2;
    // 화분 목록 부분2
    TextView potinfo2_1, potinfo2_2, potinfo2_3;
    ImageButton ImageButton2_1, ImageButton2_2;
    // 화분 목록 부분3
    TextView potinfo3_1, potinfo3_2, potinfo3_3;
    ImageButton ImageButton3_1, ImageButton3_2;
    // 추가 버튼
    ImageButton AddButton;


    socketService socketservice;
    userService userservice;
    chatService chatservice;

    // 상세 정보 보기로 넘어가는 데이터
    String index;
    String loginID;
    String Ligh, Temp, DeviceID, Time, Humi, Nutr, Grou, PotName, RegDate, Species, AutoWater;

    String id, pw;
    String potname;

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
        setContentView(R.layout.menu);

        user_id = (TextView) findViewById(R.id.user_id);
        user_friendly = (TextView) findViewById(R.id.user_friendly);
        user_totalpot = (TextView) findViewById(R.id.user_totalpot);

        potinfo1_1 = (TextView) findViewById(R.id.potinfo1_1);
        potinfo1_2 = (TextView) findViewById(R.id.potinfo1_2);
        potinfo1_3 = (TextView) findViewById(R.id.potinfo1_3);
        ImageButton1_1 = (ImageButton) findViewById(R.id.ImageButton1_1);
        ImageButton1_2 = (ImageButton) findViewById(R.id.ImageButton1_2);

        potinfo2_1 = (TextView) findViewById(R.id.potinfo2_1);
        potinfo2_2 = (TextView) findViewById(R.id.potinfo2_2);
        potinfo2_3 = (TextView) findViewById(R.id.potinfo2_3);
        ImageButton2_1 = (ImageButton) findViewById(R.id.ImageButton2_1);
        ImageButton2_2 = (ImageButton) findViewById(R.id.ImageButton2_2);

        potinfo3_1 = (TextView) findViewById(R.id.potinfo3_1);
        potinfo3_2 = (TextView) findViewById(R.id.potinfo3_2);
        potinfo3_3 = (TextView) findViewById(R.id.potinfo3_3);
        ImageButton3_1 = (ImageButton) findViewById(R.id.ImageButton3_1);
        ImageButton3_2 = (ImageButton) findViewById(R.id.ImageButton3_2);

        AddButton = (ImageButton) findViewById(R.id.AddButton);

        Intent intent_socketservice = null;
        intent_socketservice = new Intent(this, socketService.class);
        bindService(intent_socketservice, conn, Context.BIND_AUTO_CREATE);

        Intent intent_userservice = null;
        intent_userservice = new Intent(this, userService.class);
        bindService(intent_userservice, conn2, Context.BIND_AUTO_CREATE);

        Intent intent_chatservice = null;
        intent_chatservice = new Intent(this, chatService.class);
        bindService(intent_chatservice, conn3, Context.BIND_AUTO_CREATE);

        Intent intent_menu = getIntent();
        id = intent_menu.getStringExtra("id");
        pw = intent_menu.getStringExtra("pw");
    }
    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("환영합니다! 챗팟입니다!");
        builder.setMessage("처음 오셨나요? 챗팟 생성부터 시작하세요!");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(menu.this,"추가 버튼을 눌러주세요!",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    public void onClick(View v) throws JSONException, InterruptedException {
        Intent intent_plantinfo = new Intent(this, plantinfo.class);
        Intent intent_talk = new Intent(this, talk.class);
        Intent intent_add = new Intent(this, add.class);
        loginID = socketservice.loginID;

        switch (v.getId()) {
            case R.id.AddButton :
                intent_add.putExtra("loginID", loginID);

                startActivity(intent_add);
                break;

            case R.id.RefreshButton :
                //////////이걸로 바꿀거임.
                socketservice.jsonQue.offer(id);
                socketservice.jsonQue.offer("login");
                socketservice.jsonQue.offer(pw);

                socketservice.jsonSend();

                Thread.sleep(500);

                socketservice.jsonParsing();

                Thread.sleep(500);

                userservice.user_start(socketservice.loginID, socketservice.jsonstr);

                user_id.setText("ID : " + userservice.username);
                user_friendly.setText("친밀도 : " + userservice.heartpot);
                user_totalpot.setText("보유 화분 수 : " + userservice.potamount);

                if(userservice.potlist_potIDInfo.get(0) != null) {
                    potinfo1_1.setText("이름 : " + userservice.potlist_potIDInfo.get(0).PotName);
                    potinfo1_2.setText("품종 : " + userservice.potlist_potIDInfo.get(0).Species);
                    potinfo1_3.setText("친밀도 : " + userservice.potlist_potIDInfo.get(0).Heart);
                } else if (userservice.potlist_potIDInfo.get(0) == null) {
                    potinfo1_1.setText("이름 : 등록되지 않음");
                    potinfo1_2.setText("품종 : 등록되지 않음");
                    potinfo1_3.setText("친밀도 : 등록되지 않음");
                }
                /////////else if 쓰면 안됨!!!!!!!!!!!!!! 이유는... 같은 get(0)을 비교하는 것이 아니니깐
                if (userservice.potlist_potIDInfo.get(1) != null) {
                    potinfo2_1.setText("이름 : " + userservice.potlist_potIDInfo.get(1).PotName);
                    potinfo2_2.setText("품종 : " + userservice.potlist_potIDInfo.get(1).Species);
                    potinfo2_3.setText("친밀도 : " + userservice.potlist_potIDInfo.get(1).Heart);
                } else if (userservice.potlist_potIDInfo.get(1) == null) {
                    potinfo2_1.setText("이름 : 등록되지 않음");
                    potinfo2_2.setText("품종 : 등록되지 않음");
                    potinfo2_3.setText("친밀도 : 등록되지 않음");
                }
                /////////(2)는 지금 배열이 열려있지 않은 상태임. 그래서 에러가 계속 뜬거임. userService에서 배열 크기를 5로 맞춰줌
                ///////// 하지만 5로 맞춰줘도 5까지 열어준다는 의미이지 (3)에 값을 넣지 않는이상 nullpointexception이 뜸. 그래서 또 에러뜸
                if (userservice.potlist_potIDInfo.get(2) != null) {
                    potinfo3_1.setText("이름 : " + userservice.potlist_potIDInfo.get(2).PotName);
                    potinfo3_2.setText("품종 : " + userservice.potlist_potIDInfo.get(2).Species);
                    potinfo3_3.setText("친밀도 : " + userservice.potlist_potIDInfo.get(2).Heart);
                } else if (userservice.potlist_potIDInfo.get(2) == null) {
                    potinfo3_1.setText("이름 : 등록되지 않음");
                    potinfo3_2.setText("품종 : 등록되지 않음");
                    potinfo3_3.setText("친밀도 : 등록되지 않음");
                }

                if (userservice.potlist_potIDInfo.get(0) == null) {
                    show();
                }

                break;

            case R.id.ImageButton1_1 :
                if(userservice.potlist_potIDInfo.get(0) != null) {
                    intent_talk.putExtra("loginID", loginID);
                    DeviceID = userservice.potsensorlist_potSensorData.get(0).DeviceID;
                    intent_talk.putExtra("DeviceID", DeviceID);
                    potname = userservice.potlist_potIDInfo.get(0).PotName;
                    intent_talk.putExtra("PotName", potname);
                    startActivity(intent_talk);
                } else if (userservice.potlist_potIDInfo.get(0) == null) {
                    Toast.makeText(this, "등록되지 않은 화분입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ImageButton1_2 :
                if(userservice.potlist_potIDInfo.get(0) != null) {
                    index = "0";
                    intent_plantinfo.putExtra("index", index);
                    intent_plantinfo.putExtra("loginID", loginID);

                    DeviceID = userservice.potsensorlist_potSensorData.get(0).DeviceID;
                    Ligh = userservice.potsensorlist_potSensorData.get(0).Ligh;
                    Temp = userservice.potsensorlist_potSensorData.get(0).Temp;
                    Time = userservice.potsensorlist_potSensorData.get(0).Time;
                    Humi = userservice.potsensorlist_potSensorData.get(0).Humi;
                    Nutr = userservice.potsensorlist_potSensorData.get(0).Nutr;
                    Grou = userservice.potsensorlist_potSensorData.get(0).Grou;
                    PotName = userservice.potlist_potIDInfo.get(0).PotName;
                    RegDate = userservice.potlist_potIDInfo.get(0).RegDate;
                    intent_plantinfo.putExtra("RegDate", RegDate);
                    Species = userservice.potlist_potIDInfo.get(0).Species;
                    intent_plantinfo.putExtra("Species", Species);
                    AutoWater = userservice.potlist_potIDInfo.get(0).AutoWater;
                    intent_plantinfo.putExtra("AutoWater", AutoWater);

                    intent_plantinfo.putExtra("PotName", PotName);
                    /////////////////////////Potname

                    intent_plantinfo.putExtra("DeviceID", DeviceID);
                    intent_plantinfo.putExtra("Ligh", Ligh);
                    intent_plantinfo.putExtra("Temp", Temp);
                    intent_plantinfo.putExtra("Time", Time);
                    intent_plantinfo.putExtra("Humi", Humi);
                    intent_plantinfo.putExtra("Nutr", Nutr);
                    intent_plantinfo.putExtra("Grou", Grou);

                    startActivity(intent_plantinfo);
                } else if (userservice.potlist_potIDInfo.get(0) == null) {
                    Toast.makeText(this, "등록되지 않은 화분입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ImageButton2_1 :
                if(userservice.potlist_potIDInfo.get(1) != null) {
                    intent_talk.putExtra("loginID", loginID);
                    DeviceID = userservice.potsensorlist_potSensorData.get(1).DeviceID;
                    intent_talk.putExtra("DeviceID", DeviceID);
                    potname = userservice.potlist_potIDInfo.get(1).PotName;
                    intent_talk.putExtra("PotName", potname);
                    startActivity(intent_talk);
                } else if (userservice.potlist_potIDInfo.get(1) == null) {
                    Toast.makeText(this, "등록되지 않은 화분입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ImageButton2_2 :
                if(userservice.potlist_potIDInfo.get(1) != null) {
                    index = "1";
                    intent_plantinfo.putExtra("index", index);
                    intent_plantinfo.putExtra("loginID", loginID);

                    DeviceID = userservice.potsensorlist_potSensorData.get(1).DeviceID;
                    Ligh = userservice.potsensorlist_potSensorData.get(1).Ligh;
                    Temp = userservice.potsensorlist_potSensorData.get(1).Temp;
                    Time = userservice.potsensorlist_potSensorData.get(1).Time;
                    Humi = userservice.potsensorlist_potSensorData.get(1).Humi;
                    Nutr = userservice.potsensorlist_potSensorData.get(1).Nutr;
                    Grou = userservice.potsensorlist_potSensorData.get(1).Grou;
                    PotName = userservice.potlist_potIDInfo.get(1).PotName;
                    RegDate = userservice.potlist_potIDInfo.get(1).RegDate;
                    intent_plantinfo.putExtra("RegDate", RegDate);
                    Species = userservice.potlist_potIDInfo.get(1).Species;
                    intent_plantinfo.putExtra("Species", Species);
                    AutoWater = userservice.potlist_potIDInfo.get(1).AutoWater;
                    intent_plantinfo.putExtra("AutoWater", AutoWater);

                    intent_plantinfo.putExtra("PotName", PotName);

                    intent_plantinfo.putExtra("DeviceID", DeviceID);
                    intent_plantinfo.putExtra("Ligh", Ligh);
                    intent_plantinfo.putExtra("Temp", Temp);
                    intent_plantinfo.putExtra("Time", Time);
                    intent_plantinfo.putExtra("Humi", Humi);
                    intent_plantinfo.putExtra("Nutr", Nutr);
                    intent_plantinfo.putExtra("Grou", Grou);
                    //potinfo3_1.setText(DeviceID + Ligh + Temp + Time + Humi + Nutr+ Grou);
                    startActivity(intent_plantinfo);
                } else if (userservice.potlist_potIDInfo.get(1) == null) {
                    Toast.makeText(this, "등록되지 않은 화분입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ImageButton3_1 :
                if(userservice.potlist_potIDInfo.get(2) != null) {
                    intent_talk.putExtra("loginID", loginID);
                    DeviceID = userservice.potsensorlist_potSensorData.get(2).DeviceID;
                    intent_talk.putExtra("DeviceID", DeviceID);
                    potname = userservice.potlist_potIDInfo.get(2).PotName;
                    intent_talk.putExtra("PotName", potname);
                    startActivity(intent_talk);
                } else if (userservice.potlist_potIDInfo.get(2) == null) {
                    Toast.makeText(this, "등록되지 않은 화분입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ImageButton3_2 :
                if(userservice.potlist_potIDInfo.get(2) != null) {
                    index = "2";
                    intent_plantinfo.putExtra("index", index);
                    intent_plantinfo.putExtra("loginID", loginID);

                    DeviceID = userservice.potsensorlist_potSensorData.get(2).DeviceID;
                    Ligh = userservice.potsensorlist_potSensorData.get(2).Ligh;
                    Temp = userservice.potsensorlist_potSensorData.get(2).Temp;
                    Time = userservice.potsensorlist_potSensorData.get(2).Time;
                    Humi = userservice.potsensorlist_potSensorData.get(2).Humi;
                    Nutr = userservice.potsensorlist_potSensorData.get(2).Nutr;
                    Grou = userservice.potsensorlist_potSensorData.get(2).Grou;
                    PotName = userservice.potlist_potIDInfo.get(2).PotName;
                    RegDate = userservice.potlist_potIDInfo.get(2).RegDate;
                    intent_plantinfo.putExtra("RegDate", RegDate);
                    Species = userservice.potlist_potIDInfo.get(2).Species;
                    intent_plantinfo.putExtra("Species", Species);
                    AutoWater = userservice.potlist_potIDInfo.get(2).AutoWater;
                    intent_plantinfo.putExtra("AutoWater", AutoWater);

                    intent_plantinfo.putExtra("PotName", PotName);

                    intent_plantinfo.putExtra("DeviceID", DeviceID);
                    intent_plantinfo.putExtra("Ligh", Ligh);
                    intent_plantinfo.putExtra("Temp", Temp);
                    intent_plantinfo.putExtra("Time", Time);
                    intent_plantinfo.putExtra("Humi", Humi);
                    intent_plantinfo.putExtra("Nutr", Nutr);
                    intent_plantinfo.putExtra("Grou", Grou);

                    startActivity(intent_plantinfo);
                } else if (userservice.potlist_potIDInfo.get(2) == null) {
                    Toast.makeText(this, "등록되지 않은 화분입니다.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}