import sun.reflect.generics.tree.Tree;

import java.awt.*;
import java.io.*;
import java.util.*;

public class Main {

    static final String PATH_TO_DESKTOP = "d_pet_pictures.txt";
    static HashMap<String,Integer> tags;

    static SlideShow slideShow;
    static ArrayList<SubSlideShow> subSlideShows ;
    static ArrayList<SubSlideShow> subSlideShows2 ;
    static ArrayList<Slide> slides;

    static ArrayList<Image> horizontalImages;
    static ArrayList<Image> vertivalImages;

    public static void main(String[] args) {

        tags = new HashMap<>();
        horizontalImages = new ArrayList<>();
        vertivalImages = new ArrayList<>();
        subSlideShows = new ArrayList<>();
        subSlideShows2 = new ArrayList<>();
        slideShow = new SlideShow();
        slides = new ArrayList<>();
        setArray();

        addVerticals();

        for(Image image : horizontalImages) {
            Slide slide = new Slide(image,null);
            slides.add(slide);
            subSlideShows.add(new SubSlideShow(slide));
        }

        solve();
    }


    public static void solve(){

        Random random = new Random();
        int initialSize = subSlideShows.size();
        int percent = 0;
        while (subSlideShows.size() != 1) {

            //print the progress percentage
            System.out.println(100 - ((double)subSlideShows.size()/initialSize) * 100);

            int maxScore = 0;
            SubSlideShow maxResultLeft = null;
            SubSlideShow maxResultRight = null;

            //Start of main program loop.
            //This is the loop that takes most of the running time.
            //Needs to be optimised.
            for (int i = 0; i < subSlideShows.size()+200; i++) {
                int randomLeft = random.nextInt(subSlideShows.size());
                int randomRight = random.nextInt(subSlideShows.size());

                SubSlideShow left = subSlideShows.get(randomLeft);
                SubSlideShow right = subSlideShows.get(randomRight);
                if (left.equals(right)) {
                    i--;
                    continue;
                }

                int score = getPointsFor2Slides(left.slides.get(left.slides.size() - 1),
                        right.slides.get(0));
                if (score >= maxScore) {
                    maxScore = score;
                    maxResultLeft = left;
                    maxResultRight = right;
                }
            }
            //End of main program loop

            System.out.println("Score is: " +getPointsFor2Slides(maxResultLeft.slides.get(maxResultLeft.slides.size() - 1),
                    maxResultRight.slides.get(0)));
            subSlideShows.add(new SubSlideShow(maxResultLeft, maxResultRight));
            subSlideShows.remove(maxResultLeft);
            subSlideShows.remove(maxResultRight);
        }
        slideShow.slides = subSlideShows.get(0).slides;
        subSlideShows.remove(0);

        createOutputFile();
    }


    public static void improve(){
        printResult();
        int steps = 20;
        Random random = new Random();
        for (int i=0;i<slideShow.slides.size()*10;i++) {
            int maxSwapIndex = -1;
            int max = 0;
            int firstPosition = random.nextInt(slideShow.slides.size());

            for(int k = 0; k  <steps; k++) {
                if (firstPosition == k)
                    continue;

                int difference = getDiffrence(k, firstPosition);
                if (difference > max) {
                    max = difference;
                    maxSwapIndex = k;
                }
            }

            if(maxSwapIndex != -1) {
                //we have a hit
                Collections.swap(slideShow.slides,firstPosition,maxSwapIndex);
            }
        }
    }

    public static int getDiffrence(int one, int two) {
        int startScore = getScoreForPosition(one,slideShow.slides.get(one))+getScoreForPosition(two,slideShow.slides.get(two));
        Collections.swap(slideShow.slides,one,two);

        int endScore = getScoreForPosition(one,slideShow.slides.get(one))+getScoreForPosition(two,slideShow.slides.get(two));
        Collections.swap(slideShow.slides,one,two);

        return endScore-startScore;
    }


    public static int getScoreForPosition(int position, Slide slide) {
        if(position == 0) {
            return getPointsFor2Slides(slide,slideShow.slides.get(1));
        } else if (position == slideShow.slides.size()-1) {
            return getPointsFor2Slides(slideShow.slides.get(position-1),slide);
        } else {
            return getPointsFor2Slides(slide,slideShow.slides.get(position+1)) +
                    getPointsFor2Slides(slideShow.slides.get(position-1),slide);
        }
    }


    public static void addVerticals(){
        Random r = new Random();
        while(vertivalImages.size() > 0) {
            ArrayList<Save> tmp = new ArrayList<>();
            System.out.println("verticals: " + vertivalImages.size());
            for(int i=0;i<100;i++){
                Slide first = new Slide(vertivalImages.get(r.nextInt(vertivalImages.size())),null);
                Slide second = new Slide(vertivalImages.get(r.nextInt(vertivalImages.size())),null);
                if(first.first.getPhotoId() == second.first.getPhotoId()) {
                    i--;
                    continue;
                }
                int common = getCommonTags(first.getTags(),second.getTags());

                tmp.add(new Save(first,second,common));
                if(common == 0)
                    break;
            }

            tmp.sort(new Comparator<Save>() {
                @Override
                public int compare(Save o1, Save o2) {
                    if(o1.score<o2.score)
                        return -1;
                    else if(o1.score>o2.score)
                        return 1;
                    else
                        return 0;
                }
            });

            subSlideShows.add(new SubSlideShow(new Slide(tmp.get(0).firstSlide.first,tmp.get(0).secondSlide.first)));
            vertivalImages.remove(tmp.get(0).firstSlide.first);
            vertivalImages.remove(tmp.get(0).secondSlide.first);
        }
    }


    public static int getPointsFor2Slides(Slide left,Slide right ) {
        return Math.min(Math.min(getCommonTags(left.getTags(),right.getTags()),
                getLeftUnique(left.getTags(),right.getTags())),getLeftUnique(right.getTags(),left.getTags()));
    }


    public static int getCommonTags(TreeSet<String> leftList,TreeSet<String> rightList) {
        ArrayList<String> tmp = new ArrayList<>(leftList);
        tmp.removeAll(rightList);
        return leftList.size() - tmp.size();
    }

    public static int getLeftUnique(TreeSet<String> leftList,TreeSet<String> rightList) {
        ArrayList<String> tmp = new ArrayList<>(leftList);
        tmp.removeAll(rightList);
        return tmp.size();
    }

    public static void printResult(){
        int sum = 0;
        ArrayList<SlidePoint> sortedSlides = new ArrayList<>();
        for(int i=0;i<slideShow.slides.size()-1;i++) {
            sum += getPointsFor2Slides(slideShow.slides.get(i), slideShow.slides.get(i + 1));
            sortedSlides.add(new SlidePoint(slideShow.slides.get(i),i,getScoreForPosition(i,slideShow.slides.get(i))));
        }

        System.out.println(sum);
    }


    static class SubSlideShow{
        ArrayList<Slide> slides;
        public SubSlideShow(Slide first){
            slides = new ArrayList<>();
            slides.add(first);
        }
        public SubSlideShow(SubSlideShow left,SubSlideShow right) {
            slides = new ArrayList<>(left.slides);
            slides.addAll(slides.size(),right.slides);
        }
    }

    static class SlidePoint {
        int poistion;
        Slide slide;
        int points;
        SlidePoint(Slide s, int position,int points) {
            this.slide = s;
            this.poistion = position;
            this.points = points;
        }
    }

    public static class Save{
        Slide firstSlide;
        Slide secondSlide;
        int score;
        public Save(Slide first,Slide second,int score){
            this.firstSlide = first;
            this.secondSlide = second;
            this.score = score;
        }
    }

    static class SlideShow {
        ArrayList<Slide> slides = new ArrayList<>();
    }

    static class Slide {
        Image first;
        Image second;
        TreeSet<String> mergedTags = new TreeSet<>();

        public Slide(Image first,Image second) {
            this.first = first;
            if(second != null)
                this.second = second;

            mergeTags();
        }

        public TreeSet<String> getTags(){
            return mergedTags;
        }

        public void mergeTags(){
            for(String tag : first.getTagList())
                mergedTags.add(tag);

            if(second != null) {
                for(String tag : second.getTagList())
                    mergedTags.add(tag);
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
            ArrayList<String> list = new ArrayList<>();
            for (int j=2;j<values.length;j++){
                list.add(values[j]);
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

    public static void createOutputFile(){
        System.out.println("Printing");
        printResult();
        Writer writer = null;
        File file = new File(System.getProperty("user.home") + "/Desktop/hashcode/a.txt");
        try {
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(slideShow.slides.size()+"\n");
            for(int i=0; i<slideShow.slides.size();i++){
                if(slideShow.slides.get(i).second != null) {
                    w.write(slideShow.slides.get(i).first.getPhotoId()+" " + slideShow.slides.get(i).second.getPhotoId()+"\n");
                } else {
                    w.write(slideShow.slides.get(i).first.getPhotoId()+"\n");
                }
            }
            w.close();
            osw.close();
            is.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
    }

}
