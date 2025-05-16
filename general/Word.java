package general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Word extends ArrayList<Symbol> {
    
    public Word(Collection<Symbol> symbolsCollection) {
        super(symbolsCollection);
    }

    public Word(Symbol[] symbolsArray) {
        this(Arrays.asList(symbolsArray));
    }

    public Word(String[] symbolsArray) {
        this(Arrays.asList(symbolsArray).stream().map(Symbol::new).toList());
    }

    public Word(String symbolsString) {
        this(symbolsString.chars().mapToObj(c -> (char)c).toList().stream().map(Symbol::new).toList());
    }

    @Override
    public Symbol[] toArray() {
        return this.toArray(new Symbol[0]);
    }

    @Override
    public String toString() {
        return String.join("", StringWrapper.toStringArray(toArray()));
    }
}
