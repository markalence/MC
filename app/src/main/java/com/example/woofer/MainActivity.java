package com.example.woofer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button registerButton;
    Button loginButton;
    String success = "false";
    public static String sUsername = "";
    Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ContentValues params = new ContentValues();
        c = this;


        registerButton = (Button) findViewById(R.id.button3);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }

        });

        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);

                sUsername = username.getText().toString().toLowerCase();
                String sPassword = password.getText().toString();

                params.put("username", sUsername.toLowerCase());
                params.put("password", sPassword);


                @SuppressLint("StaticFieldLeak") RegisterRequest registerRequest = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/login.php", params) {
                    @Override
                    protected void onPostExecute(String output) {

                        try {
                            JSONObject j = new JSONObject(output);
                            success = j.getString("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };

                registerRequest.execute();
                if (success.equals("true")) {
                    openApp();

                }


            }
        });


    }

    public void openApp() {

        Intent intent = new Intent(MainActivity.this, AppActivity.class);
        intent.putExtra("username", sUsername);
        startActivity(intent);
        finish();

    }

    public void openRegister() {

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        intent.putExtra("username", sUsername);
        startActivity(intent);
        finish();

    }


}
