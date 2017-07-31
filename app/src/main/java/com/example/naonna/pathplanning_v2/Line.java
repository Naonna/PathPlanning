package com.example.naonna.pathplanning_v2;

import java.util.ArrayList;

/**
 * Created by naonna on 29.05.17.
 */

public class Line {
    private Point pointFist;
    private Point pointSecond;
    private float weight;
    private float feromons;

    public Line(Point pointFist, Point pointSecond, float weight, float feromons) {
        this.pointFist = pointFist;
        this.pointSecond = pointSecond;
        this.weight = weight;
        this.feromons = feromons;
    }

    public Line(Point pointFist, Point pointSecond, float weight) {
        this.pointFist = pointFist;
        this.pointSecond = pointSecond;
        this.weight = weight;
        feromons = 2;
    }

    public Line(Point pointFist, Point pointSecond) {
        this.pointFist = pointFist;
        this.pointSecond = pointSecond;
        this.weight = (float) Math.sqrt(Math.pow((pointFist.getX() - pointSecond.getX()),2) + Math.pow((pointFist.getY() - pointSecond.getY()),2));;
        feromons = 2;
    }



    public Point getPointFirst() {
        return pointFist;
    }

    public void setPointFist(Point pointFist) {
        this.pointFist = pointFist;
    }

    public Point getPointSecond() {
        return pointSecond;
    }

    public void setPointSecond(Point pointSecond) {
        this.pointSecond = pointSecond;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getFeromons() {
        return feromons;
    }

    public void setFeromons(float feromons) {
        this.feromons = feromons;
    }


}
