public class Image {
    private enum ORIENTATION {
        HORIZONTAL,
        VERTICAL
    }

    private TagList tagList;
    private int numberOfTags;
    private int photoId;

    public Image(TagList tagList, int numberOfTags, int photoId) {
        TagList tagsToLower = new TagList();
        for (Tag tag : tagList){
            tagsToLower.add(new Tag(tag.getTag().toLowerCase()));
        }

        this.tagList = tagsToLower;
        this.numberOfTags = numberOfTags;
        this.photoId = photoId;
    }
}
