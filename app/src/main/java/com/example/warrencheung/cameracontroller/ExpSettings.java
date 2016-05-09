package com.example.warrencheung.cameracontroller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by WarrenCheung on 5/5/2016.
 */
public class ExpSettings {

    private Context context;

    public ExpSettings(Context context){
        this.context = context;
    }

    public void captureMode(final String address, final Ssh ssh, final String USERNAME, final String PASSWORD, final int PORT){
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Capture Mode");
        final String[] modes = context.getResources().getStringArray(R.array.captureMode);
        b.setItems(modes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String client_mac_fixed = new String(address).replace("99", "19");
                String clientIP = Utils.getIPFromMac(client_mac_fixed);
                ssh.sendCommand(USERNAME, PASSWORD, clientIP, PORT, "gphoto2 --set-config expprogram="+modes[which], new CallBack() {
                            @Override
                            public void done(String returned) {
                            }
                        });
                }
        });
        b.show();
    }

    public void isoSetting(final String address, final Ssh ssh, final String USERNAME, final String PASSWORD, final int PORT){
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Select the ISO speed");
        final String[] iso = context.getResources().getStringArray(R.array.isoSpeed);
        b.setItems(iso, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String client_mac_fixed = new String(address).replace("99", "19");
                String clientIP = Utils.getIPFromMac(client_mac_fixed);
                ssh.sendCommand(USERNAME, PASSWORD, clientIP, PORT, "gphoto2 --set-config iso=" + iso[which], new CallBack() {
                    @Override
                    public void done(String returned) {
                    }
                });
            }
        });

        b.show();
    }

    public void shutterSpeedSettings(final String address, final Ssh ssh, final String USERNAME, final String PASSWORD, final int PORT){
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Select the Shutter Speed");
        final String[] ss = context.getResources().getStringArray(R.array.shutterSpeed);
        b.setItems(ss, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String client_mac_fixed = new String(address).replace("99", "19");
                String clientIP = Utils.getIPFromMac(client_mac_fixed);
                ssh.sendCommand(USERNAME, PASSWORD, clientIP, PORT, "gphoto2 --set-config shutterspeed=" + ss[which], new CallBack() {
                    @Override
                    public void done(String returned) {
                    }
                });
            }
        });

        b.show();
    }

    public void apertureSetting(final String address, final Ssh ssh, final String USERNAME, final String PASSWORD, final int PORT){
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Select the Shutter Speed");
        final String[] apertureSet = context.getResources().getStringArray(R.array.apertureSet);
        final String[] aperture = context.getResources().getStringArray(R.array.aperture);
        b.setItems(apertureSet, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String client_mac_fixed = new String(address).replace("99", "19");
                String clientIP = Utils.getIPFromMac(client_mac_fixed);

                ssh.sendCommand(USERNAME, PASSWORD, clientIP, PORT, "gphoto2 --set-config 5007=" + aperture[which], new CallBack() {
                    @Override
                    public void done(String returned) {
                    }
                });
            }
        });

        b.show();
    }
}
