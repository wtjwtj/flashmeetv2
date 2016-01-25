package com.fm.johan.flashmeet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class newuser_screen extends AppCompatActivity {

    Button bregister;
    EditText tMail,tName, tPass,tPass2;
    Intent i;
    String name,email,pass,pass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuser_screen);
        tName   = (EditText) findViewById(R.id.editText3);
        tMail   = (EditText) findViewById(R.id.editText4);
        tPass   = (EditText) findViewById(R.id.editText5);
        tPass2  = (EditText) findViewById(R.id.editText6);

        bregister = (Button) findViewById(R.id.button6);

        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name    = tName.getText().toString();
                email   = tMail.getText().toString();
                pass    = tPass.getText().toString();
                pass2   = tPass2.getText().toString();
                name    = name.trim();
                email   = email.trim();
                pass    = pass.trim();
                pass2   = pass2.trim();

                if(!pass.equals(pass2)){
                    Toast.makeText(newuser_screen.this,"Passwords are not equal",Toast.LENGTH_LONG).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.logOut();

                if(name.isEmpty() || pass.isEmpty() || email.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(newuser_screen.this);
                    builder.setMessage("Username, Email and Password should not be empty")
                            .setTitle("Wrong Input")
                            .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(name);
                    newUser.setPassword(pass);
                    newUser.setEmail(email);

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //successful registration takes to login screen

                                i = new Intent(getApplicationContext(),login_screen.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(newuser_screen.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle("ERROR")
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
