
import java.util.Comparator;

public class Tag implements Comparable {
    private String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public int compareTo(Object o) {
        return tag.compareTo(((Tag)o).getTag());
    }
}
