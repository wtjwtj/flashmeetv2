package com.fm.johan.flashmeet;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class login_screen extends AppCompatActivity{

    Button bnewuser, bmap;
    TextView tuser, tpassword;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //Parse.enableLocalDatastore(this);

        //Parse.initialize(this);

        tuser = (TextView)findViewById(R.id.editText2);
        tpassword = (TextView)findViewById(R.id.editText);

        bnewuser = (Button) findViewById(R.id.button5);
        bnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               i = new Intent(getApplicationContext(),newuser_screen.class);
               startActivity(i);
            }
        });
        bmap = (Button) findViewById(R.id.button4);
        bmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user,password;

                user= tuser.getText().toString();
                password= tpassword.getText().toString();

                user= user.trim();
                password=password.trim();

                if(user.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Username or password is empty", Toast.LENGTH_LONG).show();

                }
                else
                {

                    ParseUser.logInInBackground(user, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(e==null)
                            {

                                i = new Intent(getApplicationContext(),MapsActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);  //successful

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(login_screen.this);
                                builder.setMessage("Sorry, you can't sign up. Error code: " + e.getMessage())
                                        .setTitle("Login failed")
                                        .setPositiveButton(android.R.string.ok, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }

                    });

                }

            }
        });



    }

}
