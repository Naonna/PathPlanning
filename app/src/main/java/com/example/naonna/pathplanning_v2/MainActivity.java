package com.example.naonna.pathplanning_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    Draw draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }


    public void onClick(View view){

        Intent intent;

        switch (view.getId()){
            case R.id.btnGraph :
                intent = new Intent(this, ActivityGraph.class);
                startActivity(intent);
                break;
            case R.id.btnAnalit :
                intent = new Intent(this, ActivityAnalit.class);
                startActivity(intent);
                break;
            case R.id.btnSettings :
                intent = new Intent(this, ActivitySettings.class);
                startActivity(intent);
                break;
        }

    }

}
