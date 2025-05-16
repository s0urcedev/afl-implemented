package regular.expressions;

import general.alphabets.Alphabet;

import general.words.Symbol;

public class RESymbol extends RE {
    
    private final Symbol value;

    public RESymbol(Symbol value) {
        this.value = value;
    }

    public RESymbol(String value) {
        this(new Symbol(value));
    }

    public Symbol getValue() {
        return value;
    }

    public String toString() {
        return getValue().toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RESymbol rc = (RESymbol)o;
        return value.equals(rc.getValue());
    }

    public boolean fits(Alphabet s) {
        return s.contains(value);
    }

    public boolean matches(Symbol[] word, int si, int ei) {
        if (si != ei) return false;
        return value.equals(word[si]);
    }

}
