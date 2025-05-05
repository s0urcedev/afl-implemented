package regular;

// deterministic final state automaton
public class DFA {

    protected Alphabet s;
    protected States q;
    protected String q0;
    protected Transitions d;
    protected States f;
    
    public DFA(Alphabet s, States q, String q0, Transitions d, States f) {
        if (!q.contains(q0)) throw new IllegalArgumentException("States set must contain initial state");
        if (!d.fitsStates(q)) throw new IllegalArgumentException("States set must contain all states from transitions");
        if (!d.fitsAlphabet(s)) throw new IllegalArgumentException("Alphabet must contain all letters from transitions");
        if (!d.isDeterministic()) throw new IllegalArgumentException("FSA must be deterministic");
        if (!q.containsAll(f)) throw new IllegalArgumentException("States set must contain all final states");

        this.s = s;
        this.q = q;
        this.q0 = q0;
        this.d = d;
        this.f = f;
    }

    public Alphabet getAlphabet() {
        return new Alphabet(s);
    }

    public States getStates() {
        return new States(q);
    }

    public String getInitialState() {
        return q0;
    }

    public States getFinalStates() {
        return new States(f);
    }

    public boolean checkWord(String[] word) {
        String qc = q0;
        for (String w: word) {
            if (!d.hasTransitions(qc, w)) return false;
            qc = d.getToStates(qc, w)[0];
        }
        return f.contains(qc);
    }

}