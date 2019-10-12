package org.hydev.veracross.analyzer.database;

import org.hibernate.Session;
import org.hibernate.Transaction;

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
    public static void start(TransactionOperation operation)
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
}