package rotn.nightscript.functionalstuff;

import java.util.Arrays;
import java.util.Collection;

public class FuncArgPair {
    public final Memo memo;
    public final Collection arguments;

    public FuncArgPair(Memo memo, Object ...arguments) {
        this.memo = memo;
        this.arguments = Arrays.asList(arguments);
    }

    public Object evaluate() {
        return memo.eval(arguments);
    }
}
