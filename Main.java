import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

//Inspirowane zadaniem z https://class.coursera.org/progfun-005 autorstwa Martina Odersky'ego

public class Main {

    interface Set extends Predicate<Integer> {}

    /*
    static BiFunction<Set, Integer, Boolean> contains = new BiFunction<Set, Integer, Boolean>() {
        @Override
        public Boolean apply(Set s, Integer x) {
            return s.test(x);
        }
    };*/
    static BiFunction<Set, Integer, Boolean> contains = (s, x) -> s.test(x);
    static Function<Integer, Set> singletonSet = elem -> (elem::equals);
    static BiFunction<Set, Set, Set> union = (s1, s2) -> (x -> s1.or(s2).test(x));
    static BiFunction<Set, Set, Set> intersection = (s1, s2) -> (x -> s1.and(s2).test(x));
    static BiFunction<Set, Set, Set> diff = (s1, s2) -> (x -> s1.and(s2.negate()).test(x));
    static BiFunction<Set, Predicate<Integer>, Set> filter = (s1, f) -> (x -> s1.and(f).test(x));

    static Boolean forall(Set s, Predicate<Integer> p) {
        class Iter implements Function<Integer, Boolean> {

            @Override
            public Boolean apply(Integer i) {
                if(i == 1001) return true;
                else if(contains.apply(s, i) && !p.test(i)) return false;
                else return apply(i + 1);
            }
        }
        return new Iter().apply(-1000);
    }

    static BiFunction<Set, Predicate<Integer>, Boolean> exists = (s, p) -> !forall(s, p.negate());
    static BiFunction<Set, Function<Integer, Integer>, Set> map =
            (s, f) -> (x -> exists.apply(s, y -> f.apply(y).equals(x)));


    public static void main(String[] args) {
        Set evenNumbers = x -> x % 2 == 0;
        Set filtered = filter.apply(evenNumbers, x -> x < 10 && x > 0);
        printSet(map.apply(filtered, x -> x * x));
    }


    public static void printSet(Set s) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = -1000; i < 1000; i++) {
            if(s.test(i)) {
                sb.append(i);
                sb.append(", ");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        System.out.println(sb.toString());
    }
}
