package org.hydev.veracross.analyzer.database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Hibernate stuff.
 * https://www.javaguides.net/2018/11/hibernate-hello-world-tutorial.html
 * <p>
 * Class created by the HyDEV Team on 2019-10-09!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-09 17:53
 */
public class HibernateUtils
{
    private static StandardServiceRegistry registry;

    private static SessionFactory sessionFactory;

    /**
     * Get session factory
     *
     * @return Session factory
     */
    public static SessionFactory getSessionFactory()
    {
        if (sessionFactory == null)
        {
            try
            {
                // Create registry
                registry = new StandardServiceRegistryBuilder().configure().build();

                // Create MetadataSources
                MetadataSources sources = new MetadataSources(registry);

                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            }
            catch (Exception e)
            {
                e.printStackTrace();

                if (registry != null)
                {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Destroy session factory
     */
    public static void shutdown()
    {
        if (registry != null)
        {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
