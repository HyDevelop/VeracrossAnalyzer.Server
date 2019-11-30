import lombok.AllArgsConstructor;
import org.hydev.veracross.analyzer.utils.L$;

import static org.hydev.veracross.analyzer.utils.L$.l$;

/**
 * Tester for L$
 * <p>
 * Class created by the HyDEV Team on 2019-11-14!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-14 22:40
 */
public class L$Test
{
    public static void main(String[] args)
    {
        // L$ 语法糖示范w!
        // JS: let list = [{name: 'User1', age: 14}, {name: 'User2', age: 15}, {name: 'MyCat', age: 99999}]
        L$<User> list = l$(new User("User1", 14), new User("User2", 15), new User("MyCat", 99999));

        // JS: let find = list.find(user => user.name == 'User2')
        User find = list.find(user -> user.name.equals("User2"));

        // JS: list.forEach((value, i) => console.log(`${value} - ${i}`))
        list.forEach((value, i) -> System.out.println(value + " - " + i));

        // JS: list.forEach((value, i, self) => console.log(`${value} - ${i} of ${self}`))
        list.forEach((value, i, self) -> System.out.println(value + " - " + i + " of " + self));

        // 比 JS 更甜的语法糖w
        // JS: let last = list[list.length - 1]
        User last = list.last();
    }

    @AllArgsConstructor
    public static class User
    {
        public String name;
        public int age;
    }
}
