package general;

import regular.Alphabet;

public interface TransitionValue {

    public boolean fits(Alphabet s);
    
    public String toString();
}