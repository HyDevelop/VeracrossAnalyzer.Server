package org.hydev.veracross.analyzer.database.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hydev.veracross.analyzer.database.DatabaseModel;
import org.hydev.veracross.analyzer.database.VADatabase;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2020-04-11!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-04-11 22:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = "va_courses_info")
public class CourseInfo extends DatabaseModel<CourseInfo>
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ci")
    private int id_ci;

    @Column(name = "year") // School year
    private int year;

    @Column(name = "name")
    private String name;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "level", length = 8)
    private String level;

    @Column(name = "course_ids")
    private String courseIds;

    /**
     * Get course ids to list
     *
     * @return Course ids
     */
    public List<Integer> getCourseIds()
    {
        return stream(courseIds.split("\\|")).map(Integer::parseInt).collect(Collectors.toList());
    }

    /**
     * Set course ids (without saving)
     *
     * @param courseIds Course ids
     */
    public void setCourseIds(List<Integer> courseIds)
    {
        this.courseIds = courseIds.stream().map(Objects::toString).collect(joining("|"));
    }

    /**
     * Add a course id (without saving)
     *
     * @param courseId Course id
     */
    public void addCourseId(Integer courseId)
    {
        if (!this.courseIds.equals("")) this.courseIds += "|";
        this.courseIds += courseId;
    }
}
