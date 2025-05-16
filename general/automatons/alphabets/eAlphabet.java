package general.automatons.alphabets;

import java.util.Collection;

import general.words.Symbol;

// Alphabet with epsilon
public class eAlphabet extends Alphabet {
    
    public eAlphabet() {
        super();
        super.add((Symbol)null);
    }

    public eAlphabet(Collection<Symbol> symbolsCollection) {
        super(symbolsCollection);
        super.addFirst((Symbol)null);
    }

    public eAlphabet(Symbol[] symbolsArray) {
        super(symbolsArray);
        super.addFirst((Symbol)null);
    }

    public eAlphabet(String[] symbolsArray) {
        super(symbolsArray);
        super.addFirst((Symbol)null);
    }

}
