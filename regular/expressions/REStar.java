package regular.expressions;

import general.Symbol;
import regular.Alphabet;

public class REStar extends RE {
    
    private RE value;

    public REStar(RE value) {
        this.value = value;
    }

    public RE getValue() {
        return value;
    }

    public String toString() {
        String valueS = value.toString();
        if (!(value instanceof RESymbol)) valueS = "(" + valueS + ")";
        return valueS + "*";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        REStar rc = (REStar)o;
        return value.equals(rc.getValue());
    }

    public boolean fits(Alphabet s) {
        return value.fits(s);
    }

    public boolean matches(Symbol[] word, int si, int ei) {
        if (ei < si) return true;
        for (int i = si; i <= ei; i ++) {
            if (value.matches(word, si, i) && this.matches(word, i + 1, ei)) {
                return true;
            }
        }
        return false;
    }

}
