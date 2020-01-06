package com.somrajstudio.somrajmukherjee.brainjam;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class MainActivity extends AppCompatActivity {

    //Intent
    Intent intentProfile;
    Intent intentProgress;
    Intent intentPerformance;
    Intent intentAboutMe;

    //SharedPreferences
    SharedPreferences sharedPreferences;


    //Constraint Layout
    ConstraintLayout levelLayout;
    ConstraintLayout tutorialLayout;
    ConstraintLayout gameLayout;
    ConstraintLayout finalGameLayout;

    android.support.v7.widget.GridLayout gridLevelLayout;
    KonfettiView viewKonfetti;

    //Buttons
    Button goButton ;
    Button enterButton;
    Button backEditUserName;
    Button gameModeButton;

    boolean listIsOn ;


    Button helpButton;
    Button howToPlayButton;

    Button backLevelLayout;

    Button levelOneButton;
    Button levelTwoButton;
    Button levelThreeButton;
    Button levelFourButton;
    Button levelFiveButton;


    Button button0;
    Button button1;
    Button button2;
    Button button3;


    Button finalButton;
    Button nextLevelButton;


    //EditText
    EditText userNameEditText;

    //ListView
    ListView helpListView;



    //TextViews
    TextView timerTextView ;
    TextView sumTextView;
    TextView scoreTextView;
    TextView finalScoreTextView;
    TextView congratsTextView;
    TextView welcomeBackTextView;
    TextView tutorialTextView;

    //BooleanVariable

    boolean isTutorialActive;
    boolean isLevelOneUnlocked;
    boolean isLevelTwoUnlocked;
    boolean isLevelThreeUnlocked;
    boolean isLevelFourUnlocked;
    boolean isLevelFiveUnlocked;

    boolean isTimerOn;


    boolean levelOneIsActive;
    boolean levelTwoIsActive;
    boolean levelThreeIsActive;
    boolean levelFourIsActive;
    boolean levelFiveIsActive;

    boolean gamePaused = false;


    //Permanent Storage Variables

    String username = "";


    //Variables
    int gameMode;

    int score;
    int noOfQuestion;
    int correctAnswerPosition;

    int answerPositionClicked;

    int scoreLevelOne;
    int scoreLevelTwo;
    int scoreLevelThree;
    int scoreLevelFour;
    int scoreLevelFive;


    int noOfQuestionLevelOne;
    int noOfQuestionLevelTwo;
    int noOfQuestionLevelThree;
    int noOfQuestionLevelFour;
    int noOfQuestionLevelFive;



    int levelThreshold;
    long timerValue = 30100;

    int success;
    int failure;


    ArrayList<String> helpList;
    ArrayAdapter<String> helpListsAdapter;

    ArrayList<Integer> answer;
    ArrayList<String> operator;

    ArrayList<String> progress;

    //Random generator
    Random rand = new Random();


    //Game Sound Initialisation

    MediaPlayer mediaPlayer = null;

    Toolbar toolbar;


    /// First Thing - Setting up the game

    @Override
    protected void onCreate(Bundle savedInstanceState){

        //Game Mode : 10 PT


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));

        gameModeButton = (Button) findViewById(R.id.gameModeButton);
        tutorialTextView = (TextView) findViewById(R.id.showTutorialTextView);
        howToPlayButton = (Button) findViewById(R.id.howToPlayButton);


        sharedPreferences = this.getSharedPreferences("com.somrajstudio.somrajmukherjee.brainjam", Context.MODE_PRIVATE);

        String gameModeString;
        gameMode = sharedPreferences.getInt("gameMode",2);
        sharedPreferences.edit().putInt("gameMode",gameMode).commit();
        levelThreshold = gameMode * 10;
        if(gameMode == 1)
        {
            gameModeString = "Game Mode : EASY";
            gameModeButton.setBackgroundResource(R.drawable.easy);
            howToPlayButton.setBackgroundResource(R.drawable.easy);



        }else if(gameMode == 2)
        {
            gameModeString = "Game Mode : MEDIUM";
            gameModeButton.setBackgroundResource(R.drawable.medium);
            howToPlayButton.setBackgroundResource(R.drawable.medium);


        }
        else if(gameMode == 3)
        {
            gameModeString = "Game Mode : HARD";
            gameModeButton.setBackgroundResource(R.drawable.hard);
            howToPlayButton.setBackgroundResource(R.drawable.hard);


        }
        else{
            gameMode = 2;
            levelThreshold = gameMode * 10;
            gameModeString = "Game Mode : MEDIUM";
            gameModeButton.setBackgroundResource(R.drawable.medium);
            howToPlayButton.setBackgroundResource(R.drawable.medium);



        }

        gameModeButton.setText(gameModeString);


        String tutorialString = "5 levels!\n\n"+String.valueOf((int)timerValue/1000)+" seconds time in each level!\n\nSolve as many sums as you can! "+"\n\nScore "+String.valueOf(levelThreshold)+" to go to next level.";
        tutorialTextView.setText(tutorialString);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewKonfetti = (KonfettiView) findViewById(R.id.viewKonfetti);

        levelLayout = (ConstraintLayout) findViewById(R.id.levelLayout);
        tutorialLayout = (ConstraintLayout) findViewById(R.id.tutorialLayout);
        gameLayout = (ConstraintLayout) findViewById(R.id.gameLayout);
        finalGameLayout = (ConstraintLayout) findViewById(R.id.finalGameLayout);

        gridLevelLayout = (android.support.v7.widget.GridLayout) findViewById(R.id.gridLevelLayout);

        helpButton = (Button) findViewById(R.id.helpButton);
        helpListView = (ListView) findViewById(R.id.helpListView);

        goButton = (Button) findViewById(R.id.goButton);
        enterButton = (Button) findViewById(R.id.enterButton);
        backEditUserName = (Button) findViewById(R.id.backEditUserName);


        levelOneButton = (Button) findViewById(R.id.levelOneButton);
        levelTwoButton = (Button) findViewById(R.id.levelTwoButton);
        levelThreeButton = (Button) findViewById(R.id.levelThreeButton);
        levelFourButton = (Button) findViewById(R.id.levelFourButton);
        levelFiveButton = (Button) findViewById(R.id.levelFiveButton);

        howToPlayButton = (Button) findViewById(R.id.howToPlayButton);

        backLevelLayout = (Button) findViewById(R.id.backLevelLayout);


        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        finalButton = (Button) findViewById(R.id.finalButton);
        nextLevelButton = (Button) findViewById(R.id.nextLevelButton);

        userNameEditText = (EditText) findViewById(R.id.userNameEditText);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        sumTextView = (TextView) findViewById(R.id.sumTextView);
        finalScoreTextView = (TextView) findViewById(R.id.finalScoreTextView);
        congratsTextView = (TextView) findViewById(R.id.congratsTextView);
        welcomeBackTextView = (TextView) findViewById(R.id.welcomeBackTextView);

        try {
            initialiseTheWholeGame();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Second Thing - Initialising the elements for the first time and Go Button is ready to be pressed


    public void initialiseTheWholeGame() throws IOException {
        goButton.setVisibility(View.INVISIBLE);
        enterButton.setVisibility(View.INVISIBLE);
        gameModeButton.setVisibility(View.INVISIBLE);
        backEditUserName.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);

        helpListView.setVisibility(View.INVISIBLE);

        userNameEditText.setVisibility(View.INVISIBLE);
        welcomeBackTextView.setVisibility(View.INVISIBLE);

        gamePaused = false;
        isTutorialActive = false;
        isTimerOn = false;
        listIsOn = false;


        isLevelOneUnlocked = sharedPreferences.getBoolean("isLevelOneUnlocked",true);
        isLevelTwoUnlocked = sharedPreferences.getBoolean("isLevelTwoUnlocked",false);
        isLevelThreeUnlocked = sharedPreferences.getBoolean("isLevelThreeUnlocked",false);
        isLevelFourUnlocked = sharedPreferences.getBoolean("isLevelFourUnlocked",false);
        isLevelFiveUnlocked = sharedPreferences.getBoolean("isLevelFiveUnlocked",false);


        levelOneIsActive = false;
        levelTwoIsActive = false;
        levelThreeIsActive = false;
        levelFourIsActive = false;
        levelFiveIsActive = false;


        score = sharedPreferences.getInt("score",0);
        scoreLevelOne = sharedPreferences.getInt("scoreLevelOne",0);
        scoreLevelTwo = sharedPreferences.getInt("scoreLevelTwo",0);
        scoreLevelThree = sharedPreferences.getInt("scoreLevelThree",0);
        scoreLevelFour = sharedPreferences.getInt("scoreLevelFour",0);
        scoreLevelFive = sharedPreferences.getInt("scoreLevelFive",0);


        noOfQuestion = sharedPreferences.getInt("noOfQuestion",0);
        noOfQuestionLevelOne = sharedPreferences.getInt("noOfQuestionLevelOne",0);
        noOfQuestionLevelTwo = sharedPreferences.getInt("noOfQuestionLevelTwo",0);
        noOfQuestionLevelThree = sharedPreferences.getInt("noOfQuestionLevelThree",0);
        noOfQuestionLevelFour = sharedPreferences.getInt("noOfQuestionLevelFour",0);
        noOfQuestionLevelFive = sharedPreferences.getInt("noOfQuestionLevelFive",0);

        success = sharedPreferences.getInt("success",0);
        failure = sharedPreferences.getInt("failure",0);

        finalButton.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);


        congratsTextView.setVisibility(View.INVISIBLE);


        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");


        progress= new ArrayList<String>();

        try {
            progress = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("progress",ObjectSerializer.serialize(new ArrayList<String>())));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        username = "";

/*        welcomeBackTextView.setTypeface(typefaceBold);
        goButton.setTypeface(typefaceBold);*/

        timerTextView.setText("30 s");
        scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));


        username = sharedPreferences.getString("username","");
        userNameEditText.setText("");

        if (username.equals(""))
        {

            userNameEditText.setVisibility(View.VISIBLE);
            enterButton.setVisibility(View.VISIBLE);
            goButton.setVisibility(View.INVISIBLE);
            welcomeBackTextView.setVisibility(View.INVISIBLE);
            gameModeButton.setVisibility(View.INVISIBLE);
        }
        else
            {
            userNameEditText.setVisibility(View.INVISIBLE);
            enterButton.setVisibility(View.INVISIBLE);
            username = sharedPreferences.getString("username", "");
            goButton.setVisibility(View.VISIBLE);
            String banner = "Hey " + username + " !";
            welcomeBackTextView.setText(banner.toUpperCase());
            welcomeBackTextView.setVisibility(View.VISIBLE);
            gameModeButton.setVisibility(View.VISIBLE);
        }

    }

    public void initialiseLevelOneMode()
    {
        welcomeBackTextView.setVisibility(View.VISIBLE);
        goButton.setVisibility(View.VISIBLE);


        isTutorialActive = false;

        isLevelOneUnlocked = true;
        isLevelTwoUnlocked = false;
        isLevelThreeUnlocked = false;
        isLevelFourUnlocked = false;
        isLevelFiveUnlocked = false;


        levelOneIsActive = true;
        levelTwoIsActive = false;
        levelThreeIsActive = false;
        levelFourIsActive = false;
        levelFiveIsActive = false;


        score = 0;
        scoreLevelOne = 0;
        scoreLevelTwo = 0;
        scoreLevelThree = 0;
        scoreLevelFour = 0;
        scoreLevelFive = 0;


        noOfQuestion = 0;
        noOfQuestionLevelOne = 0;
        noOfQuestionLevelTwo = 0;
        noOfQuestionLevelThree = 0;
        noOfQuestionLevelFour = 0;
        noOfQuestionLevelFive = 0;


        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");


    }

    public void changeGameMode(View view)
    {
        String tutorialString;
        switch (gameMode)
        {
            case 1:
                initialiseLevelOneMode();
                gameMode = 2;
                levelThreshold = gameMode * 10;
                gameModeButton.setText("Game Mode : MEDIUM");
                gameModeButton.setBackgroundResource(R.drawable.medium);
                howToPlayButton.setBackgroundResource(R.drawable.medium);
                tutorialString = "5 levels!\n\n"+String.valueOf((int)timerValue/1000)+" seconds time in each level!\n\nSolve as many sums as you can! "+"\n\nScore "+String.valueOf(levelThreshold)+" to go to next level.";
                tutorialTextView.setText(tutorialString);
                sharedPreferences.edit().putInt("gameMode",gameMode).apply();

                break;
            case 2:
                initialiseLevelOneMode();

                gameModeButton.setBackgroundResource(R.drawable.hard);
                howToPlayButton.setBackgroundResource(R.drawable.hard);

                gameMode = 3;
                levelThreshold = gameMode * 10;
                gameModeButton.setText("Game Mode : HARD");
                tutorialString = "5 levels!\n\n"+String.valueOf((int)timerValue/1000)+" seconds time in each level!\n\nSolve as many sums as you can! "+"\n\nScore "+String.valueOf(levelThreshold)+" to go to next level.";
                tutorialTextView.setText(tutorialString);
                sharedPreferences.edit().putInt("gameMode",gameMode).apply();

                break;
            case 3:
                initialiseLevelOneMode();
                gameModeButton.setBackgroundResource(R.drawable.easy);
                howToPlayButton.setBackgroundResource(R.drawable.easy);


                gameMode = 1;
                levelThreshold = gameMode * 10;
                gameModeButton.setText("Game Mode : EASY");
                tutorialString = "5 levels!\n\n"+String.valueOf((int)timerValue/1000)+" seconds time in each level!\n\nSolve as many sums as you can! "+"\n\nScore "+String.valueOf(levelThreshold)+" to go to next level.";
                tutorialTextView.setText(tutorialString);
                sharedPreferences.edit().putInt("gameMode",gameMode).apply();

                break;
            default:
                initialiseLevelOneMode();
                gameMode = 2;
                levelThreshold = gameMode * 10;
                gameModeButton.setText("Game Mode : MEDIUM");
                gameModeButton.setBackgroundResource(R.drawable.medium);
                howToPlayButton.setBackgroundResource(R.drawable.medium);
                tutorialString = "5 levels!\n\n"+String.valueOf((int)timerValue/1000)+" seconds time in each level!\n\nSolve as many sums as you can! "+"\n\nScore "+String.valueOf(levelThreshold)+" to go to next level.";
                tutorialTextView.setText(tutorialString);
                sharedPreferences.edit().putInt("gameMode",gameMode).apply();
                break;
        }


    }




    public void hideKeyboard()
    {
        try {
/*
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
*/
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
/*            }
            else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return;
            }*/

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void backToWelcomeBack(View view)
    {
        levelLayout.setVisibility(View.INVISIBLE);
        userNameEditText.setVisibility(View.INVISIBLE);
        enterButton.setVisibility(View.INVISIBLE);
        gameModeButton.setVisibility(View.VISIBLE);
        welcomeBackTextView.setVisibility(View.VISIBLE);
        goButton.setVisibility(View.VISIBLE);
    }


    public void closeEditUserName(View view)
    {
        hideKeyboard();
        gameModeButton.setVisibility(View.VISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        userNameEditText.setVisibility(View.INVISIBLE);
        enterButton.setVisibility(View.INVISIBLE);
        if(username!=null)
        {
            welcomeBackTextView.setVisibility(View.VISIBLE);
            goButton.setVisibility(View.VISIBLE);
        }else
        {
            userNameEditText.setVisibility(View.VISIBLE);
            enterButton.setVisibility(View.VISIBLE);
        }
        backEditUserName.setVisibility(View.INVISIBLE);
    }

    public void enterFunction(View view)
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if(userNameEditText.getText().toString().equals("") || userNameEditText.getText().toString().length()<=5 || userNameEditText.getText().toString().length()>10)
        {
            Toast.makeText(this, "Please enter a valid username!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            backEditUserName.setVisibility(View.INVISIBLE);
            username=userNameEditText.getText().toString();
            sharedPreferences.edit().putString("username",username).commit();
            String banner = "Hey " + username + " !";
            gameModeButton.setVisibility(View.INVISIBLE);
            welcomeBackTextView.setText(banner.toUpperCase());
            userNameEditText.setVisibility(View.INVISIBLE);
            enterButton.setVisibility(View.INVISIBLE);
            levelLayout.setVisibility(View.VISIBLE);
            hideKeyboard();
        }
    }

    public void goFunction(View view)
    {

        gamePaused = false;
        welcomeBackTextView.setVisibility(View.INVISIBLE);
        goButton.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.VISIBLE);
        backLevelLayout.setVisibility(View.VISIBLE);
        gameModeButton.setVisibility(View.INVISIBLE);

    }



    // Third Thing - Tutorial and Selecting the levels and starting the levels

    public void showTutorial(View view)
    {
        if( isTutorialActive == false)
        {

            gridLevelLayout.setVisibility(View.INVISIBLE);
            howToPlayButton.setVisibility(View.INVISIBLE);
            tutorialLayout.setVisibility(View.VISIBLE);
            backLevelLayout.setVisibility(View.INVISIBLE);
            isTutorialActive = true;

        }
    }

    public void closeTutorial(View view)
    {
        listIsOn = false;
        howToPlayButton.setVisibility(View.VISIBLE);
        gridLevelLayout.setVisibility(View.VISIBLE);
        levelLayout.setVisibility(View.VISIBLE);
        tutorialLayout.setVisibility(View.INVISIBLE);
        backLevelLayout.setVisibility(View.VISIBLE);
        isTutorialActive = false;
    }


    public void startTheGame(View view) {
        gamePaused = false;
        String tag = view.getTag().toString();

        if (isLevelOneUnlocked && tag.equals("11"))
        {
            score = 0;
            scoreLevelOne = 0;
            scoreLevelTwo = 0;
            scoreLevelThree = 0;
            scoreLevelFour = 0;
            scoreLevelFive = 0;

            noOfQuestion = 0;
            noOfQuestionLevelOne = 0;
            noOfQuestionLevelTwo = 0;
            noOfQuestionLevelThree = 0;
            noOfQuestionLevelFour = 0;
            noOfQuestionLevelFive = 0;

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));


            levelOneIsActive = true;


            goButton.setVisibility(View.INVISIBLE);
            levelLayout.setVisibility(View.INVISIBLE);
            gameLayout.setVisibility(View.VISIBLE);
            generateLevelOneQuestion();
            timerFunction();

        }
/*        else
        {
            Toast.makeText(this, "Level is still not yet unlocked!", Toast.LENGTH_SHORT).show();
        }*/

        else if(isLevelTwoUnlocked && tag.equals("22"))
        {

            score = score - scoreLevelTwo - scoreLevelThree - scoreLevelFour - scoreLevelFive;
            scoreLevelTwo = 0;
            scoreLevelThree = 0;
            scoreLevelFour = 0;
            scoreLevelFive = 0;
            score=calculateTotal(scoreLevelOne,scoreLevelTwo,scoreLevelThree,scoreLevelFour,scoreLevelFive);


            noOfQuestion = noOfQuestion - noOfQuestionLevelTwo - noOfQuestionLevelThree - noOfQuestionLevelFour - noOfQuestionLevelFive;
            noOfQuestionLevelTwo = 0;
            noOfQuestionLevelThree = 0;
            noOfQuestionLevelFour = 0;
            noOfQuestionLevelFive = 0;

            noOfQuestion = calculateTotal(noOfQuestionLevelOne,noOfQuestionLevelTwo,noOfQuestionLevelThree,noOfQuestionLevelFour,noOfQuestionLevelFive);

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));


            levelTwoIsActive = true;
            goButton.setVisibility(View.INVISIBLE);
            levelLayout.setVisibility(View.INVISIBLE);
            gameLayout.setVisibility(View.VISIBLE);
            generateLevelTwoQuestion();
            timerFunction();

        }
/*
        else
        {
            Toast.makeText(this, "Level is still not yet unlocked!", Toast.LENGTH_SHORT).show();
        }
*/

        else if(isLevelThreeUnlocked && tag.equals("33"))
        {
            score = score - scoreLevelThree - scoreLevelFour - scoreLevelFive;
            scoreLevelThree = 0;
            scoreLevelFour = 0;
            scoreLevelFive = 0;
            score=calculateTotal(scoreLevelOne,scoreLevelTwo,scoreLevelThree,scoreLevelFour,scoreLevelFive);


            noOfQuestion = noOfQuestion - noOfQuestionLevelThree - noOfQuestionLevelFour - noOfQuestionLevelFive;
            noOfQuestionLevelThree = 0;
            noOfQuestionLevelFour = 0;
            noOfQuestionLevelFive = 0;
            noOfQuestion = calculateTotal(noOfQuestionLevelOne,noOfQuestionLevelTwo,noOfQuestionLevelThree,noOfQuestionLevelFour,noOfQuestionLevelFive);

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));



            levelThreeIsActive = true;
            goButton.setVisibility(View.INVISIBLE);
            levelLayout.setVisibility(View.INVISIBLE);
            gameLayout.setVisibility(View.VISIBLE);
            generateLevelThreeQuestion();
            timerFunction();


        }
        else if(isLevelFourUnlocked && tag.equals("44"))
        {
            score = score - scoreLevelFour - scoreLevelFive;
            scoreLevelFour = 0;
            scoreLevelFive = 0;
            score=calculateTotal(scoreLevelOne,scoreLevelTwo,scoreLevelThree,scoreLevelFour,scoreLevelFive);


            noOfQuestion = noOfQuestion - noOfQuestionLevelFour - noOfQuestionLevelFive;
            noOfQuestionLevelFour = 0;
            noOfQuestionLevelFive = 0;
            noOfQuestion = calculateTotal(noOfQuestionLevelOne,noOfQuestionLevelTwo,noOfQuestionLevelThree,noOfQuestionLevelFour,noOfQuestionLevelFive);

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));



            levelFourIsActive = true;
            goButton.setVisibility(View.INVISIBLE);
            levelLayout.setVisibility(View.INVISIBLE);
            gameLayout.setVisibility(View.VISIBLE);
            generateLevelFourQuestion();
            timerFunction();



        }
        else if(isLevelFiveUnlocked && tag.equals("55"))
        {
            score = score - scoreLevelFive;
            scoreLevelFive = 0;
            score=calculateTotal(scoreLevelOne,scoreLevelTwo,scoreLevelThree,scoreLevelFour,scoreLevelFive);


            noOfQuestion = noOfQuestion - noOfQuestionLevelFive;
            noOfQuestionLevelFive = 0;
            noOfQuestion = calculateTotal(noOfQuestionLevelOne,noOfQuestionLevelTwo,noOfQuestionLevelThree,noOfQuestionLevelFour,noOfQuestionLevelFive);

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));



            levelFiveIsActive = true;
            goButton.setVisibility(View.INVISIBLE);
            levelLayout.setVisibility(View.INVISIBLE);
            gameLayout.setVisibility(View.VISIBLE);
            generateLevelFiveQuestion();
            timerFunction();


        }

        else
        {
            Toast.makeText(this, "Level is still not yet unlocked!", Toast.LENGTH_SHORT).show();
            playMusic("wrong");
        }





    }

    CountDownTimer countDownTimer;

    // Fourth Thing - Timer Function Initialisation

    public void timerFunction() {


        countDownTimer = new CountDownTimer(timerValue, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerOn = true;
                String timeMessage = Integer.toString((int) millisUntilFinished / 1000);
                if (Integer.parseInt(timeMessage) <= 9) {
                    timeMessage = "0" + timeMessage;
                }
                saveMyGame();
                timerTextView.setText(timeMessage + " s");

            }

            @Override
            public void onFinish() {
                isTimerOn = false;


                /*
                if (isLevelOneUnlocked && isLevelTwoUnlocked && isLevelThreeUnlocked && levelThreeIsActive && scoreLevelThree >= levelThreshold)
                {

                    nextLevelButton.setVisibility(View.INVISIBLE);
                    finalButton.setVisibility(View.INVISIBLE);
                    congratsTextView.setVisibility(View.VISIBLE);

                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;

                }
                else
                {*/
                if (levelOneIsActive && scoreLevelOne < levelThreshold) {
                    failure++;
                    finalButton.setText("Play Again!");
                    nextLevelButton.setVisibility(View.INVISIBLE);
                    finalButton.setVisibility(View.VISIBLE);

                    playMusic("gameover");

                    levelOneIsActive = true;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = false;
                    isLevelThreeUnlocked = false;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;

                    if (scoreLevelOne == 0 && noOfQuestionLevelOne == 0) {
                        noOfQuestionLevelOne++;

                        scoreLevelTwo = 0;
                        scoreLevelThree = 0;
                        scoreLevelFour = 0;
                        scoreLevelFive = 0;

                        noOfQuestionLevelTwo = 0;
                        noOfQuestionLevelThree = 0;
                        noOfQuestionLevelFour = 0;
                        noOfQuestionLevelFive = 0;
                    }

                    saveMyGame();

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelOne)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();


                }
                else if (levelTwoIsActive && scoreLevelTwo <  levelThreshold) {

                    failure++;

                    finalButton.setText("Play Again!");
                    nextLevelButton.setVisibility(View.INVISIBLE);
                    finalButton.setVisibility(View.VISIBLE);

                    playMusic("gameover");

                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = false;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;

                    levelOneIsActive = false;
                    levelTwoIsActive = true;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    if (scoreLevelTwo == 0 && noOfQuestionLevelTwo == 0) {
                        noOfQuestionLevelTwo++;

                        scoreLevelThree = 0;
                        scoreLevelFour = 0;
                        scoreLevelFive = 0;

                        noOfQuestionLevelThree = 0;
                        noOfQuestionLevelFour = 0;
                        noOfQuestionLevelFive = 0;
                    }

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelTwo)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();

                }
                else if (levelThreeIsActive && scoreLevelThree < levelThreshold) {

                    failure++;

                    finalButton.setText("Play Again!");
                    nextLevelButton.setVisibility(View.INVISIBLE);
                    finalButton.setVisibility(View.VISIBLE);

                    playMusic("gameover");

                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;

                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = true;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    if (scoreLevelThree == 0 && noOfQuestionLevelThree == 0) {
                        noOfQuestionLevelThree++;

                        scoreLevelFour = 0;
                        scoreLevelFive = 0;

                        noOfQuestionLevelFour = 0;
                        noOfQuestionLevelFive = 0;
                    }

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelThree)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();
                    saveMyGame();

                }


                else if (levelFourIsActive && scoreLevelFour < levelThreshold) {

                    failure++;

                    finalButton.setText("Play Again!");
                    nextLevelButton.setVisibility(View.INVISIBLE);
                    finalButton.setVisibility(View.VISIBLE);

                    playMusic("gameover");

                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = true;
                    isLevelFiveUnlocked = false;

                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = true;
                    levelFiveIsActive = false;

                    if (scoreLevelFour == 0 && noOfQuestionLevelFour == 0) {
                        noOfQuestionLevelFour++;

                        scoreLevelFive = 0;

                        noOfQuestionLevelFive = 0;
                    }

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelFour)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();

                }


                else if (levelFiveIsActive && scoreLevelFive < levelThreshold) {

                    failure++;

                    finalButton.setText("Play Again!");
                    nextLevelButton.setVisibility(View.INVISIBLE);
                    finalButton.setVisibility(View.VISIBLE);

                    playMusic("gameover");

                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = true;
                    isLevelFiveUnlocked = true;

                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = true;

                    if (scoreLevelFive == 0 && noOfQuestionLevelFive == 0) {
                        noOfQuestionLevelFive++;

                    }

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelFive)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();

                }


                else if (levelOneIsActive && scoreLevelOne >= levelThreshold) {

                    success++;

                    playMusic("nextlevel");

                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = false;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;

                    nextLevelButton.setText("Level Two");
                    finalButton.setVisibility(View.INVISIBLE);
                    nextLevelButton.setVisibility(View.VISIBLE);

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelOne)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();
                }
                else if (levelTwoIsActive && scoreLevelTwo >= levelThreshold) {

                    success++;


                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    playMusic("nextlevel");


                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;

                    nextLevelButton.setText("Level Three");
                    finalButton.setVisibility(View.INVISIBLE);
                    nextLevelButton.setVisibility(View.VISIBLE);

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelTwo)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();
                }


                else if (levelThreeIsActive && scoreLevelThree >= levelThreshold) {

                    success++;


                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    playMusic("nextlevel");


                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = true;
                    isLevelFiveUnlocked = false;

                    nextLevelButton.setText("Level Four");
                    finalButton.setVisibility(View.INVISIBLE);
                    nextLevelButton.setVisibility(View.VISIBLE);

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelThree)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();
                }


                else if (levelFourIsActive && scoreLevelFour >= levelThreshold) {

                    success++;


                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    playMusic("nextlevel");


                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = true;
                    isLevelFiveUnlocked = true;

                    nextLevelButton.setText("Level Five");
                    finalButton.setVisibility(View.INVISIBLE);
                    nextLevelButton.setVisibility(View.VISIBLE);

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelFour)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();
                }





                else if (levelFiveIsActive && scoreLevelFive >= levelThreshold) {

                    viewKonfetti.build()
                            .addColors(Color.YELLOW, Color.BLUE, Color.RED)
                            .setDirection(0.0, 359.0)
                            .setSpeed(1f, 5f)
                            .setFadeOutEnabled(true)
                            .setTimeToLive(2000L)
                            .addShapes(Shape.RECT, Shape.CIRCLE)
                            .addSizes(new Size(12, 5))
                            .setPosition(-50f, viewKonfetti.getWidth() + 50f, -100f, -100f)
                            .streamFor(300, 3000L);

                    success++;

                    nextLevelButton.setVisibility(View.INVISIBLE);
                    finalButton.setVisibility(View.INVISIBLE);
                    congratsTextView.setVisibility(View.VISIBLE);

                    playMusic("congrats");


                    score = calculateTotal(scoreLevelOne, scoreLevelTwo, scoreLevelThree, scoreLevelFour, scoreLevelFive);
                    noOfQuestion = calculateTotal(noOfQuestionLevelOne, noOfQuestionLevelTwo, noOfQuestionLevelThree, noOfQuestionLevelFour, noOfQuestionLevelFive);

                    Date today = new Date();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM");
                    SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");

                    String date = dateformat.format(today);
                    String time = timeformat.format(today);

                    int mode = sharedPreferences.getInt("gameMode",2);
                    String s;
                    if(mode == 1)
                    {
                        s = "EASY";
                    }
                    else if(mode == 2)
                    {
                        s = "MEDIUM";
                    }
                    else if(mode == 3)
                    {
                        s = "HARD";
                    }
                    else {
                        s = "MEDIUM";
                    }
                    String finalScore = "\t\t"+String.valueOf(score) + "/" + String.valueOf(noOfQuestion)+"\t\t"+date+"\t\t"+time+"\t\t"+s.toUpperCase();

                    if(progress.size() == 5)
                    {
                        progress.remove(0);
                    }
                    progress.add(finalScore);

                    try
                    {

                        sharedPreferences.edit().putString("progress", ObjectSerializer.serialize(progress)).apply();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    levelOneIsActive = false;
                    levelTwoIsActive = false;
                    levelThreeIsActive = false;
                    levelFourIsActive = false;
                    levelFiveIsActive = false;

                    sharedPreferences.edit().putInt("allTimeTotalScore",(sharedPreferences.getInt("allTimeTotalScore",0)+scoreLevelFive)).apply();
                    sharedPreferences.edit().putInt("totalAttempt",(sharedPreferences.getInt("totalAttempt",0)+success+failure)).apply();

                    saveMyGame();
                }


                score = calculateTotal(scoreLevelOne, scoreLevelTwo, scoreLevelThree, scoreLevelFour, scoreLevelFive);
                noOfQuestion = calculateTotal(noOfQuestionLevelOne, noOfQuestionLevelTwo, noOfQuestionLevelThree, noOfQuestionLevelFour, noOfQuestionLevelFive);

                finalGameLayout.setVisibility(View.VISIBLE);
                finalScoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));





            }
        }.start();

    }




    // Fifth Thing - generating level wise questions



    public void generateLevelOneQuestion()
    {
        makeRoundButton();
        answer.clear();
        int a , b;
        String operand ;
        correctAnswerPosition = rand.nextInt(4);
        int operatorPosition;
        int incorrectAnswer;
        int sum;
        operatorPosition = 0;
        operand = operator.get(operatorPosition);

        a = rand.nextInt(21);
        b = rand.nextInt(21);

        sum = decodeTheSum(a,b,operand);

        sumTextView.setText(Integer.toString(a)+" "+operand+" "+Integer.toString(b));

        int temp = 98;

        for(int i = 0; i<4 ; i++)
        {
            if( i == correctAnswerPosition)
            {
                answer.add(sum);
            }
            else
            {
                incorrectAnswer = generateIncorrect(operand,sum);
                while(incorrectAnswer == temp || incorrectAnswer == sum)
                {
                    incorrectAnswer = generateIncorrect(operand,sum);
                }
                answer.add(incorrectAnswer);
                temp = incorrectAnswer;
            }

        }


        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));




    }


    public void generateLevelTwoQuestion()
    {
        makeRoundButton();

        answer.clear();
        int a , b;
        String operand ;
        correctAnswerPosition = rand.nextInt(4);
        int operatorPosition;
        int incorrectAnswer;
        int sum;
        operatorPosition = 1;

        operand = operator.get(operatorPosition);

        a = rand.nextInt(21);
        b = rand.nextInt(21);


        sum = decodeTheSum(a,b,operand);

        sumTextView.setText(Integer.toString(a)+" "+operand+" "+Integer.toString(b));
        int temp = 99;

        for(int i = 0; i<4 ; i++)
        {
            if( i == correctAnswerPosition)
            {
                answer.add(sum);
            }
            else
            {
                incorrectAnswer = generateIncorrect(operand,sum);
                while(incorrectAnswer == temp || incorrectAnswer == sum)
                {
                    incorrectAnswer = generateIncorrect(operand,sum);
                }
                answer.add(incorrectAnswer);
                temp = incorrectAnswer;
            }

        }




        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));




    }


    public void generateLevelThreeQuestion()
    {
        makeRoundButton();

        answer.clear();
        int a , b;
        String operand ;
        correctAnswerPosition = rand.nextInt(4);
        int operatorPosition;
        int incorrectAnswer;
        int sum;
        operatorPosition = rand.nextInt(2);

        operand = operator.get(operatorPosition);


        a = rand.nextInt(21);
        b = rand.nextInt(21);



        sum = decodeTheSum(a,b,operand);

        sumTextView.setText(Integer.toString(a)+" "+operand+" "+Integer.toString(b));
        int temp = 99;

        for(int i = 0; i<4 ; i++)
        {
            if( i == correctAnswerPosition)
            {
                answer.add(sum);
            }
            else
            {
                incorrectAnswer = generateIncorrect(operand,sum);
                while(incorrectAnswer == temp || incorrectAnswer == sum)
                {
                    incorrectAnswer = generateIncorrect(operand,sum);
                }
                answer.add(incorrectAnswer);
                temp = incorrectAnswer;
            }

        }




        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));




    }


    public void generateLevelFourQuestion()
    {
        makeRoundButton();

        answer.clear();
        int a, b, c;
        String operand ;
        correctAnswerPosition = rand.nextInt(4);
        int operatorPosition;
        int incorrectAnswer;
        int sum;

        operatorPosition = rand.nextInt(2);

        operand = operator.get(operatorPosition);



        a = rand.nextInt(21);
        b = rand.nextInt(21);
        c = rand.nextInt(21);


        sum = decodeTheSumLevelFour(a,b,c,operand);

        if(operand.equals("+"))
        {
            sumTextView.setText(Integer.toString(a)+" "+operand+" "+Integer.toString(b)+" "+operand+" "+Integer.toString(c));
        }
        else
        {
            sumTextView.setText("-( "+Integer.toString(a)+" + "+Integer.toString(b)+" + "+Integer.toString(c)+") ");
        }

        int temp = 99;

        for(int i = 0; i<4 ; i++)
        {
            if( i == correctAnswerPosition)
            {
                answer.add(sum);
            }
            else
            {
                incorrectAnswer = generateIncorrectLevelFour(operand);
                while(incorrectAnswer == temp || incorrectAnswer == sum)
                {
                    incorrectAnswer = generateIncorrectLevelFour(operand);
                }
                answer.add(incorrectAnswer);
                temp = incorrectAnswer;
            }

        }



        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));




    }


    public void generateLevelFiveQuestion()
    {
        makeRoundButton();

        answer.clear();
        int a, c;
        double b;
        String operandOne ;
        String operandTwo ;
        correctAnswerPosition = rand.nextInt(4);
        int operatorPosition;
        int incorrectAnswer;
        int sum;
        operatorPosition = rand.nextInt(2);


        operandOne = operator.get(operatorPosition);


        if (operandOne.equals("+"))
        {
            operandOne = "+";
            operandTwo = "*";
        }
        else
        {
            operandOne = "*";
            operandTwo = "+";
        }

        a = rand.nextInt(10);
        b = rand.nextInt(10);
        c = rand.nextInt(10);

        a = (a*100);
        b = Double.parseDouble(String.format("%1.1e",b*.1));
        c=  (c*100);

        sum = decodeTheSumLevelFive(a,b,c,operandOne,operandTwo);

        sumTextView.setText(Integer.toString(a)+" "+operandOne+" "+Double.toString(b)+" "+operandTwo+" "+Integer.toString(c));
        int temp = 99;

        for(int i = 0; i<4 ; i++)
        {
            if( i == correctAnswerPosition)
            {
                answer.add(sum);
            }
            else
            {
                incorrectAnswer = generateIncorrectLevelFive(operandOne,operandTwo);
                while(incorrectAnswer == temp || incorrectAnswer == sum)
                {
                    incorrectAnswer = generateIncorrectLevelFive(operandOne,operandTwo);
                }
                answer.add(incorrectAnswer);
                temp = incorrectAnswer;
            }

        }



        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));




    }





    // Sixth thing - when the final play again button is pressed


    public void restartGame(View view)
    {
        try {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        gamePaused = false;

        if(levelOneIsActive)
        {
            initialiseLevelOne();
        }

        if(levelTwoIsActive)
        {
            initialiseLevelTwo();
        }

        if(levelThreeIsActive)
        {
            initialiseLevelThree();
        }

        if(levelFourIsActive)
        {
            initialiseLevelFour();
        }

        if(levelFiveIsActive)
        {
            initialiseLevelFive();
        }


    }



    //Replaying the levels that are already played



    public void initialiseLevelOne()
    {


        goButton.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);

        isTutorialActive = false;

        isLevelOneUnlocked = true;
        isLevelTwoUnlocked = false;
        isLevelThreeUnlocked = false;
        isLevelFourUnlocked = false;
        isLevelFiveUnlocked = false;


        levelOneIsActive = true;
        levelTwoIsActive = false;
        levelThreeIsActive = false;
        levelFourIsActive = false;
        levelFiveIsActive = false;


        score = 0;
        scoreLevelOne = 0;
        scoreLevelTwo = 0;
        scoreLevelThree = 0;
        scoreLevelFour = 0;
        scoreLevelFive = 0;


        noOfQuestion = 0;
        noOfQuestionLevelOne = 0;
        noOfQuestionLevelTwo = 0;
        noOfQuestionLevelThree = 0;
        noOfQuestionLevelFour = 0;
        noOfQuestionLevelFive = 0;


        finalButton.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);

        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");


        scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
        timerTextView.setText("0 s");


        generateLevelOneQuestion();
        timerFunction();
        gameLayout.setVisibility(View.VISIBLE);


    }


    public void initialiseLevelTwo()
    {
        goButton.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);

        isTutorialActive = false;

        isLevelOneUnlocked = true;
        isLevelTwoUnlocked = true;
        isLevelThreeUnlocked = false;
        isLevelFourUnlocked = false;
        isLevelFiveUnlocked = false;


        levelOneIsActive = false;
        levelTwoIsActive = true;
        levelThreeIsActive = false;
        levelFourIsActive = false;
        levelFiveIsActive = false;

        score = score - scoreLevelTwo - scoreLevelThree - scoreLevelFour - scoreLevelFive;
        scoreLevelTwo = 0;
        scoreLevelThree = 0;
        scoreLevelFour = 0;
        scoreLevelFive = 0;


        noOfQuestion = noOfQuestion - noOfQuestionLevelTwo - noOfQuestionLevelThree - noOfQuestionLevelFour - noOfQuestionLevelFive;
        noOfQuestionLevelTwo = 0;
        noOfQuestionLevelThree = 0;
        noOfQuestionLevelFour = 0;
        noOfQuestionLevelFive = 0;

        finalButton.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);

        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");

        scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
        timerTextView.setText("0 s");


        generateLevelTwoQuestion();
        timerFunction();
        gameLayout.setVisibility(View.VISIBLE);


    }





    public void initialiseLevelThree() {

        goButton.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);


        isTutorialActive = false;

        isLevelOneUnlocked = true;
        isLevelTwoUnlocked = true;
        isLevelThreeUnlocked = true;
        isLevelFourUnlocked = false;
        isLevelFiveUnlocked = false;


        levelOneIsActive = false;
        levelTwoIsActive = false;
        levelThreeIsActive = true;
        levelFourIsActive = false;
        levelFiveIsActive = false;

        score = score - scoreLevelThree -scoreLevelFour - scoreLevelFive;
        scoreLevelThree = 0;
        scoreLevelFour = 0;
        scoreLevelFive = 0;


        noOfQuestion = noOfQuestion - noOfQuestionLevelThree - noOfQuestionLevelFour - noOfQuestionLevelFive;
        noOfQuestionLevelThree = 0;
        noOfQuestionLevelFour = 0;
        noOfQuestionLevelFive = 0;

        finalButton.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);

        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");


        scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
        timerTextView.setText("0 s");



        generateLevelThreeQuestion();
        timerFunction();
        gameLayout.setVisibility(View.VISIBLE);


    }

    public void initialiseLevelFour()
    {

        goButton.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);


        isTutorialActive = false;

        isLevelOneUnlocked = true;
        isLevelTwoUnlocked = true;
        isLevelThreeUnlocked = true;
        isLevelFourUnlocked = true;
        isLevelFiveUnlocked = false;


        levelOneIsActive = false;
        levelTwoIsActive = false;
        levelThreeIsActive = false;
        levelFourIsActive = true;
        levelFiveIsActive = false;

        score = score - scoreLevelFour - scoreLevelFive;
        scoreLevelFour = 0;
        scoreLevelFive = 0;


        noOfQuestion = noOfQuestion - noOfQuestionLevelFour - noOfQuestionLevelFive;
        noOfQuestionLevelFour = 0;
        noOfQuestionLevelFive = 0;

        finalButton.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);

        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");


        scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
        timerTextView.setText("0 s");



        generateLevelFourQuestion();
        timerFunction();
        gameLayout.setVisibility(View.VISIBLE);


    }


    public void initialiseLevelFive() {

        goButton.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);


        isTutorialActive = false;

        isLevelOneUnlocked = true;
        isLevelTwoUnlocked = true;
        isLevelThreeUnlocked = true;
        isLevelFourUnlocked = true;
        isLevelFiveUnlocked = true;


        levelOneIsActive = false;
        levelTwoIsActive = false;
        levelThreeIsActive = false;
        levelFourIsActive = false;
        levelFiveIsActive = true;

        score = score - scoreLevelFive;
        scoreLevelFive = 0;


        noOfQuestion = noOfQuestion - noOfQuestionLevelFive;
        noOfQuestionLevelFive = 0;

        finalButton.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);

        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");


        scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
        timerTextView.setText("0 s");



        generateLevelFiveQuestion();
        timerFunction();
        gameLayout.setVisibility(View.VISIBLE);


    }





    // Seventh Thing - when the final new level button is played

    public void nextLevelFunction(View view)
    {
        try {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        gamePaused = false;

        goButton.setVisibility(View.INVISIBLE);
        levelLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);

        if(nextLevelButton.getText().equals("Level Two"))
        {
            isLevelOneUnlocked=true;
            isLevelTwoUnlocked=true;
            isLevelThreeUnlocked=false;
            isLevelFourUnlocked = false;
            isLevelFiveUnlocked = false;

            levelOneIsActive=false;
            levelTwoIsActive=true;
            levelThreeIsActive=false;
            levelFourIsActive = false;
            levelFiveIsActive = false;

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
            timerTextView.setText("0 s");


            generateLevelTwoQuestion();
            timerFunction();
            gameLayout.setVisibility(View.VISIBLE);
        }

        if(nextLevelButton.getText().equals("Level Three"))
        {

            isLevelOneUnlocked=true;
            isLevelTwoUnlocked=true;
            isLevelThreeUnlocked=true;
            isLevelFourUnlocked = false;
            isLevelFiveUnlocked = false;

            levelOneIsActive=false;
            levelTwoIsActive=false;
            levelThreeIsActive=true;
            levelFourIsActive = false;
            levelFiveIsActive = false;

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
            timerTextView.setText("0 s");

            generateLevelThreeQuestion();
            timerFunction();
            gameLayout.setVisibility(View.VISIBLE);
        }


        if(nextLevelButton.getText().equals("Level Four"))
        {

            isLevelOneUnlocked=true;
            isLevelTwoUnlocked=true;
            isLevelThreeUnlocked=true;
            isLevelFourUnlocked = true;
            isLevelFiveUnlocked = false;

            levelOneIsActive= false;
            levelTwoIsActive= false;
            levelThreeIsActive= false;
            levelFourIsActive = true;
            levelFiveIsActive = false;

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
            timerTextView.setText("0 s");

            generateLevelFourQuestion();
            timerFunction();
            gameLayout.setVisibility(View.VISIBLE);
        }

        if(nextLevelButton.getText().equals("Level Five"))
        {

            isLevelOneUnlocked=true;
            isLevelTwoUnlocked=true;
            isLevelThreeUnlocked=true;
            isLevelFourUnlocked = true;
            isLevelFiveUnlocked = true;

            levelOneIsActive= false;
            levelTwoIsActive= false;
            levelThreeIsActive= false;
            levelFourIsActive = false;
            levelFiveIsActive = true;

            scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
            timerTextView.setText("0 s");

            generateLevelFiveQuestion();
            timerFunction();
            gameLayout.setVisibility(View.VISIBLE);
        }



    }


    //Eighth thing - going back to main menu having unlocked everything


    public void goToMainMenu(View view)
    {
        try {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        goButton.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.INVISIBLE);
        finalGameLayout.setVisibility(View.INVISIBLE);

        isTutorialActive = false;



        levelOneIsActive = false;
        levelTwoIsActive = false;
        levelThreeIsActive = false;
        levelFourIsActive = false;
        levelFiveIsActive = false;

/*
        score = 0;
        scoreLevelOne = 0;
        scoreLevelTwo = 0;
        scoreLevelThree = 0;


        noOfQuestion = 0;
        noOfQuestionLevelOne = 0;
        noOfQuestionLevelTwo = 0;
        noOfQuestionLevelThree = 0;
*/


        finalButton.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);


        congratsTextView.setVisibility(View.INVISIBLE);


        answer = new ArrayList<Integer>();
        operator = new ArrayList<String>();
        answer.clear();
        operator.clear();
        operator.add("+");
        operator.add("-");


        score = calculateTotal(scoreLevelOne,scoreLevelTwo,scoreLevelThree,scoreLevelFour,scoreLevelFive);
        noOfQuestion = calculateTotal(noOfQuestionLevelOne,noOfQuestionLevelTwo,noOfQuestionLevelThree,noOfQuestionLevelFour,noOfQuestionLevelFive);

        scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));


        levelLayout.setVisibility(View.VISIBLE);

    }


    //Ninenth thing - when the answers are selected

    public void selectAnswer(View view)
    {
        if(isTimerOn) {
            answerPositionClicked = Integer.parseInt(view.getTag().toString());
            gamePaused = false;

            if (view.getTag().toString().equals(String.valueOf(correctAnswerPosition))) {
                view.setBackgroundResource(R.drawable.correctbutton);
            } else {
                view.setBackgroundResource(R.drawable.wrongbutton);
            }

            if (levelOneIsActive && !(levelTwoIsActive) && !(levelThreeIsActive) && !(levelFourIsActive) && !(levelFiveIsActive)) {
                if (view.getTag().toString().equals(String.valueOf(correctAnswerPosition))) {
                    playMusic("correct");
                    scoreLevelOne++;
                    scoreLevelTwo = 0;
                    scoreLevelThree = 0;
                    scoreLevelFour = 0;
                    scoreLevelFive = 0;

                    noOfQuestionLevelOne++;
                    noOfQuestionLevelTwo = 0;
                    noOfQuestionLevelThree = 0;
                    noOfQuestionLevelFour = 0;
                    noOfQuestionLevelFive = 0;
                } else {
                    playMusic("wrong");
                    noOfQuestionLevelOne++;
                    noOfQuestionLevelTwo = 0;
                    noOfQuestionLevelThree = 0;
                    noOfQuestionLevelFour = 0;
                    noOfQuestionLevelFive = 0;


                }


                if (scoreLevelOne < levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = false;
                    isLevelThreeUnlocked = false;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;
                    saveMyGame();
                } else if (scoreLevelOne >= levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = false;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;
                    saveMyGame();
                }

                score = calculateTotal(scoreLevelOne, scoreLevelTwo, scoreLevelThree, scoreLevelFour, scoreLevelFive);
                noOfQuestion = calculateTotal(noOfQuestionLevelOne, noOfQuestionLevelTwo, noOfQuestionLevelThree, noOfQuestionLevelFour, noOfQuestionLevelFive);
                scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
                saveMyGame();

                generateLevelOneQuestion();

            } else if (!(levelOneIsActive) && levelTwoIsActive && !(levelThreeIsActive) && !(levelFourIsActive) && !(levelFiveIsActive)) {
                if (view.getTag().toString().equals(String.valueOf(correctAnswerPosition))) {

                    playMusic("correct");

                    scoreLevelTwo++;
                    scoreLevelThree = 0;
                    scoreLevelFour = 0;
                    scoreLevelFive = 0;

                    noOfQuestionLevelTwo++;
                    noOfQuestionLevelThree = 0;
                    noOfQuestionLevelFour = 0;
                    noOfQuestionLevelFive = 0;
                } else {

                    playMusic("wrong");
                    noOfQuestionLevelTwo++;
                    noOfQuestionLevelThree = 0;
                    noOfQuestionLevelFour = 0;
                    noOfQuestionLevelFive = 0;


                }


                if (scoreLevelTwo < levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = false;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;
                    saveMyGame();
                } else if (scoreLevelTwo >= levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;
                    saveMyGame();
                }

                score = calculateTotal(scoreLevelOne, scoreLevelTwo, scoreLevelThree, scoreLevelFour, scoreLevelFive);
                noOfQuestion = calculateTotal(noOfQuestionLevelOne, noOfQuestionLevelTwo, noOfQuestionLevelThree, noOfQuestionLevelFour, noOfQuestionLevelFive);
                scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
                saveMyGame();

                generateLevelTwoQuestion();

            } else if (!(levelOneIsActive) && !(levelTwoIsActive) && (levelThreeIsActive) && !(levelFourIsActive) && !(levelFiveIsActive)) {
                if (view.getTag().toString().equals(String.valueOf(correctAnswerPosition))) {

                    playMusic("correct");

                    scoreLevelThree++;
                    scoreLevelFour = 0;
                    scoreLevelFive = 0;

                    noOfQuestionLevelThree++;
                    noOfQuestionLevelFour = 0;
                    noOfQuestionLevelFive = 0;
                } else {

                    playMusic("wrong");

                    noOfQuestionLevelThree++;
                    noOfQuestionLevelFour = 0;
                    noOfQuestionLevelFive = 0;


                }


                if (scoreLevelThree < levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = false;
                    isLevelFiveUnlocked = false;
                    saveMyGame();
                } else if (scoreLevelThree >= levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = true;
                    isLevelFiveUnlocked = false;
                    saveMyGame();
                }


                score = calculateTotal(scoreLevelOne, scoreLevelTwo, scoreLevelThree, scoreLevelFour, scoreLevelFive);
                noOfQuestion = calculateTotal(noOfQuestionLevelOne, noOfQuestionLevelTwo, noOfQuestionLevelThree, noOfQuestionLevelFour, noOfQuestionLevelFive);
                scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
                saveMyGame();

                generateLevelThreeQuestion();

            } else if (!(levelOneIsActive) && !(levelTwoIsActive) && !(levelThreeIsActive) && (levelFourIsActive) && !(levelFiveIsActive)) {
                if (view.getTag().toString().equals(String.valueOf(correctAnswerPosition))) {

                    playMusic("correct");

                    scoreLevelFour++;
                    scoreLevelFive = 0;

                    noOfQuestionLevelFour++;
                    noOfQuestionLevelFive = 0;
                } else {

                    playMusic("wrong");

                    noOfQuestionLevelFour++;
                    noOfQuestionLevelFive = 0;

                }


                if (scoreLevelFour < levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = true;
                    isLevelFiveUnlocked = false;
                    saveMyGame();
                } else if (scoreLevelThree >= levelThreshold) {
                    isLevelOneUnlocked = true;
                    isLevelTwoUnlocked = true;
                    isLevelThreeUnlocked = true;
                    isLevelFourUnlocked = true;
                    isLevelFiveUnlocked = true;
                    saveMyGame();
                }


                score = calculateTotal(scoreLevelOne, scoreLevelTwo, scoreLevelThree, scoreLevelFour, scoreLevelFive);
                noOfQuestion = calculateTotal(noOfQuestionLevelOne, noOfQuestionLevelTwo, noOfQuestionLevelThree, noOfQuestionLevelFour, noOfQuestionLevelFive);
                scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
                saveMyGame();

                generateLevelFourQuestion();

            } else if (!(levelOneIsActive) && !(levelTwoIsActive) && !(levelThreeIsActive) && !(levelFourIsActive) && (levelFiveIsActive)) {
                if (view.getTag().toString().equals(String.valueOf(correctAnswerPosition))) {

                    playMusic("correct");

                    scoreLevelFive++;

                    noOfQuestionLevelFive++;
                } else {

                    playMusic("wrong");

                    noOfQuestionLevelFive++;

                }


                isLevelOneUnlocked = true;
                isLevelTwoUnlocked = true;
                isLevelThreeUnlocked = true;
                isLevelFourUnlocked = true;
                isLevelFiveUnlocked = true;
                saveMyGame();


                score = calculateTotal(scoreLevelOne, scoreLevelTwo, scoreLevelThree, scoreLevelFour, scoreLevelFive);
                noOfQuestion = calculateTotal(noOfQuestionLevelOne, noOfQuestionLevelTwo, noOfQuestionLevelThree, noOfQuestionLevelFour, noOfQuestionLevelFive);
                scoreTextView.setText(String.valueOf(score) + "/" + String.valueOf(noOfQuestion));
                saveMyGame();

                generateLevelFiveQuestion();

            }
        }
    }

    public void makeRoundButton() {
        long timerValue = 200;
            switch (answerPositionClicked) {
                case 0:
                    new CountDownTimer(timerValue,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            button1.setBackgroundResource(R.drawable.sharpbutton);
                            button2.setBackgroundResource(R.drawable.sharpbutton);
                            button3.setBackgroundResource(R.drawable.sharpbutton);
                        }

                        @Override
                        public void onFinish() {
                            button0.setBackgroundResource(R.drawable.sharpbutton);
                            button1.setBackgroundResource(R.drawable.sharpbutton);
                            button2.setBackgroundResource(R.drawable.sharpbutton);
                            button3.setBackgroundResource(R.drawable.sharpbutton);
                        }
                    }.start();

                    break;
                case 1:
                    new CountDownTimer(timerValue,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            button0.setBackgroundResource(R.drawable.sharpbutton);
                            button2.setBackgroundResource(R.drawable.sharpbutton);
                            button3.setBackgroundResource(R.drawable.sharpbutton);
                        }

                        @Override
                        public void onFinish() {
                            button0.setBackgroundResource(R.drawable.sharpbutton);
                            button1.setBackgroundResource(R.drawable.sharpbutton);
                            button2.setBackgroundResource(R.drawable.sharpbutton);
                            button3.setBackgroundResource(R.drawable.sharpbutton);
                        }
                    }.start();
                    break;
                case 2:
                    new CountDownTimer(timerValue,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            button0.setBackgroundResource(R.drawable.sharpbutton);
                            button1.setBackgroundResource(R.drawable.sharpbutton);
                            button3.setBackgroundResource(R.drawable.sharpbutton);
                        }

                        @Override
                        public void onFinish() {
                            button0.setBackgroundResource(R.drawable.sharpbutton);
                            button1.setBackgroundResource(R.drawable.sharpbutton);
                            button2.setBackgroundResource(R.drawable.sharpbutton);
                            button3.setBackgroundResource(R.drawable.sharpbutton);
                        }
                    }.start();
                    break;
                case 3:
                    new CountDownTimer(timerValue,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            button0.setBackgroundResource(R.drawable.sharpbutton);
                            button1.setBackgroundResource(R.drawable.sharpbutton);
                            button2.setBackgroundResource(R.drawable.sharpbutton);
                        }

                        @Override
                        public void onFinish() {
                            button0.setBackgroundResource(R.drawable.sharpbutton);
                            button1.setBackgroundResource(R.drawable.sharpbutton);
                            button2.setBackgroundResource(R.drawable.sharpbutton);
                            button3.setBackgroundResource(R.drawable.sharpbutton);
                        }
                    }.start();
                    break;
            }

    }




    // Complementary Functions for the above


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        gamePaused = false;

        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.profile:
                if(!isTimerOn) {
                    int allTimeScore = sharedPreferences.getInt("allTimeTotalScore",0);
                    intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                    intentProfile.putExtra("username", username);
                    intentProfile.putExtra("success", success);
                    intentProfile.putExtra("failure", failure);
                    intentProfile.putExtra("allTimeScore",allTimeScore);
                    startActivity(intentProfile);
                }
                else{
                    Toast.makeText(this, "Finish the level first!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.progress:
                if(!isTimerOn ) {
                    intentProgress = new Intent(getApplicationContext(), ProgressActivity.class);
                    intentProgress.putExtra("username",username);
                    intentProgress.putExtra("progress", progress);
                    startActivity(intentProgress);
                }
                else
                {
                    Toast.makeText(this, "Finish the level first!", Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.performance:
                if(!isTimerOn) {
                    int allTimeScore = sharedPreferences.getInt("allTimeTotalScore", 0);
                    int totalAttempt = sharedPreferences.getInt("totalAttempt", 0);
                    intentPerformance = new Intent(getApplicationContext(), PerformanceActivity.class);
                    intentPerformance.putExtra("username",username);
                    intentPerformance.putExtra("success",success);
                    intentPerformance.putExtra("failure",failure);
                    intentPerformance.putExtra("allTimeScore",allTimeScore);
                    intentPerformance.putExtra("totalAttempt",totalAttempt);
                    intentPerformance.putExtra("timerValue",(int) timerValue/1000);

                    startActivity(intentPerformance);
                }
                else
                {
                    Toast.makeText(this, "Finish the level first!", Toast.LENGTH_SHORT).show();

                }
                return true;

            case R.id.about:
                if(!isTimerOn) {
                    intentAboutMe = new Intent(getApplicationContext(),AboutMeActivity.class);
                    startActivity(intentAboutMe);



                }
                else
                {
                    Toast.makeText(this, "Finish the level first!", Toast.LENGTH_SHORT).show();

                }
                return true;

            case R.id.editProfile:
                if(!isTimerOn) {
                    gameLayout.setVisibility(View.INVISIBLE);
                    backEditUserName.setVisibility(View.VISIBLE);
                    welcomeBackTextView.setVisibility(View.INVISIBLE);
                    goButton.setVisibility(View.INVISIBLE);
                    gameModeButton.setVisibility(View.INVISIBLE);
                    levelLayout.setVisibility(View.INVISIBLE);
                    userNameEditText.setVisibility(View.VISIBLE);
                    enterButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(this, "Finish the level first!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.rate:
                if(!isTimerOn)
                {
                    rateApp();
                }
                else
                {
                    Toast.makeText(this, "Finish the level first!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.shareBrainjam:
                if(!isTimerOn)
                {
                    shareMyApp();
                }
                else
                {
                    Toast.makeText(this, "Finish the level first!", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return false;


        }
    }

    public void shareMyApp()
    {

/*
        int sOne = sharedPreferences.getInt("scoreLevelOne",0);
        int sTwo = sharedPreferences.getInt("scoreLevelTwo",0);
        int sThree = sharedPreferences.getInt("scoreLevelThree",0);
        int sFour = sharedPreferences.getInt("scoreLevelFour",0);
        int sFive = sharedPreferences.getInt("scoreLevelFive",0);


        int nOne = sharedPreferences.getInt("noOfQuestionLevelOne",0);
        int nTwo = sharedPreferences.getInt("noOfQuestionLevelTwo",0);
        int nThree = sharedPreferences.getInt("noOfQuestionLevelThree",0);
        int nFour = sharedPreferences.getInt("noOfQuestionLevelFour",0);
        int nFive = sharedPreferences.getInt("noOfQuestionLevelFive",0);

        int s = calculateTotal(sOne,sTwo,sThree,sFour,sFive);
        int n = calculateTotal(nOne,nTwo,nThree,nFour,nFive);
*/


        int allTimeTotalScore = sharedPreferences.getInt("allTimeTotalScore",0);

        try {
            if(allTimeTotalScore != 0 ) {
                String scre = String.valueOf(allTimeTotalScore);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BrainJam");
                String shareMessage = "\nCheck Out This Awesome Game & Beat My Current Total Score Of "+scre+" at BrainJam.\n\n-----------------\n\nDownload It Here:\n\n-----------------\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share Using "));
            }
            else {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BrainJam");
                String shareMessage = "\nCheck Out This Awesome Game BrainJam.\n\n-----------------\n\nDownload It Here:\n\n-----------------\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share Using "));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url,"com.somrajstudio.somrajmukherjee.brainjam")));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    public int decodeTheSumLevelFive(int a, double b, int c, String operandOne, String operandTwo)
    {
        if(operandOne.equals("+"))
        {
            return (int)(a+b*c);
        }else {
            return (int)(a*b+c);
        }
    }

    public int decodeTheSumLevelFour(int a, int b, int c, String op)
    {
        int sum;
        if(op.equals("+"))
        {
            sum = a+b+c;
        }
        else {
            sum = -(a+b+c);
        }
        return sum;


    }

    public int decodeTheSum(int a, int b, String op)
    {
        if(op.equals("+"))
        {
            return a+b;
        }
        else
        {
            return a-b;
        }
    }


    public int generateIncorrectLevelFive(String operandOne, String operandTwo)
    {
        int incorrectActualAnswer;
        int a,c;
        double b;

        a = rand.nextInt(10);
        b = rand.nextInt(10);
        c = rand.nextInt(10);

        a = a*100;
        b = b*.1;
        c=  c*100;

        if(operandOne.equals("+"))
        {

            incorrectActualAnswer = (int) (a+b*c);
        }
        else
        {
            incorrectActualAnswer = (int) (a*b+c);
        }
        return incorrectActualAnswer;
    }


    public int generateIncorrectLevelFour(String op)
    {
        int incorrectActualAnswer;
        if(op.equals("+"))
        {
            incorrectActualAnswer = rand.nextInt(61);
        }
        else
        {
            incorrectActualAnswer = -1 * rand.nextInt(61);
        }
        return incorrectActualAnswer;
    }

    public int generateIncorrect(String op, int sum)
    {
        int incorrectActualAnswer;
        if(op.equals("+"))
        {
            int a = rand.nextInt(21);
            int b = rand.nextInt(21);

            incorrectActualAnswer = a+b;
        }
        else
        {
            if(sum < 0)
            {
                incorrectActualAnswer = -1 * rand.nextInt(41);

            }
            else
            {
                incorrectActualAnswer = rand.nextInt(41);
            }
        }
        return incorrectActualAnswer;
    }

    public void pauseFunction()
    {
        if(levelOneIsActive)
        {
            if(scoreLevelOne<levelThreshold)
            {
                failure++;
                sharedPreferences.edit().putInt("failure",failure).commit();
            }
            else
            {
                success++;
                sharedPreferences.edit().putInt("success",success).commit();
            }
        }
        else if(levelTwoIsActive)
        {
            if(scoreLevelTwo<levelThreshold)
            {
                failure++;
                sharedPreferences.edit().putInt("failure",failure).commit();
            }
            else
            {
                success++;
                sharedPreferences.edit().putInt("success",success).commit();
            }
        }else if(levelThreeIsActive)
        {
            if(scoreLevelThree<levelThreshold)
            {
                failure++;
                sharedPreferences.edit().putInt("failure",failure).commit();
            }
            else
            {
                success++;
                sharedPreferences.edit().putInt("success", success).commit();
            }
        }
        else if(levelFourIsActive)
        {
            if(scoreLevelFour<levelThreshold)
            {
                failure++;
                sharedPreferences.edit().putInt("failure",failure).commit();
            }
            else
            {
                success++;
                sharedPreferences.edit().putInt("success", success).commit();
            }
        }else if(levelFiveIsActive)
        {
            if(scoreLevelFive<levelThreshold)
            {
                failure++;
                sharedPreferences.edit().putInt("failure",failure).commit();
            }
            else
            {
                success++;
                sharedPreferences.edit().putInt("success", success).commit();
            }
        }
    }

    public void gamenormalise()
    {
        if(listIsOn)
        {
            showTutorialTextView.setVisibility(View.VISIBLE);
            listIsOn = false;
            helpListView.setVisibility(View.INVISIBLE);
            helpButton.setText("STILL NEED HELP?");
        }

        listIsOn = false;
        howToPlayButton.setVisibility(View.VISIBLE);
        gridLevelLayout.setVisibility(View.VISIBLE);
        levelLayout.setVisibility(View.VISIBLE);
        tutorialLayout.setVisibility(View.INVISIBLE);
        backLevelLayout.setVisibility(View.VISIBLE);
        isTutorialActive = false;
        if(isTimerOn)
        {
            countDownTimer.cancel();
        }

    }


    @Override
    public void onResume()
    {
        pauseFunction();
        gamenormalise();
        try {
            initialiseTheWholeGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onResume();
        gamePaused = false;
    }
    @Override
    public void onPause()
    {
        try
        {
            if(mediaPlayer != null)
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onPause();
        gamePaused = true;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        gamePaused = true;
    }


    int doubleBackToExitPressed = 1;
    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressed == 2) {
            pauseFunction();
            finishAffinity();
            System.exit(0);
        }
        else
            {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }

 /*   @Override
    public void onBackPressed() {
        if (backButtonPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backButtonPressedTime = System.currentTimeMillis();
    }
*/

    public void playMusic(String string)
    {
        if(!gamePaused) {
            int resourceid = getResources().getIdentifier(string, "raw", "com.somrajstudio.somrajmukherjee.brainjam");
            mediaPlayer = MediaPlayer.create(this, resourceid);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }

                }
            });
        }

    }


    public int calculateTotal(int a, int b, int c, int d, int e)
    {
        return (a + b + c + d + e);
    }




    public void saveMyGame()
    {
        //All the score are saved here!

        sharedPreferences.edit().putInt("success",success).apply();
        sharedPreferences.edit().putInt("failure",failure).apply();


        sharedPreferences.edit().putInt("score",score).apply();
        sharedPreferences.edit().putInt("scoreLevelOne",scoreLevelOne).apply();
        sharedPreferences.edit().putInt("scoreLevelTwo",scoreLevelTwo).apply();
        sharedPreferences.edit().putInt("scoreLevelThree",scoreLevelThree).apply();
        sharedPreferences.edit().putInt("scoreLevelFour",scoreLevelFour).apply();
        sharedPreferences.edit().putInt("scoreLevelFive",scoreLevelFive).apply();



        sharedPreferences.edit().putInt("noOfQuestion",noOfQuestion).apply();
        sharedPreferences.edit().putInt("noOfQuestionLevelOne",noOfQuestionLevelOne).apply();
        sharedPreferences.edit().putInt("noOfQuestionLevelTwo",noOfQuestionLevelTwo).apply();
        sharedPreferences.edit().putInt("noOfQuestionLevelThree",noOfQuestionLevelThree).apply();
        sharedPreferences.edit().putInt("noOfQuestionLevelFour",noOfQuestionLevelFour).apply();
        sharedPreferences.edit().putInt("noOfQuestionLevelFive",noOfQuestionLevelFive).apply();



        //All the levels are saved here!

        sharedPreferences.edit().putBoolean("isLevelOneUnlocked",isLevelOneUnlocked).apply();
        sharedPreferences.edit().putBoolean("isLevelTwoUnlocked",isLevelTwoUnlocked).apply();
        sharedPreferences.edit().putBoolean("isLevelThreeUnlocked",isLevelThreeUnlocked).apply();
        sharedPreferences.edit().putBoolean("isLevelFourUnlocked",isLevelFourUnlocked).apply();
        sharedPreferences.edit().putBoolean("isLevelFiveUnlocked",isLevelFiveUnlocked).apply();

    }
    TextView showTutorialTextView;

    public void helpFunction(View view)
    {
        showTutorialTextView = (TextView) findViewById(R.id.showTutorialTextView);

        if(!listIsOn)
        {
            showTutorialTextView.setVisibility(View.INVISIBLE);
            listIsOn = true;
            helpList = new ArrayList<String>();
            helpList.add("Profile Section:");
            helpList.add("Success and failure are counted on whether you are able to cross a level or not. Total score shows the aggregate for every correct answer.");
            helpList.add("Progress Section:");
            helpList.add("This section contains the details of the last five successfully finished games ( i.e., Progress is updated when 5 levels are completed ). Progress is basically for your evaluation purpose.");
            helpList.add("Performance Section:");
            helpList.add("This section stores performance quotient based on your success with respect to the total number of attempts. Speed is calculated once you have crossed a level.");
            helpList.add("Game mode selection:");
            helpList.add("Once you change the game mode, you will lose your progress. You start afresh from level one.");
            helpList.add("Roll back:");
            helpList.add("Playing rollback to a previous level implies that you will have to complete the rest of the upcoming levels again. If you have selected a particular game mode, you will have to finish all the 5 levels with that mode.");
            helpList.add("Share:");
            helpList.add("Share button allows you to challenge your friends.");
            helpList.add("Rate: ");
            helpList.add("I, Somraj Mukherjee have developed BrainJam with a lot of hard work and passion. Please take a moment to rate my app on Playstore.");
            helpList.add("Feedback: ");
            helpList.add("I am always waiting for your suggestions. ");
            helpList.add("Support: ");
            helpList.add("If this application was successful in entertaining your grey matter, please appreciate me in Support section. It will inspire me to run the distance.");
            helpList.add("For business inquiry: ");
            helpList.add("Go to the About section and click on my image.");




            helpListsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,helpList);
            helpListView.setAdapter(helpListsAdapter);
            helpListView.setVisibility(View.VISIBLE);
            helpListView.setBackgroundResource(R.drawable.roundbutton);
            helpButton.setText("CLOSE HELP");
        }
        else
        {
            showTutorialTextView.setVisibility(View.VISIBLE);
            listIsOn = false;
            helpListView.setVisibility(View.INVISIBLE);
            helpButton.setText("STILL NEED HELP?");
        }

    }





}