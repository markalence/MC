package com.example.woofer;

import android.content.ContentValues;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    Button b;
    String username;
    String name;
    String surname;
    String pass1;
    String pass2;
    ContentValues params = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText et1 = (EditText) (findViewById(R.id.email));
                username = et1.getText().toString();

                EditText et2 = (EditText) (findViewById(R.id.fname));
                name = et2.getText().toString();

                EditText et3 = (EditText) (findViewById(R.id.lname));
                surname = et3.getText().toString();

                EditText et4 = (EditText) (findViewById(R.id.pass1));
                pass1 = et4.getText().toString();

                EditText et5 = (EditText) (findViewById(R.id.pass2));
                pass2 = et5.getText().toString();

                params.put("name", name);
                params.put("surname", surname);
                params.put("username", username);
                params.put("password", pass1);

                RegisterRequest registerRequest = new RegisterRequest(
                        "http://lamp.ms.wits.ac.za/~s1676701/register.php", params) {

                };
                RegisterRequest addTable = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addposttable.php", params) {
                };
                registerRequest.execute();
                addTable.execute();


            }
        });


    }
}
