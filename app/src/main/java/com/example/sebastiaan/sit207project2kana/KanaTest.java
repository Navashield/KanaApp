package com.example.sebastiaan.sit207project2kana;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// This is the main testing grounds, this part of the app has user information
// Loaded in, and kana chosen at random for the user to select.
public class KanaTest extends AppCompatActivity {

    static int TIMEOUT = 2000;
    android.os.Handler handler = new Handler();
    Random randKana = new Random();
    UserDB myKanaDB = new UserDB(this, null, null, 1);

    List<String> romaList = new ArrayList<String>();
    List<String> hiraList = new ArrayList<String>();
    List<Integer> kanaOptions = new ArrayList<Integer>();

    Integer streakInt;
    String answerStr = "";
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Kana Test");

        final Button logout = (Button) findViewById(R.id.logOutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logOutIntent = new Intent(KanaTest.this, MainActivity.class);
                KanaTest.this.startActivity(logOutIntent);
            }
        });

        try {
            // Import the hiragana from our resource file
            importHiragana();
        } catch (Exception x) {
            // If we can't import the kana data, we throw an exception and quit
            Toast.makeText(getApplicationContext(), x.toString(), Toast.LENGTH_LONG).show();
        }

        // In the SignIn class we sent over user information, here we check if
        // it contains a user, if it does then we add that user
        String loadUser = getIntent().getExtras().getString("userInfo");
        if (!loadUser.equals("")) {
            userName = loadUser;
        }

        streakInt = 0;
        // Here we check if we're in guest mode, or if we have a user
        if (!userName.equals("")) {
            try {
                // If we have a user we load their score in
                streakInt = myKanaDB.getScore(userName);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        try {
            // Lets get new cards
            newFlashCards();
        } catch (Exception x) {
            Toast.makeText(getApplicationContext(), x.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void importHiragana() {
        try {
            // Here we import the Hiragana from its txt file.
            BufferedReader hiraRead = new BufferedReader(new InputStreamReader(
                    getAssets().open("HIRAROMAJI.txt")));
            String curLine = "";
            while ((curLine = hiraRead.readLine()) != null) {
                String section[] = curLine.split(":");
                // The value to the left of the : is the hiragana, which gets
                // added to the hiraList
                hiraList.add(section[0]);
                // The romaji is to the right of the : and is added to romaList
                romaList.add(section[1]);
            }
            hiraRead.close();
        } catch (IOException x) {
            Toast.makeText(getApplicationContext(), x.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkAnswers(final TextView myOption, int myIndex) {
        // If the texts value is the same as our randomly selected answer
        if (romaList.get(kanaOptions.get(myIndex)).equals(answerStr)) {
            // Make the button background green if its correct
            myOption.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            // Make a toast saying it's correct
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            // Increase the streak
            streakInt += 1;
        } else {
            // Make the button background red if incorrect
            myOption.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            // If incorrect make a toast saying so
            Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
            // Reset the streak to 0
            streakInt = 0;
        }
        if (!userName.equals("")) {
            myKanaDB.updateScore(userName, streakInt);
        }

        // You don't learn if you don't have the time to realise you got the
        // question wrong, so here's a timer to give people 2 seconds to realise
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // We reset the background colour
                myOption.getBackground().clearColorFilter();
                newFlashCards();
            }
        }, TIMEOUT);
    }

    private void newFlashCards() {
        // Here we declare the elements
        final TextView option1 = (TextView) findViewById(R.id.option1);
        final TextView option2 = (TextView) findViewById(R.id.option2);
        final TextView option3 = (TextView) findViewById(R.id.option3);
        final TextView option4 = (TextView) findViewById(R.id.option4);
        final TextView header = (TextView) findViewById(R.id.header);
        final TextView streaks = (TextView) findViewById(R.id.streaks);




        // We clear the options, as we don't want each new screen to be identical
        kanaOptions.clear();
        // Here we fill the list of potential kana options
        for (int i = 0; i < 4; i++) {
            int temp = randKana.nextInt(hiraList.size());
            // Here we check for duplicates, we don't want all the options to be
            // the same
            if (!kanaOptions.contains(temp)) {
                kanaOptions.add(temp);
            } else {
                i--;
            }
        }

        // From the kana options list, we pick at random what the correct answer is
        int answerInt = randKana.nextInt(kanaOptions.size());
        answerStr = romaList.get(kanaOptions.get(answerInt));

        // Now we set the text for all the options
        option1.setText(hiraList.get(kanaOptions.get(0)));
        option2.setText(hiraList.get(kanaOptions.get(1)));
        option3.setText(hiraList.get(kanaOptions.get(2)));
        option4.setText(hiraList.get(kanaOptions.get(3)));

        // And the text for the headings
        header.setText(String.format("Select the Kana for %s", answerStr));
        streaks.setText(String.format("%d Kana Streak!", streakInt));

        // Here's what happens when the the button is pressed
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // We call the check answers method
                checkAnswers(option1, 0);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswers(option2, 1);
            }

        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswers(option3, 2);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswers(option4, 3);
            }
        });
    }
}
