package general.words;

import general.StringWrapper;

import general.automatons.alphabets.Alphabet;

import general.automatons.transitions.TransitionValue;

public class Symbol extends StringWrapper implements TransitionValue {

    public Symbol(String value) {
        super(value);
    }

    public Symbol(char value) {
        super(String.valueOf(value));
    }

    public boolean fits(Alphabet s) {
        return s.contains(this);
    }

}
