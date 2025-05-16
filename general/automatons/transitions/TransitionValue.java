package general.automatons.transitions;

import general.automatons.alphabets.Alphabet;

public interface TransitionValue {

    public boolean fits(Alphabet s);
    
    public String toString();
}