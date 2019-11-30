package org.hydev.veracross.analyzer.database.model;

import lombok.*;
import org.hydev.veracross.analyzer.database.DatabaseModel;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2019-10-09!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-09 16:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "va_access_log")
public class AccessLog extends DatabaseModel<AccessLog>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NonNull
    @Column(name = "user")
    private String user;

    @NonNull
    @Column(name = "action")
    private String action;

    @NonNull
    @Column(name = "details")
    private String details;

    @NonNull
    @Column(name = "time")
    private Date time;

    /**
     * Record an access log
     *
     * @param user Username
     * @param action Action
     * @param details Details
     */
    public static void record(String user, String action, String details)
    {
        new AccessLog(user, action, details, new Date()).save();
    }
}
