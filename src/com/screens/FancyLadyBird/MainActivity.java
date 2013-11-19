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
    private Intent deviceIntent;


    // Bluetooth Stuff, tworzone sa obiekty odpowiedzialne za komunikacje
    private BluetoothAdapter btadapter = null;
    private OutputStream outstream = null;
    private ConnectingThread connectthread = null;
    public Handler handler;

    //Parametry potrzebne przy komunikacji
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int ENABLE_BLUETOOTH_REQ= 2;

    public static final String USER_NAME= "com.example.FancyLadyBird.USER";

    boolean connectstat = false;
    byte output;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.deviceIntent = new Intent(this, FindDeviceActivity.class);
        EditText editText= (EditText) findViewById(R.id.edit_message);
        String message= editText.getText().toString();
        this.deviceIntent.putExtra(USER_NAME, message);

        btadapter = BluetoothAdapter.getDefaultAdapter();
        final Toast toast = Toast.makeText(this, "CONNECTION FAILED", Toast.LENGTH_SHORT);

        handler = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    connectstat = true;
                    output = 0;
                } else {
                    toast.show();
                }
            }
        };

    }

    public void findDevices(View view) {
        if (this.connectstat){
            //disconnect
            if (outstream != null){
                try{
                    outstream.close();
                    connectstat = false;
                } catch (IOException e){}
            }
        } else {
            startActivityForResult(deviceIntent, REQUEST_CONNECT_DEVICE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK){
                    String deviceaddress = data.getExtras().getString(FindDeviceActivity.EXTRA_DEVICE_ADDRESS);
                    connectthread = new ConnectingThread(deviceaddress, btadapter, outstream, handler );
                    new Thread (connectthread).start();
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
    }
}
