package rotn.nightscript.functionalstuff;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

public class FuncArgPair {
    public final Memo memo;
    public final Collection arguments;

    public FuncArgPair(Memo memo, Object ...arguments) {
        this.memo = memo;
        this.arguments = Arrays.asList(arguments);
    }

    public Object evaluate(Event event) {
        try {
            return memo.eval(event, arguments);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String toString() {
        return "[Memo: " + memo + ", Args:" + arguments + "]";
    }
}
