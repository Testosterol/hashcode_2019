import java.awt.*;
import java.io.*;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static final String PATH_TO_DESKTOP = "a_example.txt";

    static HashMap<String,Integer> tags;
    static ArrayList<Image> images;
    static ArrayList<Pair> pairs ;
    static SlideShow slideShow;



    public static void main(String[] args) {

        tags = new HashMap<>();
        images = new ArrayList<>();
        pairs = new ArrayList<>();
        slideShow = new SlideShow();
        setArray();

        solve();
    }

    public static void solve(){
        sortBestImages(images);

        while(pairs.size() > 0){
            Pair maxPair = pairs.get(pairs.size()-1);
            slideShow.slides.add(new Slide(maxPair.left));
            slideShow.slides.add(new Slide(maxPair.right));
            pairs.remove(maxPair.left);
            pairs.remove(maxPair.right);
            removeFromPairs(maxPair.left);
            removeFromPairs(maxPair.right);
        }

//        for(Slide slide : slideShow.slides) {
//            System.out.println(slide.images.get(0).getPhotoId()+" ");
//        }
        createOutputFile();
    }

    public static void removeFromPairs(Image left){
        ArrayList<Pair> removeList = new ArrayList<>();

        for(Pair pair : pairs) {
            if(pair.left.getPhotoId() == left.getPhotoId()
                    || pair.right.getPhotoId() == left.getPhotoId()) {
                removeList.add(pair);
            }
        }

        pairs.removeAll(removeList);
    }

    public static void sortBestImages(ArrayList<Image> images) {

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

        pairs.sort(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                 if(o1.score < o2.score)
                     return -1;
                 else if(o1.score == o2.score)
                     return 0;
                 else
                     return 1;
            }
        });

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
                if(leftList.get(i).getTag().equals(rightList.get(j).getTag()))
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
                if(leftList.get(i).getTag().equals(rightList.get(j).getTag()))
                    break;
                unique++;
            }
        }

        return unique;
    }



    public static void createOutputFile(){
        Writer writer = null;
        File file = new File(System.getProperty("user.home") + "/Desktop/hashcode/a.txt");
        try {
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(slideShow.slides.size()+"\n");
            for(int i=0; i<slideShow.slides.size();i++){
                w.write(slideShow.slides.get(i).images.get(0).getPhotoId()+"\n");
            }
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

    static class SlideShow {
        ArrayList<Slide> slides = new ArrayList<>();
    }

    static class Slide {
        ArrayList<Image> images = new ArrayList<>();
        public Slide(Image image) {
            images.add(images.size(),image);
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
