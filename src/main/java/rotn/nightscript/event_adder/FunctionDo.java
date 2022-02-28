package rotn.nightscript.event_adder;

@FunctionalInterface
public interface FunctionDo<Z, T> {
    Z accept(T ...t);
}
