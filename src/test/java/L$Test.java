import lombok.AllArgsConstructor;
import org.hydev.veracross.analyzer.utils.L$;

import static org.hydev.veracross.analyzer.utils.L$.l$;

/**
 * TODO: Write a description for this class!
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

        // JS: let age = list.find(user => user.name == 'User2').age
        int age = list.find(user -> user.name.equals("User2")).age;
    }

    @AllArgsConstructor
    public static class User
    {
        public String name;
        public int age;
    }
}
