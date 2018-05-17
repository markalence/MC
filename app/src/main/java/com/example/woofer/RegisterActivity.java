package com.example.woofer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

                if(username.isEmpty() || name.isEmpty() || surname.isEmpty() || pass1.isEmpty() || pass2.isEmpty()){Toast.makeText(getBaseContext(),"Please fill in all fields.", Toast.LENGTH_SHORT).show();}

                else {

                    if (usernameCondition(username)) {

                        if (pass1.equals(pass2)) {

                            params.put("name", name);
                            params.put("surname", surname);
                            params.put("username", username);
                            params.put("password", pass1);

                            @SuppressLint("StaticFieldLeak") RegisterRequest registerRequest = new RegisterRequest(
                                    "http://lamp.ms.wits.ac.za/~s1676701/register.php", params) {
                                @Override
                                protected void onPostExecute(String output) {

                                    if (output.equals("Registration Successful!")) {


                                        Toast.makeText(getBaseContext(), output, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, AppActivity.class);
                                        MainActivity.sUsername = username;


                                        startActivity(intent);

                                    } else {

                                        Toast.makeText(getBaseContext(), output, Toast.LENGTH_SHORT).show();

                                    }

                                }


                            };


                            registerRequest.execute();
                            RegisterRequest addPostTable = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addposttable.php", params) {
                            };

                            RegisterRequest addFriendTable = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addfriendtable.php", params) {
                            };
                            addPostTable.execute();
                            addFriendTable.execute();


                        } else {
                            Toast.makeText(getBaseContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else{Toast.makeText(getBaseContext(),"Username is restricted to lowercase letters, uppercase letters, and numbers.",Toast.LENGTH_SHORT).show();}// fields empty?

                }

            } //onClick
        }); //onClickListener




    }


    public static boolean usernameCondition(String username){


        boolean condition=true;

        for(int i = 0; i<username.length();++i){

            int c= (int)username.charAt(i);

            if((48<=c && 57>=c) || (65<=c && 90>=c) || (97<=c && c<=122)) {



            }

            else{
                System.out.println(c );return false;}

        }

        return  condition;

    }
}
