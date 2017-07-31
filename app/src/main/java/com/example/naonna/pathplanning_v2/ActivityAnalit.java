package com.example.naonna.pathplanning_v2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by naonna on 10.06.17.
 */

public class ActivityAnalit extends Activity{

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    CheckBox start;
    CheckBox finish;
    EditText firstX;
    EditText firstY;
    EditText secondX;
    EditText secondY;
    LinearLayout llLineParams;
    LinearLayout llPoints;
    EditText etFer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.analit);


        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAnalit.this);
        builder.setTitle("Важливо!")
                .setMessage(Html.fromHtml("<font color=\"red\">1. Прийміть до уваги те, що розміри задаються у пікселях. Координата (0,0) знаходиться у верхньому лівому кутку!<br>" +
                        "2. Будьте уважні при введені значень, адже додані ребра змінювати чи видаляти не можна.</font><br>" +

                        "3. Попарно введіть координати першої і другої вершин.<br>4. Щоб зробити вершину початковою - поставте галочку біля першої вершини; " +
                        "цільовою - біля другої вершини.<br>" +
                        "5. При включеному режимі введення феромонів, якщо залишити поле значення кількості феромону пустим, буде використано значення за замовчуванням." ))
                .setCancelable(false)
                .setNegativeButton("Зрозуміло",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();





        if (Settings.getInstanse().isFeromons()){

            llLineParams = (LinearLayout) findViewById(R.id.llLineParams);

            etFer = new EditText(this);
            etFer.setHint("Феромони");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            etFer.setLayoutParams(lp);
            llLineParams.addView(etFer);
        }
        Settings.getInstanse().setScale(5);

        ListView listView = (ListView) findViewById(R.id.listView);


        adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.tv, arrayList);
        listView.setAdapter(adapter);



        start = (CheckBox) findViewById(R.id.cbS);
        finish = (CheckBox) findViewById(R.id.cbF);
        firstX = (EditText) findViewById(R.id.pointFirstX);
        firstY = (EditText) findViewById(R.id.pointFirstY);
        secondX = (EditText) findViewById(R.id.pointSecondX);
        secondY = (EditText) findViewById(R.id.pointSecondY);



    }

    Point p1;
    Point p2;
    float length;
    float feromons = 2;
    Point startPoint;
    Point finishPoint;
    ArrayList<Point> pointList = new ArrayList<>();
    ArrayList<Line> lineList = new ArrayList<>();
//    float maxX=0;
//    float maxY=0;
//    float minX=1000000;
//    float minY=1000000;

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnAdd :
                if(firstX.getText().toString().isEmpty() || firstY.getText().toString().isEmpty() ||
                        secondX.getText().toString().isEmpty() || secondY.getText().toString().isEmpty()) {
                    break;
                }

                StringBuilder sb = new StringBuilder();
//
//                if(maxX<Float.parseFloat(firstX.getText().toString()) || maxX<Float.parseFloat(secondX.getText().toString()) ){
//                    maxX = Math.max(Float.parseFloat(firstX.getText().toString()), Float.parseFloat(secondX.getText().toString()));
//                }
//                if(maxY<Float.parseFloat(firstY.getText().toString()) || maxX<Float.parseFloat(secondY.getText().toString()) ){
//                    maxX = Math.max(Float.parseFloat(firstY.getText().toString()), Float.parseFloat(secondY.getText().toString()));
//                }
//                if(minX>Float.parseFloat(firstX.getText().toString()) || maxX>Float.parseFloat(secondX.getText().toString()) ){
//                    minX = Math.min(Float.parseFloat(firstX.getText().toString()), Float.parseFloat(secondX.getText().toString()));
//                }
//                if(minY>Float.parseFloat(firstY.getText().toString()) || minY>Float.parseFloat(secondY.getText().toString()) ){
//                    minY = Math.min(Float.parseFloat(firstY.getText().toString()), Float.parseFloat(secondY.getText().toString()));
//                }



                sb.append("[("+firstX.getText().toString()+","+firstY.getText().toString()+"),("+secondX.getText().toString()+","+secondY.getText().toString()+")]. ");


                p1 = new Point(Float.parseFloat(firstX.getText().toString()), Float.parseFloat(firstY.getText().toString()));
                p2 = new Point(Float.parseFloat(secondX.getText().toString()), Float.parseFloat(secondY.getText().toString()));
                length = (float) Math.sqrt(Math.pow((p1.getX() - p2.getX()),2) + Math.pow((p1.getY() - p2.getY()),2));

                if(Settings.getInstanse().isFeromons()){
                    if (!etFer.getText().toString().isEmpty()){
                        feromons = Float.parseFloat(etFer.getText().toString());
                    }

                }
                if (! pointList.contains(p1)) {
                    pointList.add(p1);
                }
                if (! pointList.contains(p2)) {
                    pointList.add(p2);
                }
                lineList.add(new Line(p1, p2, length, feromons));

                if (start.isChecked()){
                    startPoint = new Point(Float.parseFloat(firstX.getText().toString()), Float.parseFloat(firstY.getText().toString()));
                    sb.append("Початкова вершина: " + "("+firstX.getText().toString()+","+firstY.getText().toString()+"). ");
                }
                if (finish.isChecked()){
                    finishPoint = new Point(Float.parseFloat(secondX.getText().toString()), Float.parseFloat(secondY.getText().toString()));
                    sb.append("Цільова вершина: " + "("+secondX.getText().toString()+","+secondY.getText().toString()+"). ");
                }
                arrayList.add(sb.toString());
                start.setChecked(false);
                finish.setChecked(false);
                firstX.setText("");
                firstY.setText("");
                secondX.setText("");
                secondY.setText("");

                adapter.notifyDataSetChanged();
                break;

            case R.id.btnStart :

                Point p1 = new Point(200, 300);
                Point p2 = new Point(800, 180);
                Point p3 = new Point(150, 600);
                Point p4 = new Point(190, 860);
                Point p5 = new Point(875, 940);
                Point p6 = new Point(950, 580);
                Point p7 = new Point(485, 620);

                pointList.add(p1);
                pointList.add(p2);
                pointList.add(p3);
                pointList.add(p4);
                pointList.add(p5);
                pointList.add(p6);
                pointList.add(p7);

                Line l1 = new Line(p1, p2);
                Line l2 = new Line(p1, p3);
                Line l3 = new Line(p3, p4);
                Line l4 = new Line(p4, p5);
                Line l5 = new Line(p5, p6);
                Line l6 = new Line(p6, p2);
                Line l7 = new Line(p2, p7);
                Line l8 = new Line(p6, p7);
                Line l9 = new Line(p5, p7);
                Line l10 = new Line(p4, p7);
                Line l11 = new Line(p3, p7);

                lineList.add(l1);
                lineList.add(l2);
                lineList.add(l3);
                lineList.add(l4);
                lineList.add(l5);
                lineList.add(l6);
                lineList.add(l7);
                lineList.add(l8);
                lineList.add(l9);
                lineList.add(l10);
                lineList.add(l11);

                startPoint = p1;
                finishPoint = p7;






               if (startPoint==null || finishPoint==null) {
                   Toast.makeText(this, "Не обрана початкова або цільова вершина!", Toast.LENGTH_SHORT).show();
                   break;
               }
                DrawResult draw = new DrawResult(this);
                draw.startAlgorithm();
                setContentView(draw);

                break;
        }
    }




    class DrawResult extends View {

        Paint mPaint;
//        float xMax;
//        float yMax;
//        float xMin;
//        float yMin;

        ACO aco;
        ArrayList<Line> path;

        final int [] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
                99999999, 999999999, Integer.MAX_VALUE };


        public DrawResult(Context context) {
            super(context);
            setBackgroundColor(Color.WHITE);
            setFocusable(true);
            setFocusableInTouchMode(true);
            mPaint = new Paint();
//            this.xMax = maxX+minX;
//            this.yMax = maxY+minY;
//            this.xMin = 0;
//            this.yMin = 0;

        }




        protected void startAlgorithm(){
            aco = new ACO(lineList,pointList, startPoint,finishPoint);
            aco.execute();
            try {
                path = aco.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            postInvalidate();
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);



//            canvas.save();
//            float width = canvas.getWidth();
//            float height = canvas.getHeight();
//            canvas.scale(width / (xMax - xMin), height / (yMax - yMin));
//////            canvas.translate(-xMin + 0.2F, -yMax + 0.2F);


//            int forX = canvas.getWidth() / (int) (xMax - xMin) / (int) (xMax - xMin);
//            int forY = canvas.getHeight() / (int) (yMax - yMin) / (int) (yMax - yMin);


            int size = 0;

            size = 5;

            mPaint.setColor(Color.BLACK);

            for (Point p : pointList) {
                canvas.drawCircle(p.getX(), p.getY(), size, mPaint);
//                canvas.drawCircle(p.getX() * forX, p.getY() * forY, size, mPaint);
                System.out.println("POINT FROM DRAW " + p.getX() + ", " + p.getY());
            }

            mPaint.setStrokeWidth(size - 10);
            for (Line l : lineList) {
                canvas.drawLine(l.getPointFirst().getX(), l.getPointFirst().getY(), l.getPointSecond().getX(), l.getPointSecond().getY(), mPaint);
//                canvas.drawLine(l.getPointFirst().getX() * forX, l.getPointFirst().getY()* forY, l.getPointSecond().getX() * forX, l.getPointSecond().getY()* forY, mPaint);
            }

            if (path!=null){
                mPaint.setColor(Color.RED);
                for(Line l : path){
                    canvas.drawCircle(l.getPointFirst().getX(), l.getPointFirst().getY(), size, mPaint);
                    canvas.drawCircle(l.getPointSecond().getX(), l.getPointSecond().getY(), size, mPaint);
                    canvas.drawLine(l.getPointFirst().getX(), l.getPointFirst().getY(), l.getPointSecond().getX(), l.getPointSecond().getY(), mPaint);
//                    canvas.drawCircle(l.getPointFirst().getX()*forX, l.getPointFirst().getY()*forY, size, mPaint);
//                    canvas.drawCircle(l.getPointSecond().getX()*forX, l.getPointSecond().getY()*forY, size, mPaint);
//                    canvas.drawLine(l.getPointFirst().getX()*forX, l.getPointFirst().getY()*forY, l.getPointSecond().getX()*forX, l.getPointSecond().getY()*forY, mPaint);
                }

            }


        }


        int stringSize(int x) {
            for (int i=0; ; i++)
                if (x <= sizeTable[i])
                    return i+1;
        }

    }

}
