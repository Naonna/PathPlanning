package com.example.naonna.pathplanning_v2;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Created by naonna on 29.05.17.
 */

public class ACO extends AsyncTask<Void,Void,ArrayList<Line>>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private static final String TAG = "TAG";
    private ArrayList<Line> lineList;
    private ArrayList<Point> pointList;
    private Point startPoint;
    private Point finishPoint;

    public ACO(ArrayList<Line> lineList,ArrayList<Point> pointList, Point startPoint, Point finishPoint) {
        this.lineList = lineList;
        this.pointList = pointList;
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
    }

    ArrayList<ArrayList<Point>> antsPath = new ArrayList<>();
    HashSet<ArrayList<Line>> allAntsPathes = new HashSet<>();
    Point tempStart;
    ArrayList<Point> path;
    HashMap<Integer, Float> lineRaiting = new HashMap<>();


    Settings settings = Settings.getInstanse();


    float p;
    float ALPHA = settings.getALPHA();
    float BETA = settings.getBETA();
    float TAU = settings.getTAU();
    int Q = settings.getQ();
    float sumP=0;
    int countSize=0;
    float min=0;

    HashMap<ArrayList<Point>, Integer> previousPath = new HashMap<>();
    int count = 0;


    @Override
    protected ArrayList<Line> doInBackground(Void... params) {
        return finding();
    }

    private ArrayList<Line> finding() {

        Log.d(TAG, "finding: ");
//        while (count < 5 || previousPath.size() != 1) {
        while (count < pointList.size()*2 && countSize<lineList.size() ) {

            //DELETE FEROMONS
            if (antsPath.size() != 0) {
                for (ArrayList<Point> points : antsPath) {

                    for (int i = 0; i < points.size() - 1; i++) {

                        Line line = findLine(lineList, points.get(i), points.get(i + 1));
                        line.setFeromons(line.getFeromons() * (1 - TAU));
                    }
                }
                antsPath.clear();
                count++;
            }

            //FOR EVERY AGENT
//            for (int i = 0; i < numberOfAgents(); i++) {

            int numberOfAgents;
            if (settings.numberOfagents==0) {
                numberOfAgents = lineList.size();
            } else
                numberOfAgents = settings.getNumberOfagents();

            for (int i = 0; i < numberOfAgents; i++) {

                path = new ArrayList<Point>();
                tempStart = new Point(startPoint.getX(), startPoint.getY());
                path.add(tempStart);

                //END WHEN WE RICH THE FINISH POINT
                while (!tempStart.equals(finishPoint)) {

                    //FINDING LINES FROM POINT
                    for (Integer lineNumber : findLinesFromPoint(tempStart)) {

                        //IF THIS POINT ISN'T IN PATH - COUNT P FOR LINE
                        Point f = lineList.get(lineNumber).getPointFirst();
                        Point s = lineList.get(lineNumber).getPointSecond();
                        if(f.equals(finishPoint) || s.equals(finishPoint)){
                            path.add(finishPoint);
                            tempStart=finishPoint;
                            break;
                        }

                     //   if ((!path.contains(f) || !path.contains(s)) ) {
                        if ((!path.contains(f) || !path.contains(s)) || (path.indexOf(f) != path.indexOf(s) + 1 && path.indexOf(f) != path.indexOf(s) - 1)) {
                            //  if(!path.contains(lineList.get(lineNumber))){

                            p = (float) (Math.pow(lineList.get(lineNumber).getWeight(), ALPHA) *
                                    Math.pow(lineList.get(lineNumber).getFeromons(), BETA)) /
                                    denominatorP(findLinesFromPoint(tempStart));
                            sumP = sumP + p;
                            lineRaiting.put(lineNumber, p);
                        }
                    }
                    if (tempStart.equals(finishPoint)){
                        lineRaiting.clear();
                        break;
                    }else {
                        if (lineRaiting.isEmpty()) break;
                        int choosedLine = chooseLine(lineRaiting);
                        if (lineList.get(choosedLine).getPointFirst().equals(tempStart)) {
                            tempStart = lineList.get(choosedLine).getPointSecond();
                        } else {
                            tempStart = lineList.get(choosedLine).getPointFirst();
                        }
                        path.add(tempStart);
                        lineRaiting.clear();
                        sumP = 0;
                    }
                }
                antsPath.add(path);
                ArrayList<Line> line = new ArrayList<>();
                for (int j = 0; j < path.size() - 1; j++) {
                    Line l = findLine(lineList, path.get(j), path.get(j + 1));
                    line.add(l);
                }
                int size=allAntsPathes.size();
                allAntsPathes.add(line);
                if (allAntsPathes.size()==size){
                    countSize++;
                } else countSize=0;
                if (countSize>lineList.size()) {
                    break;
                }
            }

            //ADD FEROMONS
            for (ArrayList<Point> points : antsPath) {
                float eta = 0;
                for (int i = 0; i < points.size() - 1; i++) {

                    Line line = findLine(lineList, points.get(i), points.get(i + 1));
                    eta = eta + deltaEta(points) + line.getFeromons() * BETA;
                    lineList.get(lineList.indexOf(line)).setFeromons(eta);
                    if (eta>min) min=eta;
                }
            }

        }
        ArrayList<Line> finalPath = new ArrayList<>();
        float fer = 0;
        float max = 0;
        float len=0;
        for (ArrayList<Line> line : allAntsPathes) {
            if (allAntsPathes.size() == 1) {
                finalPath = line;
            } else {
                for (Line l : line) {

                    len = len + l.getWeight();
                    fer = fer + l.getFeromons();
                }
                if (fer > max) {
                    max = fer;
                    fer = 0;
                    //               finalPath = line;
                }
                if (len < min) {
                    min = len;
                    finalPath = line;
                    len = 0;
                }

            }
        }


        return finalPath;
    }



/* here
            if (previousPath.size()!=0){
                if (previousPath.equals(makePriviousPath(antsPath))) {
                    count++;
                } else {
                    count=0;
                    previousPath=makePriviousPath(antsPath);
                }
            } else {
                previousPath=makePriviousPath(antsPath);
            }
        }

        ArrayList<Point> finalPath = new ArrayList<>();
        Object[] keys;
        keys = previousPath.keySet().toArray();

        if (previousPath.size()!=1){

            int max = 0;
            for (int i = 0; i < previousPath.size(); i++) {
                if (previousPath.get(keys[i])>max) {
                    max=previousPath.get(keys[i]);
                    finalPath = ( ArrayList<Point>) keys[i];
                }
            }
        } else {
            finalPath = ( ArrayList<Point>) keys[0];
        }
HERE*/
 //       return finalPath;
   // }


    private int numberOfAgents(){
        return (findLinesFromPoint(startPoint).size()+findLinesFromPoint(finishPoint).size())/2;
    }


    private ArrayList<Integer> findLinesFromPoint(Point p){
        ArrayList<Integer> numberOfLines = new ArrayList<>();

        for(Line line : lineList){
            if(line.getPointFirst().equals(p) ||
                    line.getPointSecond().equals(p)){
                numberOfLines.add(lineList.indexOf(line));
            }
//            if((line.getPointFirst().getX()==p.getX() &&
//                    line.getPointFirst().getY()==p.getY()) ||
//                    line.getPointSecond().getX()==p.getX() &&
//                            line.getPointSecond().getY()==p.getY()){
//                numberOfLines.add(lineList.indexOf(line));
//            }
        }
        return numberOfLines;
    }

    private float denominatorP(ArrayList<Integer> numberOfLines){
        float sum = 0;

        for(Integer num : numberOfLines){
            sum += Math.pow(lineList.get(num).getWeight(), ALPHA) * Math.pow(lineList.get(num).getFeromons(), BETA);
        }
        return sum;
    }

    private int chooseLine(HashMap<Integer, Float> lineRaiting) {

        float randomNumber = (float) Math.random()*sumP;
        float min;
        int key;

        Object[] keys;
        keys =  lineRaiting.keySet().toArray();

//        min = Math.abs(randomNumber - lineRaiting.get(keys[0]));

        ArrayList<Integer> k = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            k.add(Integer.parseInt(keys[i].toString()));
        }
 //       Collections.sort(k);
        key =  k.get(0);

        //EXAMPLE:
        // WE HAVE LINES WITH P: 75, 10, 15.
        //RANDOM NUMBER - 100.
        // |100-75|=25. |100-10|=90. |100-15|=85
        //25 IS MIN, SO WE CHOOSE THIS LINE.
        //RANDOM NUMBER - 5: 100-
        //|5-75|=70. |5-10|=5. |5-15|=10. 5 - MIN, SO CHOOSE SECOND LINE
//        for(int i = 0; i < lineRaiting.size(); i++){
//            if(Math.abs(randomNumber - lineRaiting.get(keys[i]))<min){
//                min = randomNumber - lineRaiting.get(keys[i]);
//                key = (int) keys[i];
//            }
//
//        }
        float interval=0;
        for(int i =0; i < lineRaiting.size(); i++){
            if(randomNumber>=interval && randomNumber<interval+lineRaiting.get(k.get(i))) {
                key = k.get(i);
                break;
            } else {
                interval = interval+lineRaiting.get(k.get(i));
            }

        }
        return key;
    }

    private float deltaEta(ArrayList<Point> points){
        float weight = 0;
        for (int i = 0; i < points.size()-1; i++) {
            Line line = findLine(lineList, points.get(i), points.get(i+1));
            weight = weight + line.getWeight();
        }
        return Q/weight;
    }

    //FIND LINE BY TWO POINTS
    public Line findLine(ArrayList<Line> lines, Point pointFist, Point pointSecond){
        Line l = null;
        for (Line line : lines){
            if ((line.getPointFirst().equals(pointFist) && line.getPointSecond().equals(pointSecond))
                    || (line.getPointFirst().equals(pointSecond) && line.getPointSecond().equals(pointFist))){
                l = line;
                break;
            }
        }
        return l;
    }

    private HashMap<ArrayList<Point>, Integer> makePriviousPath(ArrayList<ArrayList<Point>> antsPath){
        float x;
        float y;

        for (ArrayList<Point> points : antsPath){
            for (Point point : points){
                if(pointList.contains(point)){
                    x = pointList.get(pointList.indexOf(point)).getX();
                    y = pointList.get(pointList.indexOf(point)).getY();
                    point = new Point(x,y);
            }
            if (previousPath.containsKey(points)){
                previousPath.put(points, previousPath.get(points)+1);
            } else
                previousPath.put(pointList, 1);
            }
        }
        System.out.println(ALPHA);
        return previousPath;


//
//
//        for (ArrayList<Point> pointList : antsPath){
//            if (containts(previousPath, pointList)){
//                previousPath.put(pointList, previousPath.get(pointList)+1);
//            }
//            else{
//                previousPath.put(pointList, 1);
//            }
//        }
//        return previousPath;
    }
//    private boolean containts(HashMap<ArrayList<Point>, Integer> previousPath, ArrayList<Point> pointList){
//
//        for(ArrayList<Point> key : previousPath.keySet()){
//            for (Point p : pointList){
//                if (!key.contains(p)) return false;
//            }
//        }
//        return true;
//    }



}
