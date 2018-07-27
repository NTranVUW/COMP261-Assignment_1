package GUI;

import RoadMap.Polygon;

import java.awt.*;

public class ColorFactory {

    private ColorFactory(){}

    public static Color getNodeColor(){
        return new Color(19, 15, 64); }

    public static Color getHighlightedNodeColor(){
        return new Color(255, 121, 121);
    }

    public static Color getSegmentColor(){
        return new Color(48, 51, 107);
    }

    public static Color getHighlightedSegmentColor(){
        return new Color(255, 190, 118);
    }

    public static Color getPolygonColor(Polygon.PolyType type){
        Color lightBlue = new Color(104, 109, 224);
        Color lightGreen = new Color(186, 220, 88);
        Color darkGreen = new Color(106, 176, 76);
        Color lightYellow = new Color(246, 229, 141);
        Color lightGrey = new Color(149, 175, 192);
        Color darkGrey = new Color(83, 92, 104);


        switch(type){
            case LAKE:
                return lightBlue;
            case OCEAN:
                return lightBlue;
            case RIVER:
                return lightBlue;
            case BLUE_UNKNOWN:
                return lightBlue;
            case STATE_PARK:
                return lightGreen;
            case SPORT:
                return lightGreen;
            case GOLF:
                return lightGreen;
            case CITY_PARK:
                return lightGreen;
            case WOODS:
                return darkGreen;
            case NATIONAL_PARK:
                return darkGreen;
            case UNIVERSITY:
                return lightYellow;
            case HOSPITAL:
                return lightYellow;
            case SHOPPING_CENTRE:
                return lightYellow;
            case MAN_MADE_AREA:
                return lightGrey;
            case AIRPORT_RUNWAY:
                return lightGrey;
            case CEMETERY:
                return lightGrey;
            case CITY:
                return darkGrey;
            case CAR_PARK:
                return lightGrey;
            case AIRPORT:
                return darkGrey;
            default:
                return darkGreen;

        }
    }
}
