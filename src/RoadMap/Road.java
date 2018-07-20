package RoadMap;

import java.util.ArrayList;

public class Road {

    public enum RoadClass{
        RESIDENTIAL, COLLECTOR, ARTERIAL, PRINCIPLE_HW, MAJOR_HW
    }

    private final int roadID, speedLimit;
    private final String name, city;
    private final boolean oneway, notforcar,notforped, notforbicycle;
    private final RoadClass roadClass;
    private final ArrayList<Segment> segments = new ArrayList<Segment>();
    private boolean highlighted;


    public static class Builder {
        private final int roadID;

        private String name = null;
        private String city = null;
        private int speedLimit = -1;
        private RoadClass roadClass = null;
        private boolean oneway = false;
        private boolean notforcar = false;
        private boolean notforped = false;
        private boolean notforbicycle = false;

        private Builder(int roadID){this.roadID = roadID;}

        public static Builder createWithID(int roadID){
            return new Builder(roadID);
        }

        public Builder name(String name){this.name = name; return this;}
        public Builder city(String city){this.city = city; return this;}

        public Builder speedlimit(int speedlimit){
            int speed;
            switch(speedlimit){
                case 0: speed = 5;
                        break;
                case 6: speed = 110;
                        break;
                case 7: speed = 0; // no speed limit
                        break;
                default: speed = speedlimit * 20;
                        break;
            }
            this.speedLimit = speed;
            return this;
        }

        public Builder roadClass(int roadClass){
            switch(roadClass){
                case 0: this.roadClass = RoadClass.RESIDENTIAL;
                        break;
                case 1: this.roadClass = RoadClass.COLLECTOR;
                        break;
                case 2: this.roadClass = RoadClass.ARTERIAL;
                    break;
                case 3: this.roadClass = RoadClass.PRINCIPLE_HW;
                        break;
                case 4: this.roadClass = RoadClass.MAJOR_HW;
                        break;
                default:
                        break;
            }
            return this;
        }

        public Builder isOneWay(int bool){if(bool == 1) {this.oneway = true;} return this;}
        public Builder isNotForCar(int bool){if(bool == 1) {this.oneway = true;} return this;}
        public Builder isNotForPed(int bool){if(bool == 1) {this.oneway = true;} return this;}
        public Builder isNotForBicycle(int bool){if(bool == 1) {this.oneway = true;} return this;}

        public Road build(){
            return new Road(this);
        }
    }

    private Road(Builder builder){
        this.roadID = builder.roadID;
        this.name = builder.name;
        this.city = builder.city;
        this.speedLimit = builder.speedLimit;
        this.roadClass = builder.roadClass;
        this.oneway = builder.oneway;
        this.notforcar = builder.notforcar;
        this.notforped = builder.notforped;
        this.notforbicycle = builder.notforbicycle;
    }

    public int getRoadID(){
        return this.roadID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void addSegment(Segment s){
        segments.add(s);
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }
}
