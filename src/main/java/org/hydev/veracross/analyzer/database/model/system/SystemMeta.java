package org.hydev.veracross.analyzer.database.model.system;

import lombok.*;
import lombok.experimental.Accessors;
import org.hydev.veracross.analyzer.database.DatabaseModel;
import org.hydev.veracross.analyzer.database.VADatabase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.lang.Integer.parseInt;

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
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = "va_system_meta")
public class SystemMeta extends DatabaseModel<SystemMeta>
{
    // ID list
    public static final int ID_VERSION_BUILD = 0;
    public static final int ID_MAINTENANCE = 1;

    @Id
    @Column(name = "id")
    public int id;

    @Column(name = "name")
    public String name;

    @Column(name = "value")
    public String value;

    /**
     * Get entry by id
     *
     * @param id ID
     * @return Value
     */
    public static SystemMeta get(int id)
    {
        return VADatabase.query(s -> s
                .createQuery("from SystemMeta where id=:id", SystemMeta.class)
                .setParameter("id", id)
                .getSingleResult());
    }

    /**
     * Set entry by id
     *
     * @param id ID
     * @param name Name
     * @param value Value
     */
    public static void set(int id, String name, String value)
    {
        VADatabase.save(new SystemMeta(id, name, value));
    }

    public static int getBuildVersion()
    {
        String currentVersionString = SystemMeta.get(ID_VERSION_BUILD).value;
        return currentVersionString == null ? -1 : parseInt(currentVersionString);
    }

    public static void setBuildVersion(int version)
    {
        set(ID_VERSION_BUILD, "Build Version", "" + version);
    }

    public static String getMaintenance()
    {
        return get(ID_MAINTENANCE).value;
    }

    public static void setMaintenance(String maintenance)
    {
        set(ID_MAINTENANCE, "Maintenance", maintenance);
    }
}
