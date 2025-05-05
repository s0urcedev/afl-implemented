package regular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Transitions extends HashMap<String, HashMap<String, ArrayList<String>>> {

    public Transitions() {
        super();
    }

    public Transitions(Collection<Transition> transitionsCollection) {
        this();
        for (Transition t: new HashSet<>(transitionsCollection)) {
            if (!this.containsKey(t.q1)) this.put(t.q1, new HashMap<>());
            if (!this.get(t.q1).containsKey(t.a)) this.get(t.q1).put(t.a, new ArrayList<>());
            this.get(t.q1).get(t.a).add(t.q2);
        }
    }

    public Transitions(Transition[] transitionsArray) {
        this(Arrays.asList(transitionsArray));
    }

    public boolean hasTransitions(String q1, String a) {
        if (!this.containsKey(q1)) return false;
        if (!this.get(q1).containsKey(a)) return false;
        if (this.get(q1).get(a).size() == 0) return false;
        return true;
    }

    public Transition[] getTransitions(String q1, String a) {
        if (!hasTransitions(q1, a)) throw new IllegalArgumentException("Transitions do not exist");
        ArrayList<Transition> res = new ArrayList<>();
        for (String q2: this.get(q1).get(a)) {
            res.add(new Transition(q1, a, q2));
        }
        return res.toArray(new Transition[0]);
    }

    public Transition[] getAllTransitions() {
        ArrayList<Transition> res = new ArrayList<>();
        for (String q1: this.keySet()) {
            for (String a: this.get(q1).keySet()) {
                for (String q2: this.get(q1).get(a)) {
                    res.add(new Transition(q1, a, q2));
                }
            }
        }
        return res.toArray(new Transition[0]);
    }

    public String[] getToStates(String q1, String a) {
        if (!hasTransitions(q1, a)) throw new IllegalArgumentException("Transitions do not exist");
        return this.get(q1).get(a).toArray(new String[0]);
    }

    public boolean isDeterministic() {
        for (String q1: this.keySet()) {
            for (String a: this.get(q1).keySet()) {
                if (this.get(q1).get(a).size() > 1) return false;
            }
        }
        return true;
    }

    public boolean fitsStates(States q) {
        for (String q1: this.keySet()) {
            if (!q.contains(q1)) return false;
            for (String a: this.get(q1).keySet()) {
                for (String q2: this.get(q1).get(a)) {
                    if (!q.contains(q2)) return false;
                }
            }
        }
        return true;
    }

    public boolean fitsAlphabet(Alphabet s) {
        for (String q1: this.keySet()) {
            for (String a: this.get(q1).keySet()) {
                if (!s.contains(a)) return false;
            }
        }
        return true;
    }

}