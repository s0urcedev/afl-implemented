package regular.expressions;

import general.Symbol;
import regular.Alphabet;

public class REConcat extends RE {
    
    private RE left;
    private RE right;

    public REConcat(RE left, RE right) {
        this.left = left;
        this.right = right;
    }

    public RE getLeft() {
        return left;
    }

    public RE getRight() {
        return right;
    }

    public String toString() {
        String leftS = left.toString();
        if (left instanceof REUnion) leftS = "(" + leftS + ")";

        String rightS = right.toString();
        if (right instanceof REUnion) rightS = "(" + rightS + ")";

        return leftS + rightS;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        REConcat rc = (REConcat)o;
        return left.equals(rc.getLeft()) && right.equals(rc.getRight());
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
