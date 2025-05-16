package general.alphabets;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import general.words.Symbol;

public class Alphabet extends LinkedHashSet<Symbol> {

    public Alphabet() {
        super();
    }

    public Alphabet(Collection<Symbol> symbolsCollection) {
        this();
        super.addAll(symbolsCollection);
    }

    public Alphabet(Symbol[] symbolsArray) {
        this(Arrays.asList(symbolsArray));
    }

    public Alphabet(String[] symbolsArray) {
        this(Arrays.asList(symbolsArray).stream().map(Symbol::new).toList());
    }

    public void add(String a) {
        this.add(new Symbol(a));
    }

    public boolean hasEpsilon() {
        return super.contains(null);
    }

}