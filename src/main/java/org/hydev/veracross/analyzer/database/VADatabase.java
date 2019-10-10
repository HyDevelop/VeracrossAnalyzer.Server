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
     * Transaction callback for Lambda
     */
    public interface TransactionOperation
    {
        String callback(Transaction transaction);
    }
}
