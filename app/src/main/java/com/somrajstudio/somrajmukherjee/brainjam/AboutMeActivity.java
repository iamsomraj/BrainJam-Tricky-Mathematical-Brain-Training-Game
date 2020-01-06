package com.somrajstudio.somrajmukherjee.brainjam;

import android.content.Intent;

import android.net.Uri;
import android.os.Build;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class AboutMeActivity extends AppCompatActivity {

    boolean feedbackIsPressed;

    public void feedbackFunction(View view)
    {
        feedbackIsPressed = true;
        String deviceInfo="Read me First:\n\n Device Info:";
        deviceInfo += "\n OS Version: " + System.getProperty("os.version") +"\n Build Version: "+ "( " + android.os.Build.VERSION.INCREMENTAL + " )";
        deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        deviceInfo += "\n Device: " + android.os.Build.DEVICE;
        deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        String body = deviceInfo + "\n\n----------------\n\n";
        body += "\n\nPlease enter your detailed feedback below. We will look into it as soon as possible.\n\n----------------\n\nFeedback:\n\n";
        String subject = "FEEDBACK ON BRAINJAM!";
        String[] email = {"iamsomraj@gmail.com"};

        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(intent, "Choose an Email Application: "));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void supportFunction(View view)
    {
        feedbackIsPressed = true;
        try
        {
            Uri uri = Uri.parse("https://www.paypal.me/iamsomraj/");
            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backFunction(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        feedbackIsPressed = false;


        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
    }

    public void imageClick(View view)
    {
        feedbackIsPressed = true;
        try
        {
            Uri uri = Uri.parse("https://www.linkedin.com/in/iamsomraj/ ");
            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(!feedbackIsPressed) {
            super.onBackPressed();
            startActivity(new Intent(AboutMeActivity.this, MainActivity.class));
            finish();
        }
        else {
            feedbackIsPressed = false;
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        if(!feedbackIsPressed) {
            super.onPause();
            startActivity(new Intent(AboutMeActivity.this, MainActivity.class));
            finish();
        }
        else {
            feedbackIsPressed = false;
            super.onPause();

        }
    }
}
