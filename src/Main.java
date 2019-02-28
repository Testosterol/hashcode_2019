import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static final String PATH_TO_DESKTOP = "a_example.in";

    static int images;
    static HashMap<String,Integer> tags;
    //static SlideShow slideShow;


    public static void main(String[] args) {

       tags = new HashMap<>();
       //slideShow = new SlideShow();

        setArray();
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
        images = Integer.parseInt(s);

        for(int i=0;i<images;i++) {
            //array[i] = scan.nextLine().split("");
            String[] values = scan.nextLine().split(" ");
            Image image = new Imgae();
            image.setOrientation(values[0]);
            TagList list = new TagList();
            for (int j=2;j<Integer.parseInt(values[1]);j++){
                if(tags.containsKey(values[j])) {
                    tags.put(values[j],tags.get(values[j]+1));
                } else {
                    tags.put(values[j],1);
                }
            }
        }



    }

}
