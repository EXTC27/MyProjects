package app.mulcam.com.chat_pot_main;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class userService extends Service {
    private final IBinder mBinder = new MyBinder();

    public String user_test = "imymemine";

    public String userid, potamount, heartpot, regdate, username, jsonstr, potsensordata, potdata;
    ArrayList<String> potlist_id = new ArrayList<String>();
    ArrayList<JSONObject> potlist_JSONObject = new ArrayList<JSONObject>();
    ArrayList<potIDInfoClass> potlist_potIDInfo = new ArrayList<potIDInfoClass>();
    potIDInfoClass potIDInfoclass;
    ArrayList<JSONObject> potsensorlist_JSONObject = new ArrayList<JSONObject>();
    ArrayList<potSensorDataClass> potsensorlist_potSensorData = new ArrayList<potSensorDataClass>();

    ArrayList<String> datalist = new ArrayList<String>();
    ArrayList<String> datalist_potid = new ArrayList<String>();


    potSensorDataClass potsensordataclass;

    public void user_start(String self, String jsonstr) throws JSONException {
        /*potlist_potIDInfo.clear();
        potsensorlist_potSensorData.clear();*/
        potlist_potIDInfo.clear();
        potsensorlist_potSensorData.clear();
        datalist.clear();
        datalist_potid.clear();

        this.userid = self;
        this.jsonstr = jsonstr;
        userParse(jsonstr);
        countPot_potIDInfo(jsonstr);
        //couuntPot_potSensorData(jsonstr);
    }

    void userParse(String jsonstr) throws JSONException {
        JSONObject data_temp = new JSONObject(jsonstr);
        JSONObject data = data_temp.getJSONObject("data");
        JSONObject user = data.getJSONObject("User");
        this.username = user.getString("UserID");
        this.potamount = user.getString("PotAmount");
        this.heartpot = user.getString("HeartTot");
        this.regdate = user.getString("RegDate");
    }

    public static boolean isStringDouble(String s) { // 숫자인지 아닌지 판별
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void countPot_potIDInfo(String jsonstr) throws JSONException {
        JSONObject data_temp = new JSONObject(jsonstr);
        JSONObject data = data_temp.getJSONObject("data");
        Iterator l = data.keys();
        while(l.hasNext()) {
            String b = l.next().toString();
            datalist.add(b);
        }

        // 100, user, 200 으로 이루어진 data 키값에 potid인 100과 200을 따로 담음
        for(int m = 0; m<datalist.size(); m++) {
            if (isStringDouble(datalist.get(m))) {
                datalist_potid.add(datalist.get(m));
            } else {
                continue;
            }
        }

        for(int n = 0; n<datalist_potid.size(); n++) {
            JSONObject potIDKeys = data.getJSONObject(datalist_potid.get(n));
            potIDInfo(datalist_potid.get(n), potIDKeys);
        }

        potlist_potIDInfo.add(null);
        potlist_potIDInfo.add(null);
        potlist_potIDInfo.add(null);
        potsensorlist_potSensorData.add(null);
        potsensorlist_potSensorData.add(null);
        potsensorlist_potSensorData.add(null);

    }

    void potIDInfo(String potID, JSONObject potIDKeys) throws JSONException {
        String id = potID;
        JSONObject potidkeys = potIDKeys;

        JSONObject chatpot = potidkeys.getJSONObject("ChatPot");
        JSONObject plantpotsensor = potidkeys.getJSONObject("PlantPotSensor");

        potlist_potIDInfo.add(new potIDInfoClass(id, chatpot));
        potsensorlist_potSensorData.add(new potSensorDataClass(id, plantpotsensor));
    }


    class MyBinder extends Binder {
        userService getService() {
            return userService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

class potIDInfoClass {
    String potID;
    JSONObject potIDObject;

    String Species, AutoWater, RegDate, Heart, GrowDate, PotNumber, PotName;

    public potIDInfoClass(String pot_id, JSONObject pot_id_object) throws JSONException {

        this.potID = pot_id;
        this.potIDObject = pot_id_object;
        potIDObjectParsing();
    }

    void potIDObjectParsing() throws JSONException {
        this.Species = this.potIDObject.getString("Species");
        this.AutoWater = this.potIDObject.getString("AutoWater");
        this.RegDate = this.potIDObject.getString("RegDate");
        this.Heart = this.potIDObject.getString("Heart");
        this.GrowDate = this.potIDObject.getString("GrowDate");
        this.PotNumber = this.potIDObject.getString("PotNumber");
        this.PotName = this.potIDObject.getString("PotName");
    }
}

class potSensorDataClass {
    String potID;
    JSONObject potSensorObject;
    //String PotName = "Noname";

    String Ligh, Temp, DeviceID, Time, Humi, Nutr, Grou;

    public potSensorDataClass(String pot_id, JSONObject potsensorlist_object) throws JSONException {
        this.potID = pot_id;
        this.potSensorObject = potsensorlist_object;
        potSensorDataParsing();
    }

    void potSensorDataParsing() throws JSONException {
        this.Ligh = this.potSensorObject.getString("Ligh");
        this.Temp = this.potSensorObject.getString("Temp");
        this.DeviceID = this.potSensorObject.getString("DeviceID");
        this.Time = this.potSensorObject.getString("Time");
        this.Humi = this.potSensorObject.getString("Humi");
        this.Nutr = this.potSensorObject.getString("Nutr");
        this.Grou = this.potSensorObject.getString("Grou");
    }
}

