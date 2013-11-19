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
    // Bluetooth Stuff, tworzone sa obiekty odpowiedzialne za komunikacje
    private BluetoothAdapter btadapter;
    private BluetoothSocket btsocket;
    private OutputStream outstream;
    public Handler handler;

    String address;
    boolean cstatus;

    //zadany UUID
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectingThread(String MACaddr, BluetoothAdapter btadapter ,OutputStream outstream, Handler handler){
        this.address = MACaddr;
        this.cstatus = true;
        this.btadapter=btadapter;
        this.outstream=outstream;
        this.handler= handler;
    }

    @Override
    public void run(){
        try{
            BluetoothDevice btdevice = btadapter.getRemoteDevice(address);
            try{
                btsocket = btdevice.createRfcommSocketToServiceRecord(SPP_UUID);

            } catch(IOException e){
                this.cstatus = false;
            }
        } catch (IllegalArgumentException e){
            this.cstatus = false;
        }
        btadapter.cancelDiscovery();
        try {
            btsocket.connect();
        }catch (IOException e1){
            try {
                btsocket.close();
            } catch (IOException e2){
            }
        }
        //data stream do rozmowy
        try {
            outstream = btsocket.getOutputStream();
        } catch (IOException e2){
            this.cstatus = false;
        }
        if (this.cstatus){
            handler.sendEmptyMessage(1);
        } else {
            handler.sendEmptyMessage(0);
        }
    }

}
