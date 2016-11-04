package com.vthomas.flightquery;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.JsonElement;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AIListener{
    String[] perms = {"android.permission.RECORD_AUDIO"};
    int permsRequestCode = 200;

    private AIService aiService;
    private Button listenButton;
    private TextView resultTextView;



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

        //request permission for recording audio if not yet granted
        if(!hasPermission(perms[0])){
            requestPermissions(perms, permsRequestCode);
        }

    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        //Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()){
            for(final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()){
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ")";
            }
        }

        //Show results in TextView
        resultTextView.setText("Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString);
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
}
