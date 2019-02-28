import java.io.*;
import java.util.*;

public class Main {

    static final String PATH_TO_DESKTOP = "a_example.txt";

    static HashMap<String,Integer> tags;
    static ArrayList<Image> horizontalImages;
    static ArrayList<Pair> pairs ;
    static SlideShow slideShow;
    static ArrayList<Slide> slides;
    static ArrayList<Image> vertivalImages;

    public static void main(String[] args) {

        tags = new HashMap<>();
        horizontalImages = new ArrayList<>();
        vertivalImages = new ArrayList<>();
        pairs = new ArrayList<>();
        slideShow = new SlideShow();
        slides = new ArrayList<>();
        setArray();

        solve();
    }


    public static void solve(){

        for(int i=0; i<vertivalImages.size();i+=2){
            Slide slide = new Slide(vertivalImages.get(i));
            slide.images.add(vertivalImages.get(i+1));
            slide.mergeTag();
            slides.add(slide);
        }

        for(Image image : horizontalImages)
            slides.add(new Slide(image));

        sortBestImages(slides);

        while(pairs.size() > 0){
            Pair maxPair = pairs.get(pairs.size()-1);
            slideShow.slides.add(slideShow.slides.size(),maxPair.left);
            slideShow.slides.add(slideShow.slides.size(),maxPair.right);
            pairs.remove(maxPair.left);
            pairs.remove(maxPair.right);
            slides.remove(maxPair.left);
            slides.remove(maxPair.right);
            removeFromPairs(maxPair.left);
            removeFromPairs(maxPair.right);
        }
        if(slides.size() > 0) {
            slideShow.slides.add(slideShow.slides.size(),slides.get(0));
        }

//        for(Slide slide : slideShow.slides) {
//            System.out.println(slide.images.get(0).getPhotoId()+" ");
//        }

        createOutputFile();
    }

    public static void removeFromPairs(Slide slide){
        ArrayList<Pair> removeList = new ArrayList<>();

        for(Pair pair : pairs) {
            if(pair.left.equals(slide)
                    || pair.right.equals(slide)) {
                removeList.add(pair);
            }
        }
        pairs.removeAll(removeList);
    }

    public static void sortBestImages(ArrayList<Slide> slides) {

        for(int i=0;i<slides.size();i++) {
            for (int j = 0; j < horizontalImages.size(); j++) {
                if (i == j)
                    continue;

                Pair pair = new Pair();
                pair.left = slides.get(i);
                pair.right = slides.get(j);
                pair.score = getPointsFor2Slides(pair.left,pair.right);
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


    public static int getPointsFor2Slides(Slide left,Slide right ) {
        return Math.min(Math.min(getCommonTags(left.getTags(),right.getTags()),
                getLeftUnique(left.getTags(),right.getTags())),getLeftUnique(right.getTags(),left.getTags()));
    }

    public static int getCommonTags(TagList leftList,TagList rightList) {
        int equals = 0;

        for(int i=0;i<leftList.size();i++){
            for(int j=0;j<rightList.size();j++){
                if(leftList.get(i).getTag().equals(rightList.get(j).getTag()))
                    equals++;
            }
        }

        return equals;
    }

    public static int getLeftUnique(TagList leftList,TagList rightList) {
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
                if(slideShow.slides.get(i).images.size() > 1) {
                    w.write(slideShow.slides.get(i).images.get(0).getPhotoId()+" " + slideShow.slides.get(i).images.get(1).getPhotoId()+"\n");
                } else {
                    w.write(slideShow.slides.get(i).images.get(0).getPhotoId()+"\n");
                }
            }
            w.close();
            osw.close();
            is.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
    }

    static class Pair{
        Slide left;
        Slide right;
        int score;
    }

    static class SlideShow {
        ArrayList<Slide> slides = new ArrayList<>();
    }

    static class Slide {
        ArrayList<Image> images = new ArrayList<>();
        TreeSet<Tag> mergedTags = new TreeSet<>();

        public Slide(Image image) {
            images.add(images.size(),image);
        }

        public TagList getTags(){
            TagList list = new TagList();
            for(Tag tag: mergedTags){
                list.add(tag);
            }
            return list;
        }

        public void mergeTag(){
            for(Image image : images) {
                for(Tag tag : image.getTagList()) {
                    mergedTags.add(tag);
                }
            }

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
            else {
                image = new Image(Image.ORIENTATION.VERTICAL, list, Integer.parseInt(values[1]), i);
            }

            if(values[0].equals("V")){
                vertivalImages.add(image);
            } else {
                horizontalImages.add(image);
            }
        }



    }

}
