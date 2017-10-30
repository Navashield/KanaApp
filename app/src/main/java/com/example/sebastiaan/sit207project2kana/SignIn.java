package com.example.sebastiaan.sit207project2kana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// This part of the app handles signing in, it communicates with the database
// class to load information
public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        getSupportActionBar().setTitle("Sign In");

        //We want to be able to decrypt passwords
        final PasswordEncrypt passwordDe = new PasswordEncrypt();
        // And access the database
        final UserDB myKanaDB = new UserDB(this, null, null, 1);

        // Declare all the elements
        final EditText userNameEdit = (EditText) findViewById(R.id.userName);
        final EditText passwordEdit = (EditText) findViewById(R.id.passWord);
        final Button loginBtn = (Button) findViewById(R.id.loginButton);
        final Button guestBtn = (Button) findViewById(R.id.guestButton);
        final TextView signUpActivity = (TextView) findViewById(R.id.createAccount);


        // Select the guest button
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If we select the guest option, we go straight to the main app
                Intent kanaTestIntent = new Intent(SignIn.this, KanaTest.class);
                kanaTestIntent.putExtra("userInfo", "");
                SignIn.this.startActivity(kanaTestIntent);
            }
        });

        // Select the signup option
        signUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we go to the SignUp where we can make an account
                Intent signUpIntent = new Intent(SignIn.this, SignUp.class);
                SignIn.this.startActivity(signUpIntent);
            }
        });

        // Select login option
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If either field is empty we tell the user something must be wrong
                if (userNameEdit.getText().toString().trim().isEmpty() ||
                        passwordEdit.getText().toString().trim().isEmpty()) {
                    // Let the user know
                    Toast.makeText(getApplicationContext(), "Please Complete All Fields",
                            Toast.LENGTH_LONG).show();
                }

                // Let's test the fields
                else {
                    // We get the string of both the user and password field
                    String userNameEditCont = userNameEdit.getText().toString();
                    String passwordNameEditcont = passwordEdit.getText().toString();

                    // If we find a match in the database
                    if (myKanaDB.userExists(userNameEditCont)) {
                        try {
                            // We compare a decrypted version of the password to the EditText
                            String tempPass = passwordDe.decrypt(myKanaDB.getPassword(userNameEditCont), userNameEditCont);
                            // If they're the same
                            if (tempPass.equals(passwordNameEditcont)) {
                                // We prepare to launch a KanaTest intent
                                Intent kanaTestIntent = new Intent(SignIn.this, KanaTest.class);
                                // We also want to send the KanaTest the
                                // username, so we can update the streaks in KanaTest
                                kanaTestIntent.putExtra("userInfo", userNameEditCont);
                                SignIn.this.startActivity(kanaTestIntent);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
