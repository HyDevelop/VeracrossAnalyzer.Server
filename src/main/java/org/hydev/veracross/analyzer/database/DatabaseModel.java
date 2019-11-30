package org.hydev.veracross.analyzer.database;

/**
 * General database model class. Contains methods that are useful in all
 * database model classes.
 * <p>
 * Class created by the HyDEV Team on 2019-11-29!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-29 20:48
 */
public abstract class DatabaseModel<T>
{
    /**
     * Save or update
     *
     * @return Self
     */
    public T save()
    {
        VADatabase.save(this);
        return (T) this;
    }

    /**
     * Save or update
     *
     * @return Self
     */
    public T insert()
    {
        VADatabase.insert(this);
        return (T) this;
    }
}
