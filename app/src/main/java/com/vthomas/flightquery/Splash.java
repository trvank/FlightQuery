
package com.vthomas.flightquery;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

import java.util.Locale;


public class Splash extends AppCompatActivity implements OnInitListener {

    private TextToSpeech tts = null;
    String text = "Welcome to Flight Query";
    private int MY_DATA_CHECK_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Check for TTS
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);



        //get the screen size so that we can size the text
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        // Get the screen's density scale
        float scale = getResources().getDisplayMetrics().density;

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.US);
            }
        });

        timer.start();

    }



    //create a timer to move on after a few seconds
    Thread timer = new Thread() {
        public void run() {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Intent newScreen = new Intent(Splash.this, MainActivity.class);
                startActivity(newScreen);
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //create the TTS
                tts = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    // Implement onInitListener interface to confirm TTS is downloaded and enabled.
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    private void speakWords(String speech) {
        tts.speak(speech, TextToSpeech.QUEUE_ADD, null, null);
    }


    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }



}
