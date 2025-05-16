package regular.expressions;

import general.transitions.TransitionValue;

import general.words.Symbol;
import general.words.Word;

// Regular Expression
public abstract class RE implements TransitionValue {

    private static RE toRE(String[] input, int si, int ei) {
        if (si > ei) throw new IllegalArgumentException("Invalid expression passed: Invalid indices");
        if (input[si].equals("+") || input[si].equals("*") || input[si].equals(")"))
            throw new IllegalArgumentException("Invalid expression passed: Invalid symbol");
        if (si == ei) return new RESymbol(input[si]);
        
        int parCount = 0;
        boolean pcWasZero = false;
        int pi = -1;
        for (int i = si; i <= ei; i ++) {
            if (i != si && i != ei && parCount == 0) pcWasZero = true;
            if (input[i].equals("(")) {
                parCount ++;
            } else if (input[i].equals(")")) {
                if (parCount > 0) {
                    parCount --;
                } else {
                    throw new IllegalArgumentException("Invalid expression passed: Invalid parenthesis");
                }
            } else if (input[i].equals("+") && parCount == 0) {
                pi = i;
                break;
            }
        }
        if (parCount != 0) throw new IllegalArgumentException("Invalid expression passed: Invalid parenthesis");
        if (input[si].equals("(") && input[ei].equals(")") && !pcWasZero) return toRE(input, si + 1, ei - 1);

        if (pi != -1) {
            return new REUnion(toRE(input, si, pi - 1), toRE(input, pi + 1, ei));
        } else {
            int ein = si;
            if (input[si].equals("(")) {
                for (int i = si + 1; i <= ei; i ++) {
                    if (input[i].equals(")")) {
                        ein = i;
                    }
                }
                if (ein == si) throw new IllegalArgumentException("Invalid expressin passed: Invalid parenthesis");
            }
            while (ein + 1 <= ei && input[ein + 1].equals("*")) ein ++;
            if (ein == ei && input[ein].equals("*")) {
                return new REStar(toRE(input, si, ein - 1));
            } else {
                return new REConcat(toRE(input, si, ein), toRE(input, ein + 1, ei));
            }
        }
    }

    public static RE toRE(String[] input) {
        return toRE(input, 0, input.length - 1);
    }

    public static RE toRE(String input) {
        return toRE(input.chars().mapToObj(c -> (char)c).toList().stream().map(String::valueOf).toList().toArray(new String[0]));
    }
    
    public abstract String toString();

    public abstract boolean equals(Object o);

    public int hashCode() {
        return toString().hashCode();
    }

    public abstract boolean matches(Symbol[] word, int si, int ei);

    public boolean matches(Symbol[] word) {
        return matches(word, 0, word.length - 1);
    }

    public boolean matches(Word word) {
        return matches(word.toArray());
    }

    public static RE unite(RE[] reArray) {
        if (reArray.length == 0) return null;
        RE res = null;
        for (int i = reArray.length - 1; i >= 0; i --) {
            if (reArray[i] != null) {
                if (res != null) {
                    res = new REUnion(reArray[i], res);
                } else {
                    res = reArray[i];
                }
            }
        }
        return res;
    }

    public static RE concat(RE[] reArray) {
        if (reArray.length == 0) return null;
        RE res = null;
        for (int i = reArray.length - 1; i >= 0; i --) {
            if (reArray[i] != null) {
                if (res != null) {
                    res = new REConcat(reArray[i], res);
                } else {
                    res = reArray[i];
                }
            }
        }
        return res;
    }

}
