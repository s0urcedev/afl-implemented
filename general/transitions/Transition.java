package general.transitions;

import java.util.Arrays;

import general.states.State;

public class Transition {

    public final State q1;
    public final TransitionValue a;
    public final State q2;

    public Transition(State q1, TransitionValue a, State q2) {
        this.q1 = q1;
        this.a = a;
        this.q2 = q2;
    }

    public String[] toStringArray() {
        return new String[] {q1.toString(), a != null ? a.toString() : null, q2.toString()};
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition t = (Transition)o;
        return q1.equals(t.q1) && (a != null ?a.equals(t.a) : t.a == null) && q2.equals(t.q2);
    }

    public int hashCode() {
        return Arrays.hashCode(toStringArray());
    }

    public String toString() {
        return "( " + q1.toString() + ", " + a.toString() + ", " + q2.toString() + " )";
    }

}