package regular;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import general.StringWrapper;

import general.automatons.alphabets.Alphabet;
import general.automatons.Automaton;
import general.automatons.states.State;
import general.automatons.states.States;

import general.automatons.transitions.Transition;
import general.automatons.transitions.TransitionValue;
import general.automatons.transitions.Transitions;

import general.words.Symbol;

import regular.expressions.RE;
import regular.expressions.RESymbol;

// Finite State Automaton
public abstract class FSA extends Automaton {
    
    protected Alphabet s;
    protected States q;
    protected State q0;
    protected Transitions d;
    protected States f;

    public FSA(Alphabet s, States q, State q0, Transitions d, States f) {
        if (q.isEmpty()) throw new IllegalArgumentException("States set cannot be empty");
        if (!q.contains(q0)) throw new IllegalArgumentException("States set must contain initial state");
        if (!d.fits(q)) throw new IllegalArgumentException("States set must contain all states from transitions");
        if (!d.fits(s)) throw new IllegalArgumentException("Alphabet must contain all letters from transitions");
        if (!q.containsAll(f)) throw new IllegalArgumentException("States set must contain all final states");
        if (!d.onlyContains(new Class<?>[] {null, Symbol.class, RE.class})) throw new IllegalArgumentException("FSA must only contain symbols or REs as transitions");

        this.s = new Alphabet(s);
        this.q = new States(q);
        this.q0 = q0;
        this.d = new Transitions(d);
        this.f = new States(f);
    }

    public Alphabet getAlphabet() {
        return new Alphabet(s);
    }

    public States getStates() {
        return new States(q);
    }

    public State getInitialState() {
        return q0;
    }

    public Transitions getTransitions() {
        return new Transitions(d);
    }

    public States getFinalStates() {
        return new States(f);
    }

    public void printTransitionTable() {
        Set<TransitionValue> sc = new LinkedHashSet<>();
        sc.addAll(s);
        sc.addAll(Arrays.asList(Transitions.getTransitionValues(d.getAll())));
        String[][] transitionTable = new String[q.size() + 1][sc.size() + 1];
        int[] mxColWidth = new int[sc.size() + 1];
        int i = 1;
        for (TransitionValue a: sc) {
            if (a == null) {
                transitionTable[0][i] = "null";
            } else {
                transitionTable[0][i] = a.toString();
                if (a instanceof RE) {
                    transitionTable[0][i] = "RE(" + transitionTable[0][i] + ")";
                }
            }
            mxColWidth[i] = Math.max(mxColWidth[i], transitionTable[0][i].length());
            i ++;
        }

        i = 1;
        for (State qc: q) {
            transitionTable[i][0] = qc.toString();
            mxColWidth[0] = Math.max(mxColWidth[0], transitionTable[i][0].length());
            int j = 1;
            for (TransitionValue a: sc) {
                if (d.has(qc, a)) {
                    transitionTable[i][j] = String.join(",", StringWrapper.toStringArray(Transitions.getToStates(d.get(qc, a))));
                    mxColWidth[j] = Math.max(mxColWidth[j], transitionTable[i][j].length());
                }
                j ++;
            }
            i ++;
        }

        for (i = 0; i < q.size() + 1; i ++) {
            for (int j = 0; j < sc.size() + 1; j ++) {
                if (transitionTable[i][j] == null) {
                    for (int k = 0; k < mxColWidth[j]; k ++) {
                        System.out.print(" ");
                    }
                } else {
                    System.out.print(transitionTable[i][j]);
                    for (int k = 0; k < mxColWidth[j] - transitionTable[i][j].length(); k ++) {
                        System.out.print(" ");
                    }
                }
                if (j != sc.size()) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }

    public GFA toGFA() {
        String qiName = "qi";
        String qfName = "qf";
        while (q.contains(new State(qiName)) || q.contains(new State(qfName))) {
            qiName = "_" + qiName;
            qfName = "_" + qfName;
        }
        State q0n = new State(qiName);
        State qfn = new State(qfName);

        Alphabet sn = new Alphabet(s);
        States qn = new States();
        qn.add(q0n);
        qn.addAll(q);
        qn.add(qfn);
        States fn = new States(new State[] {qfn});

        Transitions dn = new Transitions();
        for (State q1: d.keySet()) {
            for (TransitionValue a: d.get(q1).keySet()) {
                RE re;
                if (a == null) {
                    re = null;
                } else if (a instanceof Symbol) {
                    re = new RESymbol((Symbol)a);
                } else {
                    re = (RE)a;
                }
                for (State q2: d.get(q1).get(a)) {
                    if (dn.has(q1, q2)) {
                        RE reEx = (RE)dn.get(q1, q2)[0].a;
                        dn.remove(q1, q2);
                        dn.add(new Transition(q1, RE.unite(new RE[] {reEx, re}), q2));
                    } else {
                        dn.add(new Transition(q1, re, q2));
                    }
                }
            }
        }

        dn.add(new Transition(q0n, null, q0));
        for (State qf: f) {
            dn.add(new Transition(qf, null, qfn));
        }

        return new GFA(sn, qn, q0n, dn, fn);
    }

}
