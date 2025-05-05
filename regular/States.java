package regular;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class States extends HashSet<String> {

    public States(Collection<String> statesCollection) {
        super();
        if (statesCollection.isEmpty()) throw new IllegalArgumentException("There must be at least one state");
        super.addAll(statesCollection);
    }

    public States(String[] statesArray) {
        this(Arrays.asList(statesArray));
    }

}