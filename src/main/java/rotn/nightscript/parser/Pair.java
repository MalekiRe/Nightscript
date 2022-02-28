package rotn.nightscript.parser;

public class Pair <T, Z>{
    public T first;
    public Z second;
    public Pair(T first, Z second) {
        this.first = first;
        this.second = second;
    }
    public static <T, Z>Pair<T, Z> of(T t, Z z) {
        return new Pair<T, Z>(t, z);
    }
}
