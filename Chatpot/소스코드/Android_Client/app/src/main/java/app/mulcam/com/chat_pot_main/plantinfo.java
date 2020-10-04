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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class plantinfo extends AppCompatActivity {
    socketService socketservice;
    userService userservice;

    String index;
    TextView potname, potspecies, pottemp, pothumi, potnutr, soilhumi, datatime, potnum, raisedate, phoneid;
    String loginID, DeviceID;
    Button changename, changespecies;
    View nameView, speciesView;
    EditText edit_name, edit_species;
    String editname, editspecies;
    RadioButton wateron, wateroff;
    RadioGroup water;
    Button waterset;
    // ★★★★★ 이 공간에는 이런 변수 선언 외에는 아무것도 하지 말것!!!!!!!!!!!!!!!!

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            socketService.MyBinder ss = (socketService.MyBinder) service;
            socketservice = ss.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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
        setContentView(R.layout.plantinfo);

        Intent intent_socketservice = null;
        intent_socketservice = new Intent(this, socketService.class);
        bindService(intent_socketservice, conn, Context.BIND_AUTO_CREATE);

        Intent intent_userservice = null;
        intent_userservice = new Intent(this, userService.class);
        bindService(intent_userservice, conn2, Context.BIND_AUTO_CREATE);

        changename = (Button) findViewById(R.id.changename);
        changespecies = (Button) findViewById(R.id.changespecies);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_species = (EditText) findViewById(R.id.edit_species);
        water = (RadioGroup) findViewById(R.id.water);
        wateron = (RadioButton) findViewById(R.id.wateron);
        wateroff = (RadioButton) findViewById(R.id.wateroff);

        potname = (TextView) findViewById(R.id.potname);
        potspecies = (TextView) findViewById(R.id.potspecies);
        pottemp = (TextView) findViewById(R.id.pottemp);
        pothumi = (TextView) findViewById(R.id.pothumi);
        potnutr = (TextView) findViewById(R.id.potnutr);
        soilhumi = (TextView) findViewById(R.id.soilhumi);
        datatime = (TextView) findViewById(R.id.datatime);
        potnum = (TextView) findViewById(R.id.potnum);
        raisedate = (TextView) findViewById(R.id.raisedate);
        phoneid = (TextView) findViewById(R.id.phoneid);

        Intent plantinfo = getIntent();
        loginID = plantinfo.getStringExtra("loginID");
        DeviceID = plantinfo.getStringExtra("DeviceID");

        ImageButton RefreshButton2 = (ImageButton) findViewById(R.id.RefreshButton2);

        index = plantinfo.getStringExtra("index");

        String Ligh = plantinfo.getStringExtra("Ligh");
        String Temp = plantinfo.getStringExtra("Temp");
        String Time = plantinfo.getStringExtra("Time");
        String Humi = plantinfo.getStringExtra("Humi");
        String Nutr = plantinfo.getStringExtra("Nutr");
        String Grou = plantinfo.getStringExtra("Grou");
        String PotName = plantinfo.getStringExtra("PotName");
        String RegDate = plantinfo.getStringExtra("RegDate");
        String Species = plantinfo.getStringExtra("Species");
        String AutoWater = plantinfo.getStringExtra("AutoWater");

        potspecies.setText("품종 : " + Species);
        potname.setText("이름 : " + PotName);
        pottemp.setText("실내기온 : " + Temp + "도");
        pothumi.setText("실내습도 : " + Humi+ "%");
        potnutr.setText("토양 영양분 : " + Nutr + "ds/m");
        soilhumi.setText("토양 수분 : " + Grou + "%");
        datatime.setText("데이터 받은 시간 : " + Time);

        raisedate.setText("기른 날짜 : " + RegDate);
        potnum.setText("Device ID : " + DeviceID);
        phoneid.setText("사용자 이름 : " + loginID);

        if(AutoWater == "on") {
            wateron.setChecked(true);
            wateroff.setChecked(false);
        } else if(AutoWater == "off") {
            wateron.setChecked(false);
            wateroff.setChecked(true);
        }
    }

    public void onClick(View v) throws JSONException, InterruptedException {
        switch (v.getId()) {
            case R.id.RefreshButton2:
                String refresh = String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {}}",
                        loginID, "refresh", DeviceID);
                socketservice.sendQue.offer(refresh);
                Thread.sleep(3000);
                int timeout = 5;
                while (socketservice.receiveQue.isEmpty() && timeout>0) {
                    Toast.makeText(this,"timeout : " + timeout, Toast.LENGTH_SHORT);
                    timeout--;
                    Thread.sleep(2000);
                }
                if ( timeout <= 0) {
                    Toast.makeText(this,"request timeout", Toast.LENGTH_SHORT);
                    return;
                }
                JSONObject sensordata = new JSONObject(socketservice.receiveQue.poll());

                userservice.potsensorlist_potSensorData.set(Integer.parseInt(index), new potSensorDataClass(DeviceID, sensordata));

                potname.setText("이름 : " + userservice.potlist_potIDInfo.get(Integer.parseInt(index)).PotName);
                pottemp.setText("실내기온 : " + userservice.potsensorlist_potSensorData.get(Integer.parseInt(index)).Temp + "도");
                pothumi.setText("실내습도 : " + userservice.potsensorlist_potSensorData.get(Integer.parseInt(index)).Humi + "%");
                potnutr.setText("토양 영양분 : " + userservice.potsensorlist_potSensorData.get(Integer.parseInt(index)).Nutr + "ds/m");
                soilhumi.setText("토양 수분 : " + userservice.potsensorlist_potSensorData.get(Integer.parseInt(index)).Grou + "%");
                datatime.setText("데이터 받은 시간 : " + userservice.potsensorlist_potSensorData.get(Integer.parseInt(index)).Time);

                raisedate.setText("기른 날짜 : " + userservice.potlist_potIDInfo.get(Integer.parseInt(index)).RegDate);
                break;

            case R.id.changename:
                nameView = (View) View.inflate(this, R.layout.dialog1, null);
                AlertDialog.Builder dlg1 = new AlertDialog.Builder(this);
                dlg1.setTitle("화분 이름 변경");
                dlg1.setView(nameView);
                //dlg1.setPositiveButton("확인", null);
                dlg1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit_name = (EditText) nameView.findViewById(R.id.edit_name);
                        editname = edit_name.getText().toString();
                        userservice.potlist_potIDInfo.get(Integer.parseInt(index)).PotName = editname;
                        String changename = String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {\"itemname\" : \"PotName\", \"updatedata\" : \"%s\"}}",
                                loginID, "edit_table", DeviceID, editname);
                        socketservice.sendQue.offer(changename);
                        potname.setText(editname);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dlg1.setNegativeButton("취소", null);
                dlg1.show();

                break;

            case R.id.changespecies:
                speciesView = (View) View.inflate(this, R.layout.dialog2, null);
                AlertDialog.Builder dlg2 = new AlertDialog.Builder(this);
                dlg2.setTitle("화분 품종 변경");
                dlg2.setView(speciesView);
                dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit_species = (EditText) speciesView.findViewById(R.id.edit_species);
                        editspecies = edit_species.getText().toString();
                        userservice.potlist_potIDInfo.get(Integer.parseInt(index)).Species = editspecies;
                        String changespecies = String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {\"itemname\" : \"Species\", \"updatedata\" : \"%s\"}}",
                                loginID, "edit_table", DeviceID, editspecies);
                        socketservice.sendQue.offer(changespecies);
                        potspecies.setText(editspecies);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
                dlg2.setNegativeButton("취소", null);
                dlg2.show();

                break;

            case R.id.wateron:
                userservice.potlist_potIDInfo.get(Integer.parseInt(index)).AutoWater = "on";
                //TEST.setText(userservice.potlist_potIDInfo.get(Integer.parseInt(index)).AutoWater);
                String water_on = String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {\"itemname\" : \"AutoWater\", \"updatedata\" : \"on\"}}",
                        loginID, "edit_table", DeviceID);
                socketservice.sendQue.offer(water_on);
                Thread.sleep(500);
                break;

            case R.id.wateroff:
                userservice.potlist_potIDInfo.get(Integer.parseInt(index)).AutoWater = "off";
                //TEST.setText(userservice.potlist_potIDInfo.get(Integer.parseInt(index)).AutoWater);
                String water_off = String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {\"itemname\" : \"AutoWater\", \"updatedata\" : \"off\"}}",
                        loginID, "edit_table", DeviceID);
                socketservice.sendQue.offer(water_off);
                Thread.sleep(500);
                break;
        }
    }
}
