package rotn.nightscript.parser;

public class Triplet<T, Z, U>{
    public T first;
    public Z second;
    public U third;
    public Triplet(T first, Z second, U third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    public static <T, Z, U> Triplet<T, Z, U> of(T t, Z z, U u) {
        return new Triplet<T, Z, U>(t, z, u);
    }
}
