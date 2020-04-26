package org.hydev.veracross.analyzer.database.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hydev.veracross.analyzer.database.DatabaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

import static org.hydev.veracross.analyzer.database.VADatabase.query;

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
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "level", length = 8)
    private String level;

    @Column(name = "id_ci")
    private Integer id_ci;

    /**
     * Get course by id
     *
     * @param id ID
     * @return Course
     */
    public static Course get(int id)
    {
        return query(s -> s.createQuery("from Course where id=:id", Course.class)
            .setParameter("id", id).getSingleResult());
    }

    /**
     * Get courses by ids
     *
     * @param ids IDs
     * @return Course
     */
    public static List<Course> getAll(int... ids)
    {
        return query(s -> s.createQuery("from Course where id = (:id)", Course.class)
            .setParameter("id", ids).list());
    }

    /**
     * Get all courses
     *
     * @return Courses
     */
    public static List<Course> getAll()
    {
        return query(s -> s.createQuery("from Course", Course.class).list());
    }
}
