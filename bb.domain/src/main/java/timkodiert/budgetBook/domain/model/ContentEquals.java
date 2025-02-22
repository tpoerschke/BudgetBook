package timkodiert.budgetBook.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ContentEquals {

    boolean contentEquals(Object other);

    static boolean listsContentEquals(List<? extends ContentEquals> l1, List<? extends ContentEquals> l2) {
        Set<ContentEquals> matched = new HashSet<>();
        return l1.stream().allMatch(i -> {
            for (ContentEquals e : l2) {
                if (i.contentEquals(e) && !matched.contains(e)) {
                    matched.add(e);
                    return true;
                }
            }
            return false;
        });
    }
}
