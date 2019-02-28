public class Image {
    public enum ORIENTATION {
        HORIZONTAL,
        VERTICAL
    }

    private ORIENTATION orientation;
    private TagList tagList;
    private int numberOfTags;
    private int photoId;

    public Image(ORIENTATION orientation, TagList tagList, int numberOfTags, int photoId) {
        TagList tagsToLower = new TagList();
        for (Tag tag : tagList) {
            tagsToLower.add(new Tag(tag.getTag().toLowerCase()));
        }

        this.orientation = orientation;
        this.tagList = tagsToLower;
        this.numberOfTags = numberOfTags;
        this.photoId = photoId;
    }

}