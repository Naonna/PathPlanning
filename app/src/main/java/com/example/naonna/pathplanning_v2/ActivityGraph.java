package com.example.naonna.pathplanning_v2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by naonna on 10.06.17.
 */

public class ActivityGraph extends Activity{

    Draw draw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityGraph.this);
        builder.setTitle("Важливо!")
                .setMessage(Html.fromHtml("<font color=\"red\">1. Приступайте до обрання початкової і цільової вершин тільки після завершення створення графу!<br></font>" +

                        "2. Розтавте вершини на екрані. <br> 3. Для створення ребра попарно з'єднайте дві вершинини. <br> " +
                        "3. Якщо залишити поле значення довжини ребра, значення довжини ребра буде рівним відстані між точками, що його утворюють.<br>" +
                        "4. При включеному режимі введення феромонів, якщо залишити поле значення кількості феромону пустим, буде використано значення за замовчуванням. <br>" +
                        "5. У верхній частині екрану відображаються підказки."))
                .setCancelable(false)
                .setNegativeButton("Зрозуміло",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();


        draw = new Draw(this);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayut = (FrameLayout) findViewById(R.id.drawLayout);
        frameLayut.addView(draw);
    }

    public void onClick(View view){
        TextView tv = (TextView) findViewById(R.id.tv);
        switch (view.getId()){
            case R.id.btnStart:
                Log.d("tag", "onClick: start ");
                tv.setText("Оберіть початкову точку");
                draw.status = Draw.Status.START;
                break;
            case R.id.btnEnd:
                tv.setText("Оберіть кінцеву точку");
                draw.status = Draw.Status.FINISH;
                break;
            case R.id.btnAlgorithm:
                tv.setText("Оптимальний шлях");
                if (draw.start == null || draw.finish == null){
                    tv.setText("Не обрано початкову чи кінцеву вершуну!");
                    tv.setTextColor(Color.RED);
                    break;
                }
                draw.startAlgorithm();
                Button btnS = (Button) findViewById(R.id.btnStart);
                Button btnE = (Button) findViewById(R.id.btnEnd);
                Button btnA = (Button) findViewById(R.id.btnAlgorithm);
                btnS.setVisibility(View.INVISIBLE);
                btnE.setVisibility(View.INVISIBLE);
                btnA.setVisibility(View.INVISIBLE);
                break;
        }

    }
}
