package com.screens.FancyLadyBird;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sylgas
 * Date: 19.11.13
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class GameWindowActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_window);


        TextView textView= (TextView) this.findViewById(R.id.textViewConnectedWith);
        textView.setText("Połączono z " + getIntent().getStringExtra(FindDeviceActivity.DEVICE_NAME));


    }

    public void finishActivity(View view) {
        if (BluetoothManager.getManager().isHandlingMsg()){
            //disconnect
            if (BluetoothManager.getManager().getOutputStream() != null){
                try{
                    BluetoothManager.getManager().getOutputStream().close();
                    BluetoothManager.getManager().setHandlingMsg(Boolean.FALSE);
                } catch (IOException e){}
            }
        }
        finish();
    }

    private void sendData(String message) {
        message= "$0" + message + "$$";
        byte[] msgBuffer = message.getBytes();
        try {
            BluetoothManager.getManager().getOutputStream().write(msgBuffer);
        } catch (IOException e) {

        }
    }

    private void goUp(View view){
        sendData("1");
    }

    private void goDown(View view){
        sendData("2");
    }

    private void goRight(View view){
        sendData("3");
    }

    private void goLeft(View view){
        sendData("4");
    }

}