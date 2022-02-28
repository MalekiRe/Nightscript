package rotn.nightscript.functionalstuff;

public class NonMemo<T, U> extends Memo<T, U> {
    private Lazy<T, U> lazy;  // a lazy expression, eval sets it to null

    public NonMemo( Lazy<T, U> lazy ) {
        super(lazy); // constructor
        this.lazy = lazy;
    }

    @Override
    public T eval(U u) {
        return lazy.eval(u);
    }
}