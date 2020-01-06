package com.somrajstudio.somrajmukherjee.brainjam;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProgressActivity extends AppCompatActivity {

    ArrayList<String> progress;
    ArrayAdapter<String> progressAdapter;

    ListView progressListView;

    TextView progressUserNameTextView;

    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);


        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));

        Intent intent = getIntent();


        username = intent.getStringExtra("username");
        progress = intent.getStringArrayListExtra("progress");



        progressListView = (ListView) findViewById(R.id.progressListView);
        progressUserNameTextView = (TextView) findViewById(R.id.progressUserNameTextView);




        for (int i = 0; i < progress.size(); i++) {
            String score = progress.get(i);
            score = String.valueOf(i + 1) + "." + score;
            progress.set(i, score);
        }

        if(progress.size() == 0)
        {
            ArrayList<String> first = new ArrayList<String>();
            first.add("\n\tYou have not made any progress yet.\n");
            first.add("\n\tFinish all the 5 levels first.\n");
            first.add("\n\tThen come here again!\n");
            progressAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, first);

        }
        else {
            progressAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, progress);
        }
        progressListView.setAdapter(progressAdapter);
        progressUserNameTextView.setText(username.toUpperCase());

    }


    public void backFunction(View view) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProgressActivity.this, MainActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        startActivity(new Intent(ProgressActivity.this, MainActivity.class));
    }


}