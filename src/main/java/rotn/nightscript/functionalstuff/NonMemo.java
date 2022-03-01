package rotn.nightscript.functionalstuff;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.lang.reflect.InvocationTargetException;

public class NonMemo<T, U> extends Memo<T, U> {
    private Lazy<T, U> lazy;  // a lazy expression, eval sets it to null

    public NonMemo( Lazy<T, U> lazy ) {
        super(lazy); // constructor
        this.lazy = lazy;
    }

    @Override
    public T eval(Event event, U u) {
        try {
            return lazy.eval(event, u);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}