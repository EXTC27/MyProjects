package app.mulcam.com.chat_pot_main;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class add extends AppCompatActivity {
    int mYear, mMonth, mDay, mHour, mMinute;
    int index=0;
    socketService socketservice;

    TextView  showdate;
    EditText setname, setspecies;
    RadioGroup setdate;
    RadioButton today, pickdate;
    Button add;
    EditText setnum;

    String loginID = "";

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh");

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
    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안녕! 챗팟!");
        builder.setMessage("이제 챗팟을 연결하고, 메뉴에서 갱신하세요!");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        Intent intent_socketservice = null;
        intent_socketservice = new Intent(this, socketService.class);
        bindService(intent_socketservice, conn, Context.BIND_AUTO_CREATE);

        setname = (EditText) findViewById(R.id.setname);
        setspecies = (EditText) findViewById(R.id.setspecies);
        setnum = (EditText) findViewById(R.id.setnum);
        showdate = (TextView) findViewById(R.id.showdate);
        setdate = (RadioGroup) findViewById(R.id.setdate);

        today = (RadioButton) findViewById(R.id.today);

        pickdate = (RadioButton) findViewById(R.id.pickdate);
        add = (Button) findViewById(R.id.add);

        Intent add =getIntent();
        loginID = add.getStringExtra("loginID");

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
    }

    public void onClick(View v) throws JSONException, InterruptedException {
        String num = setnum.getText().toString();
        String name = setname.getText().toString();
        String species = setspecies.getText().toString();

        switch (v.getId()) {
            case R.id.today:
                today.setText(" 오늘" + "     " + getTime());
                index = 0;
                break;
            case R.id.pickdate:
                new DatePickerDialog(this, mDateSetListener,mYear, mMonth, mDay).show();
                index=1;
                break;
            case R.id.add:
                setnum.setText("");
                setname.setText("");
                setspecies.setText("");

                if(index == 0) {
                    String addcmd1 = String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {\"PotName\" : \"%s\", \"GrowDate\" : \"%s\", \"Species\" : \"%s\"}}",
                            loginID, "pot_regi", num, name, getTime() ,species);
                    socketservice.sendQue.offer(addcmd1);
                    Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                } else if (index == 1) {
                    String addcmd2 = String.format("{\"header\" : {\"self\" : \"%s\", \"command\" : \"%s\", \"option\" : \"%s\"}, \"data\" : {\"PotName\" : \"%s\", \"GrowDate\" : \"%s\", \"Species\" : \"%s\"}}",
                            loginID, "pot_regi", num, name, mYear + "-" + mMonth + "-" + mDay, species);
                    socketservice.sendQue.offer(addcmd2);
                    Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                }
                show();
                break;
        }
    }
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {

                    // TODO Auto-generated method stub

                    //사용자가 입력한 값을 가져온뒤

                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    showdate.setText("       " + mYear + "-" + mMonth + "-" + mDay);


                }
            };
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
