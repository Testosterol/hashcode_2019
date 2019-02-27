import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Main {

    static final String FILE_NAME = "";
    static final String PATH_TO_DESKTOP = System.getProperty("user.home") + "/Desktop/hashcode/"+FILE_NAME;

    static String[][] array;
    static int arrayRows;
    static int arrayCols;
    static int var1;
    static int var2;

    public static void main(String[] args) {
    }

    public static void createOutputFile(String path){
        Writer writer = null;
        File file = new File(path);
        try {
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            //w.write(("\n"));
            w.close();
            osw.close();
            is.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
    }

    //set up the class array from the file given in the file name constant.
    public static void setArray(){
        //Handle the input
        File file = new File(PATH_TO_DESKTOP);
        Scanner scan = null;
        try {scan = new Scanner(file);}
        catch (FileNotFoundException e) {}

        String s = scan.nextLine();
        String[] values = s.split(" ");

        arrayRows = Integer.parseInt(values[0]);
        arrayCols = Integer.parseInt(values[1]);
        var1 = Integer.parseInt(values[2]);
        var2 = Integer.parseInt(values[3]);

        array = new String[arrayRows][arrayCols];
        for(int i=0;i<array.length;i++)
            array[i] = scan.nextLine().split("");
    }

    //Convert one dimensional point to 2 dimensional point
    static Point oneToTwo(int index) {
        Point point = new Point();
        point.x = index/(array.length);
        point.y = index%(array.length);

        return point;
    }

    //Convert two dimensional point to 1 dimensional point
    static int twoToOne(Point point) {
        return point.x*(array.length) + point.y;
    }
}
