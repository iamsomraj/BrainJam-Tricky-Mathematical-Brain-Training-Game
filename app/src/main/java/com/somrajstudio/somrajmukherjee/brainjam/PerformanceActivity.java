package com.somrajstudio.somrajmukherjee.brainjam;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PerformanceActivity extends AppCompatActivity {

    TextView performanceQuotientTextView;
    TextView performanceUserNameTextView;
    TextView speedTextView;

    String username;
    int success;
    int failure;
    int allTimeScore;
    int total;
    int timer;

    public void backFunction(View view)
    {

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        Intent intent = getIntent();


        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        username = intent.getStringExtra("username");
        success = intent.getIntExtra("success",0);
        failure = intent.getIntExtra("failure",0);
        allTimeScore = intent.getIntExtra("allTimeScore",0);
        total = intent.getIntExtra("totalAttempt",0);
        timer = intent.getIntExtra("timerValue",0);


        String pqString, speedString;
        float pq;
        float speed;
        if(total == 0)
        {
            speed = (float) 0;

        }
        else {
            float avg = (float) allTimeScore / total;
            speed = (float) avg /(timer);

        }

        if(success == 0 && failure == 0)
        {
            pq = (float) 0;
        }
        else
        {
            pq = (float) success / (success + failure);

        }



        pqString = String.format("%1.2f",pq);
        speedString = String.format("%.2f",speed);





        performanceQuotientTextView = (TextView) findViewById(R.id.performanceQuotientTextView);
        performanceUserNameTextView = (TextView) findViewById(R.id.performanceUserNameTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);

        performanceQuotientTextView.setText(pqString);
        performanceUserNameTextView.setText(username.toUpperCase());
        speedTextView.setText(speedString);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PerformanceActivity.this, MainActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        startActivity(new Intent(PerformanceActivity.this, MainActivity.class));
    }
}
