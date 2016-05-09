package com.example.warrencheung.cameracontroller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String HOST = "192.168.49.97";
    public static final String USERNAME = "pi";
    public static final String PASSWORD = "raspberry";
    public static String address = "";
    public static final int PORT = 22;
    public static String fileName = "";

    Button connect;
    Button capture;
    Button initialise;
    TextView statusText;
    ImageView imageView;
    Context context;
    Bitmap picture;
    final Ssh ssh = new Ssh();
    final ExpSettings expSettings = new ExpSettings(MainActivity.this);

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Deleting this picture");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                        if(fileName != null) {
                            Utils.delete(fileName);
                        }
                        statusText.setText(fileName + " Deleted!");
                        imageView.setImageDrawable(null);
                        //recreate();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return true;
            case R.id.captureMode:
                expSettings.captureMode(address, ssh, USERNAME, PASSWORD, PORT);
                return true;

            case R.id.isoSpeed:
                expSettings.isoSetting(address, ssh, USERNAME, PASSWORD, PORT);
                return true;

            case R.id.shutterSpeed:
                expSettings.shutterSpeedSettings(address, ssh, USERNAME, PASSWORD, PORT);
                return true;

            case R.id.aperture:
                expSettings.apertureSetting(address, ssh, USERNAME, PASSWORD, PORT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button) findViewById(R.id.connect);
        capture = (Button) findViewById(R.id.capture);
        statusText = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        context = this;
        //final Ssh ssh = new Ssh();
        final Commands commands = new Commands();

        //Resize picture
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        //File file = new File("/storage/emulated/0/DCIM/Camera/IMG_20151122_182334.jpg");
        //Picasso.with(imageView.getContext()).load(file).fit().into(imageView);
        //picture = BitmapFactory.decodeFile("/storage/emulated/0/Download/capt0000.jpg", options);
        //imageView.setImageBitmap(picture);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Connect.class));
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String client_mac_fixed = new String(address).replace("99", "19");
                String clientIP = Utils.getIPFromMac(client_mac_fixed);

                ssh.captureImage(USERNAME, PASSWORD, clientIP, PORT, commands.captureAndDisplay, new CallBack() {
                    @Override
                    public void done(String returned) {
                        if (returned == null) {
                        } else {
                            statusText.setText(returned + " Transferred!");
                            fileName = returned;
                            picture = BitmapFactory.decodeFile("/storage/emulated/0/Download/" + fileName, options);
                            imageView.setImageBitmap(picture);
                        }
                    }
                });
            }
        });

    }
}
