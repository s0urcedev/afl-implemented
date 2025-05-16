package general.automatons.transitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import general.automatons.alphabets.Alphabet;

import general.automatons.states.State;
import general.automatons.states.States;

public class Transitions extends LinkedHashMap<State, LinkedHashMap<TransitionValue, States>> {

    public Transitions() {
        super();
    }

    public Transitions(Collection<Transition> transitionsCollection) {
        this();
        for (Transition t: new LinkedHashSet<>(transitionsCollection)) {
            if (!this.containsKey(t.q1)) this.put(t.q1, new LinkedHashMap<>());
            if (!this.get(t.q1).containsKey(t.a)) this.get(t.q1).put(t.a, new States());
            this.get(t.q1).get(t.a).add(t.q2);
        }
    }

    public Transitions(Transition[] transitionsArray) {
        this(Arrays.asList(transitionsArray));
    }

    public Transitions(Transitions transitions) {
        this(Arrays.asList(transitions.getAll()));
    }

    public void add(Transition t) {
        if (!this.containsKey(t.q1)) this.put(t.q1, new LinkedHashMap<>());
        if (!this.get(t.q1).containsKey(t.a)) this.get(t.q1).put(t.a, new States());
        this.get(t.q1).get(t.a).add(t.q2);
    }

    public boolean has(State q1, TransitionValue a) {
        if (!this.containsKey(q1)) return false;
        if (!this.get(q1).containsKey(a)) return false;
        if (this.get(q1).get(a).size() == 0) return false;
        return true;
    }

    public boolean has(State q1, State q2) {
        if (!this.containsKey(q1)) return false;
        for (TransitionValue a: this.get(q1).keySet()) {
            for (State qt: this.get(q1).get(a)) {
                if (qt.equals(q2)) return true;
            }
        }
        return false;
    }

    public boolean has(Transition t) {
        if (!this.has(t.q1, t.a)) return false;
        for (State qt: this.get(t.q1).get(t.a)) {
            if (qt.equals(t.q2)) return true;
        }
        return false;
    }

    public Transition[] get(State q1, TransitionValue a) {
        if (!has(q1, a)) throw new IllegalArgumentException("Transitions do not exist");
        ArrayList<Transition> res = new ArrayList<>();
        for (State q2: this.get(q1).get(a)) {
            res.add(new Transition(q1, a, q2));
        }
        return res.toArray(new Transition[0]);
    }

    public Transition[] get(State q1, State q2) {
        if (!has(q1, q2)) throw new IllegalArgumentException("Transitions do not exist");
        ArrayList<Transition> res = new ArrayList<>();
        for (TransitionValue a: this.get(q1).keySet()) {
            for (State qt: this.get(q1).get(a)) {
                if (qt.equals(q2)) {
                    res.add(new Transition(q1, a, q2));
                }
            }
        }
        return res.toArray(new Transition[0]);
    }

    public Transition[] getAll() {
        ArrayList<Transition> res = new ArrayList<>();
        for (State q1: this.keySet()) {
            for (TransitionValue a: this.get(q1).keySet()) {
                for (State q2: this.get(q1).get(a)) {
                    res.add(new Transition(q1, a, q2));
                }
            }
        }
        return res.toArray(new Transition[0]);
    }

    public static State[] getFromStates(Transition[] ts) {
        State[] res = new State[ts.length];
        for (int i = 0; i < ts.length; i ++) {
            res[i] = ts[i].q1;
        }
        return res;
    }

    public static State[] getToStates(Transition[] ts) {
        State[] res = new State[ts.length];
        for (int i = 0; i < ts.length; i ++) {
            res[i] = ts[i].q2;
        }
        return res;
    }

    public static TransitionValue[] getTransitionValues(Transition[] ts) {
        TransitionValue[] res = new TransitionValue[ts.length];
        for (int i = 0; i < ts.length; i ++) {
            res[i] = ts[i].a;
        }
        return res;
    }

    public Transition[] getOutgoing(State q1) {
        ArrayList<Transition> res = new ArrayList<>();
        if (this.containsKey(q1)) {
            for (TransitionValue a: this.get(q1).keySet()) {
                for (State q2: this.get(q1).get(a)) {
                    res.add(new Transition(q1, a, q2));
                }
            }
        }
        return res.toArray(new Transition[0]);
    }

    public Transition[] getIncoming(State q2) {
        ArrayList<Transition> res = new ArrayList<>();
        for (State q1: this.keySet()) {
            for (TransitionValue a: this.get(q1).keySet()) {
                for (State qt: this.get(q1).get(a)) {
                    if (qt.equals(q2)) {
                        res.add(new Transition(q1, a, q2));
                    }
                }
            }
        }
        return res.toArray(new Transition[0]);
    }

    public void remove(State q1, TransitionValue a) {
        if (!has(q1, a)) throw new IllegalArgumentException("Transitions do not exist");
        this.get(q1).remove(a);
        if (this.get(q1).isEmpty()) this.remove(q1);
    }

    public void remove(State q1, State q2) {
        if (!has(q1, q2)) throw new IllegalArgumentException("Transitions do not exist");
        for (TransitionValue a: new LinkedHashSet<>(this.get(q1).keySet())) {
            for (State qt: new LinkedHashSet<>(this.get(q1).get(a))) {
                if (qt.equals(q2)) {
                    this.get(q1).get(a).remove(q2);
                }
            }
            if (this.get(q1).get(a).isEmpty()) this.get(q1).remove(a);
        }
        if (this.get(q1).isEmpty()) this.remove(q1);
    }

    public void remove(Transition t) {
        if (!has(t)) throw new IllegalArgumentException("Transition do not exist");
        this.get(t.q1).get(t.a).remove(t.q2);
        if (this.get(t.q1).get(t.a).isEmpty()) this.get(t.q1).remove(t.a);
        if (this.get(t.q1).isEmpty()) this.remove(t.q1);
    }

    public boolean hasEpsilon() {
        for (State q1: this.keySet()) {
            for (TransitionValue a: this.get(q1).keySet()) {
                if (a == null) return true;
            }
        }
        return false;
    }

    public boolean isDeterministic() {
        for (State q1: this.keySet()) {
            for (TransitionValue a: this.get(q1).keySet()) {
                if (this.get(q1).get(a).size() > 1) return false;
            }
        }
        return true;
    }

    public boolean isGeneralised() {
        LinkedHashMap<State, LinkedHashMap<State, Boolean>> transitionExist = new LinkedHashMap<>();
        for (State q1: this.keySet()) {
            for (TransitionValue a: this.get(q1).keySet()) {
                for (State q2: this.get(q1).get(a)) {
                    if (!transitionExist.containsKey(q1)) transitionExist.put(q1, new LinkedHashMap<>());
                    if (transitionExist.get(q1).containsKey(q2)) return false;
                    transitionExist.get(q1).put(q2, true);
                }
            }
        }
        return true;
    }

    public boolean fits(States q) {
        for (State q1: this.keySet()) {
            if (!q.contains(q1)) return false;
            for (TransitionValue a: this.get(q1).keySet()) {
                for (State q2: this.get(q1).get(a)) {
                    if (!q.contains(q2)) return false;
                }
            }
        }
        return true;
    }

    public boolean fits(Alphabet s) {
        for (State q1: this.keySet()) {
            for (TransitionValue a: this.get(q1).keySet()) {
                if (a != null) {
                    if (!a.fits(s)) return false;
                } else {
                    if (!s.contains(null)) return false;
                }
            }
        }
        return true;
    }

    public boolean onlyContains(Class<?>[] classes) {
        for (State q1: this.keySet()) {
            for (TransitionValue a: this.get(q1).keySet()) {
                boolean contained = false;
                for (Class<?> c: classes) {
                    if (c != null) {
                        if (c.isInstance(a)) {
                            contained = true;
                            break;
                        }
                    } else {
                        if (a == null) {
                            contained = true;
                            break;
                        }
                    }
                }
                if (!contained) return false;
            }
        }
        return true;
    }
}