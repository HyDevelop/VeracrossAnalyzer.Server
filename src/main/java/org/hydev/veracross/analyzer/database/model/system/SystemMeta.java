package org.hydev.veracross.analyzer.database.model.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hydev.veracross.analyzer.database.VADatabase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The entries in this database represents meta settings / information.
 * <p>
 * Class created by the HyDEV Team on 2019-11-27!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-27 22:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "va_system_meta")
public class SystemMeta
{
    // ID list
    public static final int ID_VERSION_BUILD = 0;
    public static final int ID_VERSION = 1;

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    /**
     * Get entry by id
     *
     * @param id ID
     * @return Value
     */
    public static String get(int id)
    {
        return VADatabase.query(session -> session.createQuery("from SystemMeta where id=:id", SystemMeta.class)
                .setParameter("id", id).getSingleResult().name);
    }

    /**
     * Get entry by id
     *
     * @param id ID
     * @param name Name
     * @param value Value
     */
    public static void set(int id, String name, String value)
    {
        VADatabase.saveOrUpdate(new SystemMeta(id, name, value));
    }
}
