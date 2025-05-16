package general;

import java.util.Arrays;
import java.util.Collection;

public class StringWrapper {
    
    public final String value;

    public StringWrapper(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static String[] toStringArray(Collection<StringWrapper> wrapped) {
        return wrapped.stream().map(x -> x.toString()).toList().toArray(new String[0]);
    }

    public static String[] toStringArray(StringWrapper[] wrapped) {
        return toStringArray(Arrays.asList(wrapped));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringWrapper state = (StringWrapper)o;
        return value.equals(state.toString());
    }

    public int hashCode() {
        return value.hashCode();
    }

}
