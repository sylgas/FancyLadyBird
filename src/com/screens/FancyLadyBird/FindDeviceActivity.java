package com.screens.FancyLadyBird;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;


/**
 * @ Sylgas
 */
public class FindDeviceActivity extends Activity {
    /**
     * welcome message
     */
    private static final String WELCOME_MESSAGE= "Witaj !";

    /**
     * set of devices finded by bluetooth
     */
    ArrayAdapter<String> devices;

    /**
     * progressBar enabled while searching
     */
    private ProgressBar progressBar;

    public static final String DEVICE_NAME= "com.example.FancyLadyBird.DEVICE_NAME";

    /**
     * if device is connected
     */

    private byte output;


    /**
     * enable bluetooth flag
     */
    public static final int ENABLE_BLUETOOTH_REQ= 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_devices);

        BluetoothManager.getManager().setBluetoothAdapter( BluetoothAdapter.getDefaultAdapter());

        final Toast toast = Toast.makeText(this, "Nie udało się nawiązać połączenia", Toast.LENGTH_SHORT);

        Handler handler = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    BluetoothManager.getManager().setHandlingMsg(Boolean.TRUE);
                    setOutput((byte) 0);
                } else {
                    toast.show();
                }
            }
        };

        BluetoothManager.getManager().setHandler(handler);

        TextView textView= (TextView) this.findViewById(R.id.textViewWelcomeMessage);
        if (getIntent().getStringExtra(MainActivity.USER_NAME) != null) {
            textView.setText(WELCOME_MESSAGE.replace(" ", " " + getIntent().getStringExtra(MainActivity.USER_NAME)));
        }

        this.progressBar= (ProgressBar) this.findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.VISIBLE);

        //listy urzadzen
        this.devices = new ArrayAdapter<String>(this, R.layout.devices_list);

        //stworzenie listview dla urzadzen
         ListView listView = (ListView) this.findViewById(R.id.listViewDevices);
         listView.setAdapter(devices);
         listView.setOnItemClickListener(clicklistener);


        // gdy urzadzenie zostanie odkryte, rejestracja
         IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
         this.registerReceiver(bluetoothReceiver, filter);

       //koniec odkrywania urzadzen
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(bluetoothReceiver, filter);

        findDevices();

    }

    // BroadcastReceiver nasluchujacy na odkrywane urzadzenia
    BroadcastReceiver bluetoothReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            //po znalezieniu device
            if (BluetoothDevice.ACTION_FOUND.equalsIgnoreCase(action)){
                //pobierz bt device z intentu
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //jezeli juz jest to skip
                if(devices.getPosition(device.getName() + "\n" + device.getAddress()) == -1){
                    devices.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                TextView textView2= (TextView) findViewById(R.id.textViewFindingDevices);
                textView2.setText(R.string.done_finding_devices_msg);
                progressBar.setVisibility(View.GONE);

                if (devices.getCount() == 0){
                    String noDevices = "Nie znaleziono żadnego urządzenia";
                    devices.add(noDevices);
                }
            }
        }
    };


    //reakcja na klikniecie danego urzadzenia
    AdapterView.OnItemClickListener clicklistener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3){
            BluetoothManager.getManager().getBluetoothAdapter().cancelDiscovery();
            //get mac
            String info = ((TextView) v).getText().toString();
            try {
                //extract mac
                String address = info.substring(info.length() - 17);
                BluetoothManager.getManager().setMACaddress(address);
                BluetoothManager.getManager().connect();

                if (BluetoothManager.getManager().isConnectStatus()) {
                    openGameWindow(info.substring(0, info.length() - 17));
                    finish();
                }

            } catch (IndexOutOfBoundsException e){
                toast("Mac jest za krótki");
            }
        }
    };



    //obsluga klikniecia przycisku scan - wyszukiwanie nowych urzadze� w poblizu
    public void findDevices(){
        if(BluetoothManager.getManager().getBluetoothAdapter() == null){
            toast("Bluetooth nie jest obslugiwany");
            this.finish();
            return;
        }

        //wymaganie w��czenia bt jezeli nie jest
        if(!BluetoothManager.getManager().getBluetoothAdapter().isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQ);
        }

        BluetoothManager.getManager().getBluetoothAdapter().startDiscovery(); //start szukania
    }

    public void finishActivity( View view) {
        finish();
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ENABLE_BLUETOOTH_REQ){
                if (resultCode == Activity.RESULT_OK){
                    BluetoothManager.getManager().getBluetoothAdapter().startDiscovery(); //start szukania
                } else {
                    toast("Bluetooth wyłączony");
                    finish();
                }
        }
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void openGameWindow(String deviceName) {
        final Intent deviceIntent = new Intent(this, GameWindowActivity.class);
        deviceIntent.putExtra(DEVICE_NAME, deviceName);
        startActivity(deviceIntent);
    }

    public void setOutput(byte output) {
        this.output = output;
    }


    public byte getOutput() {
        return output;
    }
}