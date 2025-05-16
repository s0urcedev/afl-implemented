package general.words;

import general.StringWrapper;

import general.automatons.alphabets.Alphabet;

import regular.expressions.RE;

public class Symbol extends StringWrapper implements RE {

    public Symbol(String value) {
        super(value);
    }

    public Symbol(char value) {
        super(String.valueOf(value));
    }

    public boolean fits(Alphabet s) {
        return s.contains(this);
    }

    public boolean matches(Symbol[] word, int si, int ei) {
        if (si != ei) return false;
        return value.equals(word[si].value);
    }

}
