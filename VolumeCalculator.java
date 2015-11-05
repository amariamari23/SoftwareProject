import softwareproject.*;
/*import java.util.*;
import java.lang.*;*/

public class VolumeCalculator {
    
    public static void main(String[] args) {
        
        ParseArgs p = new ParseArgs();
        
        p.programInfo("volumeCalculator", "Calculate the volume of a box.");
        p.addPos("length", "the length of a box", Argument.Type.INT);
        p.addPos("width", "the width of a box", Argument.Type.INT);
        p.addPos("height", "the height of a box", Argument.Type.INT);
        p.addOpt("digit", 4, Argument.Type.INT, false);
        
        p.parse(args);
        
        int length = (int)p.getValue("length");
        int width = (int)p.getValue("width");
        int height = (int)p.getValue("height");
        
        int volumeOfBox = length * width * height;
        
        
        
        System.out.println(volumeOfBox + "");
    }
}