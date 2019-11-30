package org.hydev.veracross.analyzer.database;

/**
 * TODO: Write a description for this class!
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
        VADatabase.saveOrUpdate(this);
        return (T) this;
    }
}
