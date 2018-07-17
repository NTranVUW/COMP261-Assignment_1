package RoadMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Loader {
    private final RoadMap roadMap;
    private final File nodeFile;
    private final File roadFile;
    private final File segmentFile;
    private final File polygonFile;

    public static class Builder {
        private final RoadMap roadMap;

        private File nodeFile = null;
        private File roadFile = null;
        private File segmentFile = null;
        private File polygonFile = null;

        public Builder(RoadMap roadMap){this.roadMap = roadMap;}

        public Builder nodeFile(File file){this.nodeFile = file; return this;}
        public Builder roadFile(File file){this.roadFile = file; return this;}
        public Builder segmentFile(File file){this.segmentFile = file; return this;}
        public Builder polygonFile(File file){this.polygonFile = file; return this;}

        public Loader build(){
            return new Loader(this);
        }
    }

    private Loader(Builder builder){
        this.roadMap = builder.roadMap;
        this.nodeFile = builder.nodeFile;
        this.roadFile = builder.roadFile;
        this.segmentFile = builder.segmentFile;
        this.polygonFile = builder.polygonFile;
    }

    public Loader load(){
        if (this.nodeFile != null){
            this.loadNodes();
        }
        return this;
    }

    private void loadNodes(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(this.nodeFile));
            String line = null;
            while((line = reader.readLine()) != null){
                String[] split = line.split("\t");
                for (int i = 0; i < split.length; i+=3){
                    this.roadMap.addNode(Node.withID(Integer.parseInt(split[i]))
                                .atLocation(Location.newFromLatLon(Double.parseDouble(split[i+1]),
                                                                   Double.parseDouble(split[i+2]))));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }




    }
}
