package regular;

import general.*;
import general.States;
import general.Symbol;
import general.Transitions;
import general.Word;

// Deterministic Finite State Automaton
public class DFA extends FSA {
    
    public DFA(Alphabet s, States q, State q0, Transitions d, States f) {
        super(s, q, q0, d, f);
        if (s.hasEpsilon()) throw new IllegalArgumentException("Alphabet must not contain epsilons");
        if (d.hasEpsilon()) throw new IllegalArgumentException("DFA must not have epsilon transitions");
        if (!d.onlyContains(new Class<?>[] {Symbol.class})) throw new IllegalArgumentException("DFA must only contain symbols as transitions");
        if (!d.isDeterministic()) throw new IllegalArgumentException("DFA must be deterministic");
    }

    public boolean checkWord(Word word) {
        State qc = q0;
        for (Symbol w: word) {
            if (!d.has(qc, w)) return false;
            qc = Transitions.getToStates(d.get(qc, w))[0];
        }
        return f.contains(qc);
    }

}