package regular;

import java.util.Arrays;

public class Transition {

    public String q1;
    public String a;
    public String q2;

    public Transition(String q1, String a, String q2) {
        this.q1 = q1;
        this.a = a;
        this.q2 = q2;
    }

    public String[] toArray() {
        return new String[] {q1, a, q2};
    }

    public boolean equals(Transition t) {
        return Arrays.equals(toArray(), t.toArray());
    }

    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

}