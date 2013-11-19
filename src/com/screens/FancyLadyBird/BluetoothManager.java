package com.screens.FancyLadyBird;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

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


}
