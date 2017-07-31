package com.example.naonna.pathplanning_v2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by naonna on 29.05.17.
 */

public class Draw extends View {


    static int width;
    static int height;

    public Draw(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        setFocusable(true);
        setFocusableInTouchMode(true);
        Settings.getInstanse().setScale(70);
    }


    private Paint mPaint = new Paint();

    float x, y;

    ArrayList<Point> pointList = new ArrayList<>();
    ArrayList<Line> lineList = new ArrayList<>();

    boolean secondTouch = false;

    Point p1;
    Point p2;

    Line l;


    Point start;
    Point finish;

    String TAG = "TAG";

    enum Status {
        START, FINISH, PAINTING
    }

    Status status = Status.PAINTING;

    int type=2;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = canvas.getWidth();
        height = canvas.getHeight();

        for (Point p : pointList) {
            canvas.drawCircle(p.getX(), p.getY(), 20, mPaint);
            System.out.println("POINT FROM DRAW " + p.getX() + ", " + p.getY());
        }

        mPaint.setStrokeWidth(16);
        for (Line l : lineList) {
            canvas.drawLine(l.getPointFirst().getX(), l.getPointFirst().getY(), l.getPointSecond().getX(), l.getPointSecond().getY(), mPaint);
            Log.d(TAG, "draw line " + l.getPointFirst().getX() + ", " + l.getPointFirst().getY() + ", " + l.getPointSecond().getX() + ", " + l.getPointSecond().getY());
        }

        if(start!=null || finish!=null) {
            mPaint.setColor(Color.BLACK);
            for (Point p : pointList) {
                canvas.drawCircle(p.getX(), p.getY(), 20, mPaint);
                System.out.println("POINT FROM DRAW " + p.getX() + ", " + p.getY());
            }
            mPaint.setColor(Color.RED);
            if (start!=null){
                canvas.drawCircle(start.getX(), start.getY(), 20, mPaint);
            }
            if (finish!=null){
                canvas.drawCircle(finish.getX(), finish.getY(), 20, mPaint);
            }

            mPaint.setColor(Color.BLACK);
        }

        //DRAWING FINDED PATH

//        if(path!=null){
//            mPaint.setColor(Color.RED);
//            for (int i = 0; i < path.size()-1; i++) {
//                canvas.drawLine(path.get(i).getX(),path.get(i).getY(),
//                        path.get(i+1).getX(),path.get(i+1).getY(), mPaint);
//
//            }
//
//        }

        if (path!=null){
            mPaint.setColor(Color.RED);
            for(Line l : path){
                canvas.drawCircle(l.getPointFirst().getX(), l.getPointFirst().getY(), 20, mPaint);
                canvas.drawCircle(l.getPointSecond().getX(), l.getPointSecond().getY(), 20, mPaint);
                canvas.drawLine(l.getPointFirst().getX(), l.getPointFirst().getY(), l.getPointSecond().getX(), l.getPointSecond().getY(), mPaint);
            }

        }
   }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();

        Log.d(TAG, "x and y : " + event.getX() +", " + event.getY());

        p1 = new Point(Math.round(x), Math.round(y));
        Log.d(TAG, "point1 : " + p1.getX() +", " + p1.getY());


        if (path==null){
        if(pointList.contains(p1)){

            //Without this lines, dots will jump every time, when the screen is redrawing,
            //couse our dot can have different coordinates than dot in pointList
            x = pointList.get(pointList.indexOf(p1)).getX();
            y = pointList.get(pointList.indexOf(p1)).getY();
            p1 = new Point(x,y);

            //IF STATUS != 0 MEANS, THAT THE BUTTONS "CHOOSE START/FINISH POINT WAS CLICKED
            if(status!=Status.PAINTING){
                secondTouch=false;
                createStartFinishDialog(status,p1);
            }



            if(secondTouch){
                createLineDialog(p2, p1);
                p2 = null;
                secondTouch = false;
            }

            else{
                p2 = p1;
                Log.d(TAG, "point2 : " + p2.getX() +", " + p2.getY());
                Log.d(TAG, "point1 : " + p1.getX() +", " + p1.getY());

                secondTouch = true;
            }

        }
        else {
            pointList.add(p1);
            System.out.println("POINT" + p1.getX() + ", " + p1.getY());
            postInvalidate();
        }}



        return false;
    }


    protected void createLineDialog(final Point p1, final Point p2){

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.prompt, null);


        final EditText inputFeromons = new EditText(getContext());

        if (Settings.getInstanse().isFeromons()){
            LinearLayout layout_root = (LinearLayout) promptsView.findViewById(R.id.layout_root);
            TextView tv = new TextView(getContext());
            tv.setText("Введіть кількість феромонів");
            tv.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
            layout_root.addView(tv);
            inputFeromons.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout_root.addView(inputFeromons);
        }




        final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());
        mDialogBuilder.setView(promptsView);

        final EditText inputWeigth = (EditText) promptsView.findViewById(R.id.input_weight);
        mDialogBuilder.setCancelable(true);
        mDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                float feromons = 2;
                if(Settings.getInstanse().isFeromons()){
                    if (!inputFeromons.getText().toString().isEmpty()){
                        feromons = Float.parseFloat(inputFeromons.getText().toString());
                    }

                }

                //IF WEIGHT IS EMPTY USE DISTANCE BETWEEN DOTS
                if (inputWeigth.getText().toString().isEmpty()){
                    float length = (float) Math.sqrt(Math.pow((p1.getX() - p2.getX()),2) + Math.pow((p1.getY() - p2.getY()),2));
                    l = new Line(p1,p2,length, feromons);
                }
                else {
                    l = new Line(p1, p2, Float.parseFloat(inputWeigth.getText().toString()), feromons);
                }
                lineList.add(l);
                postInvalidate();
            }
        });
        mDialogBuilder.setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }

    protected void createStartFinishDialog(final Status status, final Point p){
        final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

        mDialogBuilder.setMessage("Ви впевнені?").setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (status==Status.START)
                {start = p;
                    postInvalidate();
                    Log.d(TAG, "START " + p.getX() +", " +p.getY());
                    Log.d(TAG, "start : " + p.getX() +", " + p.getY());}
                else{
                    finish = p;
                    postInvalidate();
                    Log.d(TAG, "FINISH " + p.getX() +", " +p.getY());}
            }
        }).setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = mDialogBuilder.create();

        alertDialog.show();

    }

    ACO aco;
    ArrayList<Line> path;
    protected void startAlgorithm(){

        Log.d(TAG, "startAlgorithm: ");
        aco = new ACO(lineList,pointList, start,finish);
        Log.d(TAG, "startAlgorithm: execute");
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



}
