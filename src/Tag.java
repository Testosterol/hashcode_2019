import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class Tag implements Comparator {
    private String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public int compare(Object o1, Object o2) {
        return ((String)o1).compareTo((String)o2);
    }
}
