package com.somrajstudio.somrajmukherjee.brainjam;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity
{


    //Profile Username textview

    TextView profieUsernameTextView;
    TextView profileRatioTextView;
    TextView totalScoreTextView;

    //variables

    String username;

    int success;
    int failure;
    int allTimeScore;




    public void backFunction(View view)
        {

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        success = intent.getIntExtra("success",0);
        failure = intent.getIntExtra("failure",0);
        allTimeScore = intent.getIntExtra("allTimeScore",0);


        profieUsernameTextView = (TextView) findViewById(R.id.performanceUserNameTextView);
        profileRatioTextView = (TextView) findViewById(R.id.profileRatioTextView);
        totalScoreTextView = (TextView) findViewById(R.id.totalScoreTextView);

        String score = String.valueOf(success)+"/"+String.valueOf(failure);

        profieUsernameTextView.setText(username.toUpperCase());
        profileRatioTextView.setText(score);
        totalScoreTextView.setText(String.valueOf(allTimeScore));



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}
