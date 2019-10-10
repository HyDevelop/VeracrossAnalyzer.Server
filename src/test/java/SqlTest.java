import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hydev.veracross.analyzer.database.HibernateUtils;
import org.hydev.veracross.analyzer.database.User;

import java.util.List;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2019-10-09!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-09 20:34
 */
public class SqlTest
{
    public static void main(String[] args)
    {
        User user = new User("test");

        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession())
        {
            // start a transaction
            transaction = session.beginTransaction();
            // save the student objects
            session.save(user);
            // commit transaction
            transaction.commit();
        }
        catch (Exception e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        try (Session session = HibernateUtils.getSessionFactory().openSession())
        {
            List<User> students = session.createQuery("from User", User.class).list();
            students.forEach(System.out::println);
        }
        catch (Exception e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
