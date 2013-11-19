package com.screens.FancyLadyBird;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Sylgas
 * Date: 14.11.13
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class ConnectingThread implements Runnable {
    //zadany UUID
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    public ConnectingThread(){
        BluetoothManager.getManager().setConnectStatus(Boolean.TRUE);
    }

    @Override
    public void run(){
        try{
            BluetoothDevice bluetoothDevice = BluetoothManager.getManager().getBluetoothAdapter().getRemoteDevice(BluetoothManager.getManager().getMACaddress());
            try{
                BluetoothManager.getManager().setBluetoothSocket(bluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID));
            } catch(IOException e){
                BluetoothManager.getManager().setConnectStatus(Boolean.FALSE);
            }
        } catch (IllegalArgumentException e){
            BluetoothManager.getManager().setConnectStatus(Boolean.FALSE);
        }

        BluetoothManager.getManager().getBluetoothAdapter().cancelDiscovery();

        try {
            BluetoothManager.getManager().getBluetoothSocket().connect();
        }catch (IOException e1){
            try {
                BluetoothManager.getManager().getBluetoothSocket().close();
            } catch (IOException e2){
            }
        }

        try {
            BluetoothManager.getManager().setOutputStream( BluetoothManager.getManager().getBluetoothSocket().getOutputStream());
        } catch (IOException e2){
            BluetoothManager.getManager().setConnectStatus(Boolean.FALSE);
        }
        if (BluetoothManager.getManager().isConnectStatus()){
            BluetoothManager.getManager().getHandler().sendEmptyMessage(1);
        } else {
            BluetoothManager.getManager().getHandler().sendEmptyMessage(0);
        }
    }

}
