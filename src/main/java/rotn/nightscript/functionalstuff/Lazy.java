package rotn.nightscript.functionalstuff;

public interface Lazy<T, U> {
    T eval(U u);
}
