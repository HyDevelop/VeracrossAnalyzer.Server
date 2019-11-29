package org.hydev.veracross.analyzer.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hydev.veracross.analyzer.database.model.AccessLog;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeracrossStudent;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hydev.veracross.analyzer.utils.L$.l$;

/**
 * This class is for database operations
 * <p>
 * Class created by the HyDEV Team on 2019-10-09!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-09 21:19
 */
public class VADatabase
{
    /**
     * Start a transaction
     *
     * @param operation Callback
     */
    public static void transaction(TransactionOperation operation)
    {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession())
        {
            // Start a transaction
            transaction = session.beginTransaction();

            // Callback
            operation.callback(session, transaction);

            // Commit transaction
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
    }

    /**
     * Transaction callback for Lambda
     */
    public interface TransactionOperation
    {
        void callback(Session session, Transaction transaction);
    }

    /**
     * Start a transaction
     *
     * @param operation Callback
     */
    public static void transaction(SimpleTransactionOperation operation)
    {
        transaction((s, v) -> operation.callback(s));
    }

    /**
     * Transaction callback for Lambda
     */
    public interface SimpleTransactionOperation
    {
        void callback(Session session);
    }

    /**
     * Get a query
     *
     * @param operation Query operation
     * @param <T> Return type
     * @return Query data
     */
    public static <T> T query(QueryOperation<T> operation)
    {
        try (Session session = HibernateUtils.getSessionFactory().openSession())
        {
            // Callback
            return operation.callback(session);
        }
        catch (NoResultException e)
        {
            return null;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Query operation for lombok
     */
    public interface QueryOperation<T>
    {
        T callback(Session session);
    }

    /**
     * Save or update
     *
     * @param object Database model object
     * @param <T> Type of the object
     */
    public static <T> void saveOrUpdate(T object)
    {
        transaction(s -> s.saveOrUpdate(object));
    }

    /**
     * Record an access log
     *
     * @param user Username
     * @param action Action
     * @param details Details
     */
    public static void accessLog(String user, String action, String details)
    {
        transaction(s -> s.save(new AccessLog(user, action, details, new Date())));
    }

    /**
     * Find user by the username, and register if it didn't find any records
     *
     * @param username Username
     * @param client Veracross HTTP Client
     * @return User
     */
    public static User getUser(String username, VeracrossHttpClient client) throws IOException
    {
        // Check database
        User user;
        List<User> users = query(s -> s.createNamedQuery("byUsername", User.class)
                .setParameter("username", username).list());

        // No user -> Create user
        if (users.size() == 0)
        {
            // Get user data from Veracross
            VeracrossStudent student = l$(client.getDirectoryStudents()).find(s ->
                    s.getEmail().equalsIgnoreCase(username + "@stjohnsprep.org"));

            // Create user
            user = User.create(student);

            // Save
            saveOrUpdate(user);
        }
        else user = users.get(0);

        return user;
    }
}
