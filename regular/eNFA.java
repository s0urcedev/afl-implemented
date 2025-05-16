package regular;

import java.util.LinkedHashMap;
import java.util.Map;

import general.automatons.alphabets.Alphabet;
import general.automatons.alphabets.eAlphabet;

import general.automatons.states.State;
import general.automatons.states.States;

import general.automatons.transitions.Transition;
import general.automatons.transitions.TransitionValue;
import general.automatons.transitions.Transitions;

import general.words.Symbol;
import general.words.Word;

// Non-Deterministic Finite State Automaton with epsilon transitions
public class eNFA extends FSA {

    private Map<State, States> eK; // epsilon Kleene-star

    public eNFA(Alphabet s, States q, State q0, Transitions d, States f) {
        super(new eAlphabet(s), q, q0, d, f);
        if (!d.onlyContains(new Class<?>[] {null, Symbol.class})) throw new IllegalArgumentException("eNFA must only contain symbols as transitions");
        eK = new LinkedHashMap<>();
    }

    private static void calculateeKState(State qc, States calculated, Transitions d, Map<State, States> eK) {
        if (d.has(qc, (TransitionValue)null)) {
            eK.put(qc, new States(Transitions.getToStates(d.get(qc, (TransitionValue)null))));
            for (State qt: Transitions.getToStates(d.get(qc, (TransitionValue)null))) {
                if (!calculated.contains(qt)) calculateeKState(qt, calculated, d, eK);
                if (eK.containsKey(qt)) eK.get(qc).addAll(eK.get(qt));
            }
        }
        calculated.add(qc);
    }

    public static void calculateeK(FSA fsa, Map<State, States> eK) {
        States calculated = new States();
        for (State qc: fsa.getStates()) {
            if (!calculated.contains(qc)) calculateeKState(qc, calculated, fsa.d, eK);
        }
    }

    private void calculateeK() {
        calculateeK(this, eK);
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
        if (d.has(qc, (TransitionValue)null)) {
            for (State qt: eK.get(qc)) {
                if (!qc.equals(qt) && dfs(qt, word, wi)) return true;
            }
        }
        return false;
    }

    public boolean checkWord(Word word) {
        calculateeK();
        return dfs(q0, word.toArray(), 0);
    }

    public NFA toNFA() {
        calculateeK();

        Alphabet sn = new Alphabet(s);
        sn.remove(null);
        States qn = new States(q);
        Transitions dn = new Transitions();
        States fn = new States(f);

        for (State qc: d.keySet()) {
            for (TransitionValue a: d.get(qc).keySet()) {
                if (a != null) {
                    for (Transition t: d.get(qc, a)) {
                        dn.add(t);
                    }
                }
            }
            if (d.has(qc, (TransitionValue)null)) {
                for (State qt: eK.get(qc)) {
                    if (f.contains(qt)) fn.add(qc);
                    for (TransitionValue a: d.get(qt).keySet()) {
                        if (a != null) {
                            for (State qtt: Transitions.getToStates(d.get(qt, a))) {
                                dn.add(new Transition(qc, a, qtt));
                            }
                        }
                    }
                }
            }
        }

        return new NFA(sn, qn, q0, dn, fn);
    }

}
