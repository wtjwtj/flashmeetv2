package com.fm.johan.flashmeet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.Parse;

public class start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
        Intent intent = new Intent(start.this, login_screen.class);
        startActivity(intent);
        finish();
    }
}
