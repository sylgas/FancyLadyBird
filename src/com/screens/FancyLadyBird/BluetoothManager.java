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
 * Date: 19.11.13
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class BluetoothManager {
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket= null;
    private final static BluetoothManager instance= new BluetoothManager();

    private BluetoothManager() {}

    public static BluetoothManager getManager() {
         return instance;
    }

    /**
     * potrzebny do komunikacji
     */
    private OutputStream outputStream= null;
    public Handler handler;

    private String MACaddress;
    private boolean connectStatus;
    private boolean handlingMsg= false;

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setMACaddress(String MACaddress) {
        this.MACaddress = MACaddress;
    }

    public void setConnectStatus(boolean connectStatus) {
        this.connectStatus = connectStatus;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public Handler getHandler() {
        return handler;
    }

    public String getMACaddress() {
        return MACaddress;
    }

    public boolean isConnectStatus() {
        return connectStatus;
    }

    public void connect() {
        BluetoothManager.getManager().setConnectStatus(Boolean.TRUE);
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
            BluetoothManager.getManager().setConnectStatus(Boolean.FALSE);
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

    public void setHandlingMsg(boolean handlingMsg) {
        this.handlingMsg = handlingMsg;
    }

    public boolean isHandlingMsg() {
        return handlingMsg;
    }
}
