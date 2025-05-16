package regular.expressions;

import general.automatons.alphabets.Alphabet;

import general.words.Symbol;

public class REUnion extends RE {
    
    public final RE left;
    public final RE right;

    public REUnion(RE left, RE right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return left.toString() + "+" + right.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        REUnion rc = (REUnion)o;
        return left.equals(rc.left) && right.equals(rc.right);
    }

    public boolean fits(Alphabet s) {
        return left.fits(s) && right.fits(s);
    }

    public boolean matches(Symbol[] word, int si, int ei) {
        return left.matches(word, si, ei) || right.matches(word, si, ei);
    }

}
