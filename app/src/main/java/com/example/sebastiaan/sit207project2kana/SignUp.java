package com.example.sebastiaan.sit207project2kana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// This is signup, for the most part it writes to the database, but also reads
// from it to avoide duplicate users
public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        getSupportActionBar().setTitle("Sign Up");

        // Time to declare finals
        // To use the passwordEncrypt Library
        final PasswordEncrypt passwordEn = new PasswordEncrypt();
        final UserDB myUserDB = new UserDB(this, null, null, 1);
        final Button signUpBtn = (Button) findViewById(R.id.signUpButton);
        final EditText userNameEdit = (EditText) findViewById(R.id.userName);
        final EditText passwordEdit = (EditText) findViewById(R.id.passWord);
        final Button guestBtn = (Button) findViewById(R.id.guestButton);
        final TextView loginActivity = (TextView) findViewById(R.id.loginActivity);

        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If we select the guest option, we go straight to the main app
                Intent kanaTestIntent = new Intent(SignUp.this, KanaTest.class);
                kanaTestIntent.putExtra("userInfo", "");
                SignUp.this.startActivity(kanaTestIntent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In the case that the user forgets to fill out either the user
                // or the password field, we send them a toast
                if (userNameEdit.getText().toString().trim().isEmpty() ||
                        passwordEdit.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Complete All Fields",
                            Toast.LENGTH_LONG).show();
                } else {
                    // There are quite a few try catches, so I'll explain why here
                    // There is a chance that they'll fail, for reasons such as
                    // a file not being found, this lets them fail in a safe way
                    // or in this case, because the security API runs into a problem
                    try {
                        String userName = userNameEdit.getText().toString();
                        String password = passwordEn.encrypt(passwordEdit.getText().toString(), userName);
                        // We don't want duplicate users, so we need to check the
                        // database to see if we're doubling up
                        if (myUserDB.userExists(userName)) {
                            Toast.makeText(getApplicationContext(), "User Already Exists",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Add the user information to the database
                            myUserDB.addToTable(userName, password);
                            // Let the user know we have made an account
                            Toast.makeText(getApplicationContext(), "Successfully Made Account",
                                    Toast.LENGTH_LONG).show();
                            // After making an account, we go to the login screen again
                            Intent signInIntent = new Intent(SignUp.this, SignIn.class);
                            SignUp.this.startActivity(signInIntent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        // Here we go to the login Acitivty
        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(SignUp.this, SignIn.class);
                SignUp.this.startActivity(signInIntent);
            }
        });
    }
}
