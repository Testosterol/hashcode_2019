import java.awt.*;
import java.io.*;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static final String PATH_TO_DESKTOP = "a_example.txt";

    static HashMap<String,Integer> tags;
    static ArrayList<Image> images;


    public static void main(String[] args) {

        tags = new HashMap<>();
        images = new ArrayList<>();
        setArray();

        for(String pair: tags.keySet()){
            System.out.println(pair +": "+ tags.get(pair));
        }
    }

    public static void sortBestImages(ArrayList<Image> images) {
        ArrayList<Pair> pairs = new ArrayList<Pair>();

        for(int i=0;i<images.size();i++) {
            for (int j = 0; j < images.size(); j++) {
                if (i == j)
                    continue;

                Pair pair = new Pair();
                pair.left = images.get(i);
                pair.right = images.get(j);
                pair.score = getPointsFor2Images(pair.left,pair.right);
                pairs.add(pair);
            }
        }

    }

    public static int getPointsFor2Images(Image left,Image right ) {

        return Math.min(Math.min(getCommonTags(left,right),getLeftUnique(left,right)),getLeftUnique(right,left));
    }

    public static int getCommonTags(Image left,Image right) {
        TagList leftList = left.getTagList();
        TagList rightList = right.getTagList();
        int equals = 0;

        for(int i=0;i<leftList.size();i++){
            for(int j=0;j<rightList.size();j++){
                if(leftList.get(i).equals(rightList.get(j)))
                    equals++;
            }
        }

        return equals;
    }

    public static int getLeftUnique(Image left,Image right) {
        TagList leftList = left.getTagList();
        TagList rightList = right.getTagList();
        int unique = 0;

        for(int i=0;i<leftList.size();i++){
            for(int j=0;j<rightList.size();j++){
                if(leftList.get(i).equals(rightList.get(j)))
                    break;
                unique++;
            }
        }

        return unique;
    }



//    public static Tag getBiggestTag(){
//
//        for(String pair: tags.keySet()){
//            System.out.println(pair +": "+ tags.get(pair));
//        }
//    }

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

    static class Pair{
        Image left;
        Image right;
        int score;
    }

    //set up the class array from the file given in the file name constant.
    public static void setArray(){
        //Handle the input
        File file = new File(PATH_TO_DESKTOP);
        Scanner scan = null;
        try {scan = new Scanner(file);}
        catch (FileNotFoundException e) {}

        String s = scan.nextLine();
        int numOfimages = Integer.parseInt(s);

        for(int i=0;i<numOfimages;i++) {
            //array[i] = scan.nextLine().split("");
            String[] values = scan.nextLine().split(" ");
            Image image;
            TagList list = new TagList();
            for (int j=2;j<values.length;j++){
                list.add(new Tag(values[j]));
                if(tags.containsKey(values[j])) {
                    tags.put(values[j],tags.get(values[j])+1);
                } else {
                    tags.put(values[j],1);
                }
            }
            if(values[0].equals("H"))
                image = new Image(Image.ORIENTATION.HORIZONTAL,list,Integer.parseInt(values[1]),i);
            else
                image = new Image(Image.ORIENTATION.VERTICAL,list,Integer.parseInt(values[1]),i);

            images.add(image);
        }



    }

}
