package com.screens.FancyLadyBird;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity {
    /**
     * connect device flag
     */
    public static final int CONNECT_DEVICE_REQ = 1;

    /**
     * enable bluetooth flag
     */
    public static final int ENABLE_BLUETOOTH_REQ= 2;

    /**
     * user's name for greeting 
     */
    public static final String USER_NAME= "com.example.FancyLadyBird.USER";

    /**
     * if device is connected
     */
    private boolean connected = false;

    private byte output;

    private ConnectingThread connectingThread = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        BluetoothManager.getManager().setBluetoothAdapter( BluetoothAdapter.getDefaultAdapter());

        final Toast toast = Toast.makeText(this, "Brak połączenia", Toast.LENGTH_SHORT);

        Handler handler = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    setconnected(Boolean.TRUE);
                    setOutput((byte) 0);
                } else {
                    toast.show();
                }
            }
        };

        BluetoothManager.getManager().setHandler(handler);
    }

    public void findDevices(View view){
        //pobranie user name i przekazanie do kolejnego activity

        final Intent deviceIntent = new Intent(this, FindDeviceActivity.class);

        EditText editText= (EditText) findViewById(R.id.userName);
        String message= editText.getText().toString();
        deviceIntent.putExtra(USER_NAME, message);
        startActivity(deviceIntent);
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case CONNECT_DEVICE_REQ:
                if (resultCode == Activity.RESULT_OK){
                    BluetoothManager.getManager().setMACaddress(data.getExtras().getString(FindDeviceActivity.EXTRA_DEVICE_ADDRESS));
                    connectingThread = new ConnectingThread(BluetoothManager.getManager() );
                    new Thread (connectingThread).start();
                } else {
                    Toast.makeText(this, "mac failed", Toast.LENGTH_SHORT).show();
                }
                break;
            case ENABLE_BLUETOOTH_REQ:
                if (resultCode == Activity.RESULT_OK){
                } else {
                    Toast.makeText(this, "bt disabled", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }*/

    public void setconnected(boolean connected) {
        this.connected = connected;
    }

    public void setOutput(byte output) {
        this.output = output;
    }

    public void finishActivity(View view){
        this.finish();
    }
}
