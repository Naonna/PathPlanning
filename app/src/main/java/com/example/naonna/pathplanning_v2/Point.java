package com.example.naonna.pathplanning_v2;

import android.view.Display;

/**
 * Created by naonna on 29.05.17.
 */

public class Point {

    private float x;
    private float y;
    int realX;
    int realY;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
        realX = countReal(Draw.width, getX());
        realY = countReal(Draw.height, getY());
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = countReal(Draw.width, getX());
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = countReal(Draw.height,getY());
    }

//
//    int width = Draw.width;
//    int height = Draw.height;


    private int countReal(int r, float j){
        int res = 0;
        for (int i=0; i<r-Settings.getInstanse().getScale(); i=i+Settings.getInstanse().getScale()){
            if(j>=i && j<i+Settings.getInstanse().getScale())
                res = i;
        }
        return res;
    }

    @Override
    public int hashCode() {
             return realX+37*realY;
    }


    @Override
    public boolean equals(Object obj) {
        Point x = (Point) obj;

        if(realX == getX() && realY == getY()){
            if (x.getX() == getX() && x.getY() == getY())
                return true;
        } else{
            if (Math.abs(x.getX()-getX()) < Settings.getInstanse().getScale() && Math.abs(x.getY()-getY()) < Settings.getInstanse().getScale()){
//            setX(x.getX());
//            setY(x.getY());
//            x.setX(getX());
//          x.setY(getY());
                return true;
            }

        }


        return false;
    }

}
