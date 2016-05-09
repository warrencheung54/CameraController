package com.example.warrencheung.cameracontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class Exposure extends AppCompatActivity {

    NumberPicker isoPicker;
    Spinner iso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exposure);

        //isoPicker = (NumberPicker) findViewById(R.id.isoPicker);
        iso = (Spinner) findViewById(R.id.iso);

        ArrayAdapter<CharSequence> isoAdapter = ArrayAdapter.createFromResource(this, R.array.isoSpeed, android.R.layout.simple_spinner_item);
        isoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iso.setAdapter(isoAdapter);
    }
}
