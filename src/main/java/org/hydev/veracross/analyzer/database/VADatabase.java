package org.hydev.veracross.analyzer.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hydev.veracross.analyzer.database.model.AccessLog;

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
     * Record an access log
     *
     * @param user Username
     * @param action Action
     * @param details Details
     */
    public static void accessLog(String user, String action, String details)
    {
        VADatabase.transaction(s -> s.save(new AccessLog(user, action, details)));
    }
}
