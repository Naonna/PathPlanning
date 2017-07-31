package com.example.naonna.pathplanning_v2;

/**
 * Created by naonna on 09.06.17.
 */

public class Settings {


    float ALPHA;
    float BETA;
    float TAU;
    int Q;
    int numberOfagents;
    boolean feromons;
    int scale;
    

    private static final Settings INSTANSE = new Settings();

    private Settings(){
        ALPHA = 0.5F;
        BETA = 3F;
        TAU = 0.5f;
        Q = 5;
        numberOfagents = 0;
        feromons = false;
        scale = 1;
    }

    public int getNumberOfagents() {
        return numberOfagents;
    }

    public void setNumberOfagents(int numberOfagents) {
        this.numberOfagents = numberOfagents;
    }

    public boolean isFeromons() {
        return feromons;
    }

    public void setFeromons(boolean feromons) {
        this.feromons = feromons;
    }

    public static Settings getInstanse(){
        return INSTANSE;
    }

    public float getALPHA() {
        return ALPHA;
    }

    public void setALPHA(float ALPHA) {
        this.ALPHA = ALPHA;
    }

    public float getBETA() {
        return BETA;
    }

    public void setBETA(float BETA) {
        this.BETA = BETA;
    }

    public float getTAU() {
        return TAU;
    }

    public void setTAU(float TAU) {
        this.TAU = TAU;
    }

    public int getQ() {
        return Q;
    }

    public void setQ(int q) {
        Q = q;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
