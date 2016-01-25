package com.fm.johan.flashmeet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.Calendar;
import java.util.Date;

public class newflash_screen extends AppCompatActivity implements View.OnClickListener {

    Button btime, bdate;
    EditText eName,eLat,eLong,eDescription;
    TextView tDate, tTime;
    Intent i;
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    Calendar calendar = Calendar.getInstance();
    Calendar timecalendar = Calendar.getInstance();
    Date mydate;
    String name, des;

    int myhour,myminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newflash_screen);

        eName = (EditText) findViewById(R.id.editText7);
        eLat = (EditText) findViewById(R.id.editText8);
        eLong= (EditText) findViewById(R.id.editText9);
        eDescription = (EditText) findViewById(R.id.editText10);

        eLat.setText(Double.toString(getIntent().getDoubleExtra("lat",0)));
        eLong.setText(Double.toString(getIntent().getDoubleExtra("long",0)));

        tDate = (TextView) findViewById(R.id.textView);
        tTime = (TextView) findViewById(R.id.textView2);

        bdate = (Button) findViewById(R.id.button);
        btime = (Button) findViewById(R.id.button2);

        btime.setOnClickListener(this);
        bdate.setOnClickListener(this);


        tDate.setText(" " + DateUtils.formatDateTime(newflash_screen.this, timecalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE));
        tTime.setText(" " + DateUtils.formatDateTime(newflash_screen.this,timecalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_TIME));



    }

    private void clear(){
        eName.setText("");
        eLat.setText("");
        eLong.setText("");
        eDescription.setText("");
    }

    @Override
    public void onClick(View v) {



        switch(v.getId()){
            case R.id.button:
            {
                dpd = new DatePickerDialog(newflash_screen.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        timecalendar.set(Calendar.YEAR,year);
                        timecalendar.set(Calendar.MONTH, monthOfYear);
                        timecalendar.set(Calendar.DATE,dayOfMonth);

                        tDate.setText(DateUtils.formatDateTime(newflash_screen.this,timecalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));

                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DATE));
                dpd.show();
                break;
            }
            case R.id.button2:{

                tpd= new TimePickerDialog(newflash_screen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timecalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        timecalendar.set(Calendar.MINUTE,minute);

                        tTime.setText(DateUtils.formatDateTime(newflash_screen.this,timecalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_TIME));
                    }
                }
                        ,calendar.get(Calendar.HOUR_OF_DAY)// aktuelle Stunde
                        ,calendar.get(Calendar.MINUTE)// aktuelle Minute
                        ,true);// ob 12 oder 24 anhand der einstellungen im Phone (android.text.format.DateFormat.is24HourFormat(newflash_screen.this))
                tpd.show();
                break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_save:
                if(-1 != myCheck.state(getApplicationContext()) ) {

                    name = eName.getText().toString();
                    des = eDescription.getText().toString();
                    if( !name.isEmpty() && !des.isEmpty())
                    {

                        mydate = timecalendar.getTime();
                        ParseObject myput = new ParseObject("Events");
                        myput.put("name", name);
                        myput.put("date", mydate);
                        myput.put("lat", Double.parseDouble(eLat.getText().toString()));
                        myput.put("long", Double.parseDouble(eLong.getText().toString()));
                        myput.put("description", des);
                        myput.saveInBackground();
                        Toast.makeText(getApplicationContext(), "Your event has been created", Toast.LENGTH_LONG).show();
                        clear();

                        Intent i = new Intent(newflash_screen.this,MapsActivity.class);
                        finish();
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Name or description shouldn't be empty", Toast.LENGTH_LONG).show();
                    }
                }
                else Toast.makeText(getApplicationContext(),"You can't create an event. You are offline", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
