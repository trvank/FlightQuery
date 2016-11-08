package com.vthomas.flightquery;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AIListener, TextToSpeech.OnInitListener {
    String[] perms = {"android.permission.RECORD_AUDIO"};
    int permsRequestCode = 200;

    private TextToSpeech tts = null;
    private int MY_DATA_CHECK_CODE = 0;

    private AIService aiService;
    private QueryResult q = new QueryResult();
    private Button listenButton;
    private TextView resultTextView;
    private TextView queryTextView;

    private ArrayList<String> actions_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AIConfiguration config = new AIConfiguration("bba91e5348ff4dc7a964696d4daae63a",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        listenButton = (Button) findViewById(R.id.listenButton);
        resultTextView = (TextView) findViewById(R.id.resultText);
        queryTextView = (TextView) findViewById(R.id.queryText);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.US);
            }
        });

        //request permission for recording audio if not yet granted
        if(!hasPermission(perms[0])){
            requestPermissions(perms, permsRequestCode);
        }

        actions_list = set_actions();

    }

    //TTS Stuff
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
//            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    private void speakWords(String speech) {
        tts.speak(speech, TextToSpeech.QUEUE_ADD, null, null);
    }

    //AI.API stuff
    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
//        Map<String, JsonElement> map = new HashMap<String, JsonElement>();

        //Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()){
            for(final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()){
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ")";
            }
        }

        if((actions_list.contains(result.getAction()))){
            q = Analyzer.airline(result.getParameters(), result.getAction());
        }
        else{
            q.set_text_string("I may have misunderstood the query.");
            q.set_speech_string("I may have misunderstood the query. " +
                    "Do you have a question about todays flights that I can help you with?");
        }

        //Show results in TextView
        queryTextView.setText("Query: " + result.getResolvedQuery());
        resultTextView.setText("\n\nAction: " + result.getAction() +
                "\n\nParameters: " + parameterString +
                "\n\n text " + q.get_text_string() +
                "\n\n speech " + q.get_speech_string());

        // And speak it
        speakWords(q.get_speech_string());
    }

    @Override
    public void onError(AIError error) {
        resultTextView.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    //action for the listen button
    public void listen(View view) {
        aiService.startListening();
    }

    //does the device need record permission???
    private boolean needsPermission(){
        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean hasPermission(String permission){
        if(needsPermission()){
            return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }


    public void send(View view) {


    }

    private ArrayList<String> set_actions(){
        ArrayList<String> list = new ArrayList<>();
        list.add("airline");
        list.add("arrive_time");
        list.add("arrive_city");
        list.add("depart_time");
        list.add("depart_city");
        list.add("flight");
        list.add("status");
        return list;
    }
}