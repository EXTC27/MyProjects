package app.mulcam.com.chat_pot_main;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;

public class json {
    String ID_Protocol;
    String id;
    String checkProtocol;
    String password;
    JSONObject ID_Request;
    public String test = "kk";

    socketService socketservice = new socketService();
    Queue<String> sendQue  = socketservice.sendQue;
    Queue<String> receiveQue  = socketservice.receiveQue;
    Queue<String> jsonQue  = socketservice.jsonQue;

    public json() {

    }

    void jsonSend() throws JSONException {
        checkProtocol = jsonQue.peek();
        for (int i = 0; i<100; i++) {
            if(i == Integer.parseInt(checkProtocol)) {
                checkProtocol = Integer.toString(i);
                password = checkProtocol;
                break;
            }
        }
        test= String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : {\"password\" : \"%s\"}}, \"data\" : {}}",
                checkProtocol, "login", password);
        //test = "{\"header\":{\"self\":\"USERID\",\"command\":\"login\",\"option\":{\"password\":\"password\"}}";
    }

    void jsonParsing() throws JSONException {
        //String jsonstr = receiveQue.poll();
        try {
            JSONObject temp = new JSONObject(test);
            JSONObject header = temp.getJSONObject("header");
            String command = header.getString("command");
            test = command;

            //JSONParser jasonparser = new JSONParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
