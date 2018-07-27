package GUI.Drawing;

import GUI.Location;

import java.awt.*;
import java.util.ArrayList;

public class Polygon {
   public enum PolyType{
       LAKE, OCEAN, RIVER, BLUE_UNKNOWN, STATE_PARK, SPORT, GOLF, CITY_PARK, WOODS, NATIONAL_PARK,
       UNIVERSITY, HOSPITAL, SHOPPING_CENTRE, MAN_MADE_AREA, AIRPORT_RUNWAY, CEMETERY, CITY,
       CAR_PARK, AIRPORT
   }

   private String type;
   private PolyType polyType;
   private String name;
   private int endLevel;
   private int cityIdx;
   private ArrayList<Location> coords;

    private Polygon(){
        coords = new ArrayList<Location>();
    }

    public static Polygon create(){
        return new Polygon();
    }

    public Polygon ofType(String type){ this.type = type; assignType(); return this; }

    public Polygon addCoord(Location l){
        coords.add(l);
        return this;
    }

    public Polygon withLabel(String name){
        this.name = name;
        return this;
    }

    public Polygon withEndLevel(int endLevel){
        this.endLevel = endLevel;
        return this;
    }

    public Polygon withCityIdx(int cityIdx){
        this.cityIdx = cityIdx;
        return this;
    }

    public int getEndLevel() {
        return endLevel;
    }

    public String getType() {
        return type;
    }

    private void assignType(){
        switch(type){
            case "0x3c":
                polyType = PolyType.LAKE;
                break;
            case "0x3e":
                polyType = PolyType.LAKE;
                break;
            case "0x41":
                polyType = PolyType.LAKE;
                break;
            case "0x40":
                polyType = PolyType.LAKE;
                break;
            case "0x28":
                polyType = PolyType.OCEAN;
                break;
            case "0x48":
                polyType = PolyType.RIVER;
                break;
            case "0x47":
                polyType = PolyType.RIVER;
                break;
            case "0x45":
                polyType = PolyType.BLUE_UNKNOWN;
                break;
            case "0x1e":
                polyType = PolyType.STATE_PARK;
                break;
            case "0x19":
                polyType = PolyType.SPORT;
                break;
            case "0x18":
                polyType = PolyType.GOLF;
                break;
            case "0x17":
                polyType = PolyType.CITY_PARK;
                break;
            case "0x50":
                polyType = PolyType.WOODS;
                break;
            case "0x16":
                polyType = PolyType.NATIONAL_PARK;
                break;
            case "0xa":
                polyType = PolyType.UNIVERSITY;
                break;
            case "0xb":
                polyType = PolyType.HOSPITAL;
                break;
            case "0x8":
                polyType = PolyType.SHOPPING_CENTRE;
                break;
            case "0x13":
                polyType = PolyType.MAN_MADE_AREA;
                break;
            case "0xe":
                polyType = PolyType.AIRPORT_RUNWAY;
                break;
            case "0x1a":
                polyType = PolyType.CEMETERY;
                break;
            case "0x2":
                polyType = PolyType.CITY;
                break;
            case "0x5":
                polyType = PolyType.CAR_PARK;
                break;
            case "0x7":
                polyType = PolyType.AIRPORT;
                break;
            default :
                break;
        }
    }

    public ArrayList<Location> getCoords() {
        return coords;
    }

    public PolyType getPolygonType(){
        return polyType;
    }

    //public String

    public void draw(Graphics2D g, Location origin, double scale){
        Color c = ColorFactory.getPolygonColor(polyType);
        g.setColor(c);
        int[] xCoords = new int[coords.size()];
        int[] yCoords = new int[coords.size()];
        int i =0;
        for (i = 0; i < coords.size(); i++){
            Point p = coords.get(i).asPoint(origin, scale);
            xCoords[i] = (int) p.getX();
            yCoords[i] = (int) p.getY();
        }
        java.awt.Polygon p = new java.awt.Polygon(xCoords, yCoords, i);
        g.fill(p);
    }
}
