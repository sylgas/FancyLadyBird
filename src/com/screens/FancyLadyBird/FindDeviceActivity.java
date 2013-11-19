package com.screens.FancyLadyBird;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Sylgas
 * Date: 14.11.13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class FindDeviceActivity extends Activity {
    private static final String WELCOME_MESSAGE= "Witaj !";
    ArrayAdapter<String> devices;

    //Parametry potrzebne przy komunikacji
    protected static final String EXTRA_DEVICE_ADDRESS = "device_address";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_devices);

        TextView textView= (TextView) this.findViewById(R.id.textViewWelcomeMessage);
        if (getIntent().getStringExtra(MainActivity.USER_NAME) != null) {
            textView.setText(WELCOME_MESSAGE.replace(" ", " " + getIntent().getStringExtra(MainActivity.USER_NAME)));
        }

        ProgressBar progressBar= (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        //getUserBluetooth();
        //listy urzadzen
         devices = new ArrayAdapter<String>(this, R.layout.devices_list);
         findDevices();

        //stworzenie listview dla urzadzen
         ListView listView = (ListView) this.findViewById(R.id.listViewDevices);
         //listView.setAdapter(devices);
         //listView.setOnItemClickListener(clicklistener);



        //nowe
//        ListView nlv = (ListView) this.findViewById(R.id.listView2);
//        nlv.setAdapter(this.newdevices);
//        nlv.setOnItemClickListener(clicklistener);
//

// gdy urzadzenie zostanie odkryte, rejestracja
        /* IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
         this.registerReceiver(bluetoothReceiver, filter);

       //koniec odkrywania urzadzen
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
       this.registerReceiver(bluetoothReceiver, filter);

        TextView textView2= (TextView) this.findViewById(R.id.textViewFindingDevices);
        textView2.setText(R.string.done_finding_devices_msg);

        progressBar.setVisibility(View.GONE);*/


       /* BluetoothManager.getManager().getBluetoothAdapter() = BluetoothAdapter.getDefaultAdapter();

        //pobranie aktualnie powiazanych
        Set<BluetoothDevice> paireddevicesset = BluetoothManager.getManager().getBluetoothAdapter().getBondedDevices();
        if (paireddevicesset.size() > 0){
            for (BluetoothDevice device : paireddevicesset){
                this.devices.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            this.devices.add("no devices");
        }*//*

*/
    }

    //reakcja na klikniecie danego urzadzenia
    AdapterView.OnItemClickListener clicklistener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3){
            BluetoothManager.getManager().getBluetoothAdapter().cancelDiscovery();
            //get mac
            String info = ((TextView) v).getText().toString();
            try {
                //extract mac
                String address = info.substring(info.length() - 17);

                //powrot do glownego widoku z intent
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

                setResult(Activity.RESULT_OK, intent);
                finish();
            } catch (IndexOutOfBoundsException e){
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
    };

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
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    devices.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.find_devices_msg);
                if (devices.getCount() == 0){
                    String noDevices = "not found";
                   devices.add(noDevices);
                }
            }
        }
    };

    //obsluga klikniecia przycisku scan - wyszukiwanie nowych urzadze� w poblizu
    public void findDevices(){
        BluetoothManager.getManager().setBluetoothAdapter( BluetoothAdapter.getDefaultAdapter());

        if(BluetoothManager.getManager().getBluetoothAdapter() == null){
            Toast.makeText(this, "Bluetooth nie jest obslugiwany", Toast.LENGTH_LONG).show();
            this.finish();
            return;
        }

        //wymaganie w��czenia bt jezeli nie jest
        if(!BluetoothManager.getManager().getBluetoothAdapter().isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, MainActivity.ENABLE_BLUETOOTH_REQ);
        }

        BluetoothManager.getManager().getBluetoothAdapter().startDiscovery(); //start szukania
    }

    public void finishActivity( View view) {
        finish();
    }
}