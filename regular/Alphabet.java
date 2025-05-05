package regular;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Alphabet extends HashSet<String> {

    public Alphabet() {
        super();
        super.add(null);
    }

    public Alphabet(Collection<String> lettersCollection) {
        this();
        super.addAll(lettersCollection);
    }

    public Alphabet(String[] lettersArray) {
        this(Arrays.asList(lettersArray));
    }

}