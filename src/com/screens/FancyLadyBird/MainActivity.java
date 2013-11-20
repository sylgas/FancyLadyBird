package com.screens.FancyLadyBird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    /**
     * user's name for greeting 
     */
    public static final String USER_NAME= "com.example.FancyLadyBird.USER";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void findDevices(View view){
        //pobranie user name i przekazanie do kolejnego activity

        final Intent deviceIntent = new Intent(this, FindDeviceActivity.class);

        EditText editText= (EditText) findViewById(R.id.userName);
        String message= editText.getText().toString();
        deviceIntent.putExtra(USER_NAME, message);
        startActivity(deviceIntent);
    }

    public void finishActivity(View view){
        this.finish();
    }
}
