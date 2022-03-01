package rotn.nightscript.functionalstuff;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Memo<T, U> implements Lazy<T, U> {
    private Lazy<T, U> lazy;  // a lazy expression, eval sets it to null
    private T memo = null; // the memorandum of the previous value
    private Map<U, T> rememberMeMap = new HashMap<>();
    public Memo( Lazy<T, U> lazy ) { // constructor
        this.lazy = lazy;
    }

    @Override
    public T eval(Event event, U u) throws InvocationTargetException, IllegalAccessException {
        if(rememberMeMap.containsKey(u)) {
            return rememberMeMap.get(u);
        } else {
            memo = lazy.eval(event, u);
            rememberMeMap.put(u, memo);
            return memo;
        }
    }
}