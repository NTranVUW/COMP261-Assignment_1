package GUI;

import RoadMap.RoadMap;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;

public class MapGUI extends GUI {
    private RoadMap map = RoadMap.newInstance();

    @Override
    protected void redraw(Graphics g) {

    }

    @Override
    protected void onClick(MouseEvent e) {

    }

    @Override
    protected void onSearch() {

    }

    @Override
    protected void onMove(GUI.Move m) {

    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons) {

    }

    public static void main(String[] args){
        final GUI.MapGUI mapGUI = new MapGUI();
    }
}
