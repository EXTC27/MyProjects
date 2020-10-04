/*
package app.mulcam.com.chat_pot_main;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class user extends Application {
    public String user_test = "hi i'm user";

    public String userid, potamount, heartpot, regdate, username, jsonstr;
    ArrayList<String> potlist_id = new ArrayList<String>();
    ArrayList<JSONObject> potlist_JSONObject = new ArrayList<JSONObject>();
    ArrayList<potIDInfoClass> potlist_potIDInfo = new ArrayList<potIDInfoClass>();
    potIDInfoClass potIDInfoclass;
    ArrayList<JSONObject> potsensorlist_JSONObject = new ArrayList<JSONObject>();
    ArrayList<potSensorDataClass> potsensorlist_potSensorData = new ArrayList<potSensorDataClass>();
    potSensorDataClass potsensordataclass;
*/
/*

    public user(String self, String jsonstr) throws JSONException {
        this.userid = self;
        this.jsonstr = jsonstr;
        userParse(jsonstr);
        //countPot_potIDInfo(jsonstr);
        //couuntPot_potSensorData(jsonstr);
    }
*//*


    public void user_start(String self, String jsonstr) throws JSONException {
        this.userid = self;
        this.jsonstr = jsonstr;
        userParse(jsonstr);
        //countPot_potIDInfo(jsonstr);
        //couuntPot_potSensorData(jsonstr);
    }


    void userParse(String jsonstr) throws JSONException {
        // user정보는 갯수가 적으므로 키값을 따로 추출하지 않고, 보기 쉽게 바로 변수에 저장
        // data - user
        JSONObject data_temp = new JSONObject(jsonstr);
        JSONObject data = data_temp.getJSONObject("data");
        JSONObject user = data.getJSONObject("user");
        this.username = user.getString("username");
        this.potamount = user.getString("potamount");
        this.heartpot = user.getString("heartpot");
        this.regdate = user.getString("regdate");
    }

    void countPot_potIDInfo(String jsonstr) throws JSONException {
        JSONObject data_temp = new JSONObject(jsonstr);
        JSONObject data = data_temp.getJSONObject("data");
        JSONObject potdata = data.getJSONObject("PotData");
        // potdata의 키값을 가져옴. 1. 첫번째 화분 2. 두번째 화분... 배열에 넣음
        Iterator i = potdata.keys();
        while(i.hasNext()) {
            String a = i.next().toString();
            potlist_id.add(a);
        }
        // 각 첫번재 화분, 두번재 화분.. object를 가져옴. 배열에 넣음
        for (int count = 0; count < Integer.parseInt(this.potamount); count++ ) {
            JSONObject b = potdata.getJSONObject(potlist_id.get(count));
            potlist_JSONObject.add(b);
        }

        if (!potlist_id.isEmpty() && !potlist_JSONObject.isEmpty()){
            if (potlist_id.size() == potlist_JSONObject.size()) {
                for (int k = 0; k<potlist_id.size(); k++) {
                    // potlist_potIDInfo 배열에 potIDInfo 클래스 add.
                    potlist_potIDInfo.add(new potIDInfoClass(potlist_id.get(k), potlist_JSONObject.get(k)));
                }
            }
        };

        */
/*
        for (int j = 0; j<potlist_id.size(); j++) {
            JSONObject potinfo = new JSONObject(potlist_id[j]);
            String b = jsonObject.getString()
            potlist_id[j].
            potlist_info.add(potlist_id[j])
            potlist_id[j]
        }*//*

    }
    void couuntPot_potSensorData(String jsonstr) throws JSONException {
        JSONObject data_temp = new JSONObject(jsonstr);
        JSONObject data = data_temp.getJSONObject("data");
        JSONObject potSensorData = data.getJSONObject("potSensorData");
        // 각 첫번재 화분, 두번재 화분의 센서데이터.. object를 가져옴. 배열에 넣음
        // countPot_potIDData에서 검사했던 potamount를 재활용함
        for (int count = 0; count < Integer.parseInt(this.potamount); count++ ) {
            JSONObject b = potSensorData.getJSONObject(potlist_id.get(count));
            potsensorlist_JSONObject.add(b);
        }

        if (!potlist_id.isEmpty() && !potsensorlist_JSONObject.isEmpty()) {
            if (potlist_id.size() == potsensorlist_JSONObject.size()) {
                for (int k = 0; k < potlist_id.size(); k++) {
                    potsensorlist_potSensorData.add(new potSensorDataClass(potlist_id.get(k), potsensorlist_JSONObject.get(k)));
                }
            }
        }
    }

}

class potIDInfoClass {
    String potID;
    JSONObject potIDObject;

    String Species, AutoWater, RegDate, Heart, GrowDate, PotNumber;

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
    }
}

class potSensorDataClass {
    String potID;
    JSONObject potSensorObject;

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
*/
