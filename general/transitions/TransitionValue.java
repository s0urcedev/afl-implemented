package general.transitions;

import general.alphabets.Alphabet;

public interface TransitionValue {

    public boolean fits(Alphabet s);
    
    public String toString();
}