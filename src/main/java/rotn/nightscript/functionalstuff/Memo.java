package rotn.nightscript.functionalstuff;

import java.util.HashMap;
import java.util.Map;

public class Memo<T, U> implements Lazy<T, U> {
    private Lazy<T, U> lazy;  // a lazy expression, eval sets it to null
    private T memo = null; // the memorandum of the previous value
    private Map<U, T> rememberMeMap = new HashMap<>();
    public Memo( Lazy<T, U> lazy ) { // constructor
        this.lazy = lazy;
    }

    public T eval(U u) {
        if(rememberMeMap.containsKey(u)) {
            return rememberMeMap.get(u);
        } else {
            memo = lazy.eval(u);
            rememberMeMap.put(u, memo);
            return memo;
        }
    }
}