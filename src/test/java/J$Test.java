import lombok.AllArgsConstructor;
import lombok.Data;

import static org.hydev.veracross.analyzer.utils.J$.null$;

/**
 * Tester for J$
 * <p>
 * Class created by the HyDEV Team on 2019-11-29!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-29 21:53
 */
public class J$Test
{
    public static void main(String[] args)
    {
        // 随机 50% 几率是 null!
        A a = Math.random() < 0.5 ? null : new A(new B(123), "456", 789);

        // 这样写会报错 NullPointer
        // System.out.println("The number is: " + a.number);
        // System.out.println("The other number is: " + a.b.anotherNumber);

        // 这样就不会啦w! 这样中间任何地方是 null 都只会返回 null
        System.out.println("The number is: " + null$(a, v -> v.number));
        System.out.println("The other number is: " + null$(a, v -> v.b.anotherNumber));
    }

    @Data
    @AllArgsConstructor
    private static class A
    {
        public B b;
        public String someString;
        public int number;
    }

    @Data
    @AllArgsConstructor
    private static class B
    {
        public int anotherNumber;
    }
}
