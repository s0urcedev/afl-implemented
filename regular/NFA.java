package regular;

import java.util.Queue;

import general.StringWrapper;

import general.alphabets.Alphabet;

import general.states.State;
import general.states.States;

import general.transitions.Transition;
import general.transitions.Transitions;

import general.words.Symbol;
import general.words.Word;

import java.util.LinkedList;

// Non-Deterministic Finite State Automaton
public class NFA extends FSA {
    
    public NFA(Alphabet s, States q, State q0, Transitions d, States f) {
        super(s, q, q0, d, f);
        if (s.hasEpsilon()) throw new IllegalArgumentException("Alphabet must not contain epsilons");
        if (d.hasEpsilon()) throw new IllegalArgumentException("NFA must not have epsilon transitions");
        if (!d.onlyContains(new Class<?>[] {Symbol.class})) throw new IllegalArgumentException("NFA must only contain symbols as transitions");
    }

    private boolean dfs(State qc, Symbol[] word, int wi) {
        if (wi == word.length) {
            if (f.contains(qc)) return true;
        } else {
            Symbol w = word[wi];
            if (w == null) return dfs(qc, word, wi + 1);
            if (d.has(qc, w)) {
                for (State qt: Transitions.getToStates(d.get(qc, w))) {
                    if (dfs(qt, word, wi + 1)) return true;
                }
            }
        }
        return false;
    }

    public boolean checkWord(Word word) {
        return dfs(q0, word.toArray(), 0);
    }

    public DFA toDFA() {
        Alphabet sn = new Alphabet(s);
        States qn = new States();
        Transitions dn = new Transitions();
        States fn = new States();

        Queue<State[]> queue = new LinkedList<>();
        queue.add(new State[] {q0});
        while (!queue.isEmpty()) {
            State[] qc = queue.remove();
            qn.add(String.join("", StringWrapper.toStringArray(qc)));
            for (State qcc: qc) {
                if (f.contains(qcc)) {
                    fn.add(String.join("", StringWrapper.toStringArray(qc)));
                    break;
                }
            }

            for (Symbol a: s) {
                States qcn = new States();
                for (State qcc: qc) {
                    if (d.has(qcc, a)) {
                        for (State qcc2: Transitions.getToStates(d.get(qcc, a))) {
                            qcn.add(qcc2);
                        }
                    }
                }
                if (!qcn.isEmpty()) {
                    dn.add(new Transition(new State(String.join("", StringWrapper.toStringArray(qc))), a, new State(String.join("", StringWrapper.toStringArray(qcn.toArray(new State[0]))))));
                    if (!qn.contains(new State(String.join("", StringWrapper.toStringArray(qcn.toArray(new State[0])))))) queue.add(qcn.toArray(new State[0]));
                }
            }
        }

        return new DFA(sn, qn, q0, dn, fn);
    }
}
