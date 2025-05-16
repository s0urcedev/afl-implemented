package general.automatons.states;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public class States extends LinkedHashSet<State> {

    public States() {
        super();
    }

    public States(int numberOfStates) {
        this();
        for (int i = 0; i < numberOfStates; i ++) {
            this.add(new State("q" + i));
        }
    }

    public States(Collection<State> statesCollection) {
        this();
        super.addAll(statesCollection);
    }

    public States(State[] statesArray) {
        this(Arrays.asList(statesArray));
    }

    public States(String[] statesArray) {
        this(Arrays.asList(statesArray).stream().map(State::new).toList());
    }

    public void add(String a) {
        this.add(new State(a));
    }

}