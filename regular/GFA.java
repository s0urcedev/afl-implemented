package regular;

import java.util.LinkedHashMap;
import java.util.Map;

import general.alphabets.Alphabet;

import general.states.State;
import general.states.States;

import general.transitions.Transition;
import general.transitions.TransitionValue;
import general.transitions.Transitions;

import general.words.Symbol;
import general.words.Word;

import regular.expressions.RE;
import regular.expressions.REStar;

// Generalised Finite State Automaton
public class GFA extends FSA {

    private Map<State, States> eK;
    
    public GFA(Alphabet s, States q, State q0, Transitions d, States f) {
        super(s, q, q0, d, f);
        if (!d.isGeneralised()) throw new IllegalArgumentException("GFA must have at most one transition between every to states");
        if (d.getIncoming(q0).length != 0) throw new IllegalArgumentException("GFA must have no incoming transitions into the initial state");
        if (f.size() != 1) throw new IllegalArgumentException("GFA must have a unique final state");
        if (d.getOutgoing(f.getFirst()).length != 0) throw new IllegalArgumentException("GFA must have no outgoing transitions from the final state");
        if (!d.onlyContains(new Class<?>[] {null, RE.class})) throw new IllegalArgumentException("GFA must only contain REs as transitions");
        eK = new LinkedHashMap<>();
    }

    private void calculateeK() {
        eNFA.calculateeK(this, eK);
    }

    private boolean dfs(State qc, Symbol[] word, int wi) {
        if (wi == word.length) {
            if (f.contains(qc)) return true;
        } else {
            if (d.containsKey(qc)) {
                for (TransitionValue a: d.get(qc).keySet()) {
                    if (a != null) {
                        RE re = (RE)a;
                        for (int i = wi; i < word.length; i ++) {
                            if (re.matches(word, wi, i)) {
                                for (State qt: Transitions.getToStates(d.get(qc, a))) {
                                    if (dfs(qt, word, i + 1)) return true;
                                }
                            }
                        }
                    }
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

    public RE toRE() {
        Transitions dn = new Transitions(d);
        for (State qc: q) {
            if (q0.equals(qc) || f.contains(qc)) continue;
            RE rei = null;
            if (dn.has(qc, qc)) {
                rei = new REStar((RE)Transitions.getTransitionValues(dn.get(qc, qc))[0]);
            }
            for (Transition ti: dn.getIncoming(qc)) {
                State qi = ti.q1;
                if (qc.equals(qi)) continue;
                for (Transition to: dn.getOutgoing(qc)) {
                    State qo = to.q2;
                    if (qc.equals(qo)) continue;
                    RE ren = RE.concat(new RE[] {
                        (RE)ti.a,
                        rei,
                        (RE)to.a,
                    });
                    if (dn.has(qi, qo)) {
                        ren = RE.unite(new RE[] {
                            ren,
                            (RE)dn.get(qi, qo)[0].a
                        });
                        dn.remove(qi, qo);
                    }
                    dn.add(new Transition(qi, ren, qo));
                }
            }
            for (State qi: Transitions.getFromStates(dn.getIncoming(qc))) {
                dn.remove(qi, qc);
            }
            for (State qo: Transitions.getFromStates(dn.getIncoming(qc))) {
                dn.remove(qc, qo);
            }
        }
        return (RE)Transitions.getTransitionValues(dn.get(q0, f.getFirst()))[0];
    }

}
