package com.example.warrencheung.cameracontroller;

import android.os.AsyncTask;
import android.os.Handler;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by WarrenCheung on 6/3/2016.
 */
public class Ssh {

    public void captureImage(String name, String password, String host, int port, String command,CallBack callBack){
        try {
            new Capture(name, password, host, port, command, callBack).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void sendCommand(String name, String password, String host, int port, String command, CallBack callBack) {
        try {
            new CommandSender(name, password, host, port, command, callBack).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class Capture extends AsyncTask<Void, Void, String> {
        String name;
        String pw;
        String host;
        CallBack callBack;
        int port;
        String command;
        String fileName;


        public Capture(String name, String password, String host, int port, String command, CallBack callBack) {
            this.name = name;
            this.pw = password;
            this.host = host;
            this.port = port;
            this.callBack = callBack;
            this.command = command;
        }

        protected String doInBackground(Void... params) {
            String returned = null;

            try {

                JSch jsch = new JSch();
                Session session = jsch.getSession(name, host, port);
                session.setPassword(pw);

                // Avoid asking for key confirmation
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);

                session.connect();

                // SSH Channel
                ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                channelssh.setOutputStream(baos);

                // Execute command
                channelssh.setCommand(command);
                //channelssh.setCommand("ls");
                channelssh.connect();

                //wait for command to be done
                while(channelssh.isClosed() == false){
                    Thread.sleep(50);
                }

                channelssh.disconnect();
                returned = baos.toString();
                fileName = Utils.getLastLine(returned);

                //SFTP CODE for getting image
                ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();
                //change directory
                //channel.cd("/home/pi/Pictures");
                // get
                channel.get("/home/pi/Pictures/" + fileName, "/storage/emulated/0/Download/" + fileName);

                channel.disconnect();
                session.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return fileName;
            //return returned;
        }

        @Override
        protected void onPostExecute(String returned) {
            callBack.done(returned);
            super.onPostExecute(returned);
        }
    }

    //ssh send command
    public class CommandSender extends AsyncTask<Void, Void, String> {
        String name;
        String pw;
        String host;
        int port;
        CallBack callBack;
        String command;

        public CommandSender(String name, String password, String host, int port, String command, CallBack callBack) {
            this.name = name;
            this.pw = password;
            this.host = host;
            this.port = port;
            this.callBack = callBack;
            this.command = command;
        }

        protected String doInBackground(Void... params) {
            String returned = null;

            try {

                JSch jsch = new JSch();
                Session session = jsch.getSession(name, host, port);
                session.setPassword(pw);

                // Avoid asking for key confirmation
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);

                session.connect();

                // SSH Channel
                ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                channelssh.setOutputStream(baos);

                // Execute command
                channelssh.setCommand(command);
                channelssh.connect();

                channelssh.disconnect();
                returned = baos.toString();
                session.disconnect();

            } catch (JSchException e) {
                e.printStackTrace();
            }

            return returned;
        }

        @Override
        protected void onPostExecute(String returned) {
            callBack.done(returned);
            super.onPostExecute(returned);
        }
    }
}