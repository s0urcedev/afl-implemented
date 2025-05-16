package regular.expressions;

import general.automatons.alphabets.Alphabet;

import general.words.Symbol;

public class Concat implements RE {
    
    public final RE left;
    public final RE right;

    public Concat(RE left, RE right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        String leftS = left.toString();
        if (left instanceof Union) leftS = "(" + leftS + ")";

        String rightS = right.toString();
        if (right instanceof Union) rightS = "(" + rightS + ")";

        return leftS + rightS;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concat rc = (Concat)o;
        return left.equals(rc.left) && right.equals(rc.left);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean fits(Alphabet s) {
        return left.fits(s) && right.fits(s);
    }

    public boolean matches(Symbol[] word, int si, int ei) {
        for (int i = si - 1; i <= ei; i ++) {
            if (left.matches(word, si, i) && right.matches(word, i + 1, ei)) {
                return true;
            }
        }
        return false;
    }

}
