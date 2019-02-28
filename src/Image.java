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


    public ORIENTATION getOrientation() {
        return orientation;
    }

    public void setOrientation(ORIENTATION orientation) {
        this.orientation = orientation;
    }

    public TagList getTagList() {
        return tagList;
    }

    public void setTagList(TagList tagList) {
        this.tagList = tagList;
    }

    public int getNumberOfTags() {
        return numberOfTags;
    }

    public void setNumberOfTags(int numberOfTags) {
        this.numberOfTags = numberOfTags;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}