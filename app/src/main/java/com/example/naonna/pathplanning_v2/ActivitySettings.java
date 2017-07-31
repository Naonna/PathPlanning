package com.example.naonna.pathplanning_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by naonna on 10.06.17.
 */

public class ActivitySettings extends Activity {

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        final Settings settings = Settings.getInstanse();

        final EditText etALpha  = (EditText) findViewById(R.id.etALpha);
        final EditText etBeta = (EditText) findViewById(R.id.etBeta);
        final EditText etTau = (EditText) findViewById(R.id.etTau);
        final EditText etQ = (EditText) findViewById(R.id.etQ);
        final EditText etNumber = (EditText) findViewById(R.id.etNumber);
        final CheckBox chFeromons = (CheckBox) findViewById(R.id.chFeromons);

        Button btnOk = (Button) findViewById(R.id.btnOk);
        intent = new Intent(this,  MainActivity.class);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etALpha.getText().toString().isEmpty()){
                    settings.setALPHA(Float.parseFloat(etALpha.toString()));
                }
                if (!etBeta.getText().toString().isEmpty()){
                    settings.setBETA(Float.parseFloat(etBeta.toString()));
                }
                if (!etTau.getText().toString().isEmpty()){
                    settings.setTAU(Float.parseFloat(etTau.toString()));
                }
                if (!etQ.getText().toString().isEmpty()){
                    settings.setQ(Integer.parseInt(etQ.toString()));
                }
                if (!etNumber.getText().toString().isEmpty()){
                    settings.setNumberOfagents(Integer.parseInt(etNumber.toString()));
                }
                settings.setFeromons(chFeromons.isChecked());
                System.out.println("IS CHECKED "+ chFeromons.isChecked());

                startActivity(intent);


            }
        });
    }
}
