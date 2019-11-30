package org.hydev.veracross.analyzer.database.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hydev.veracross.analyzer.database.DatabaseModel;
import org.hydev.veracross.analyzer.database.VADatabase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Database model for courses.
 * <p>
 * Class created by the HyDEV Team on 2019-10-09!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-09 16:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = "va_courses")
public class Course extends DatabaseModel<Course>
{
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "teacher")
    private String teacher;

    /**
     * Get course by id
     *
     * @param id ID
     * @return Course
     */
    public static Course get(long id)
    {
        return VADatabase.query(s -> s
                .createQuery("from Course where id=:id", Course.class)
                .setParameter("id", id)
                .getSingleResult());
    }
}
