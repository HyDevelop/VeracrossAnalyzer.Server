package org.hydev.veracross.analyzer.database.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hydev.veracross.analyzer.database.DatabaseModel;
import org.hydev.veracross.analyzer.database.VADatabase;

import javax.persistence.*;
import java.util.List;

/**
 * A database table to store peer ratings of courses
 * <p>
 * Class created by the HyDEV Team on 2020-04-20!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-04-20 22:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = "va_courses_info")
public class CourseInfoRating extends DatabaseModel<CourseInfoRating>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rating")
    private long id_rating;

    @Column(name = "id_ci")
    private int id_ci;

    @Column(name = "id_user")
    private long id_user;

    @Column(name = "user")
    private String user;

    @Column(name = "anonymous")
    private boolean anonymous;

    // Is the course is enjoyable?
    @Column(name = "course_enjoyable")
    private Short courseEnjoyable;

    // Is the knowledge interesting or important?
    // Is it something you feel worth learning?
    @Column(name = "course_knowledge")
    private Short courseKnowledge;

    // Is the teacher interesting or boring?
    @Column(name = "teacher_interesting")
    private Short teacherInteresting;

    // Is the teacher easy to understand?
    @Column(name = "teacher_eloquence")
    private Short teacherEloquence;

    // How fair is the grading, is credit given in proportion to work?
    @Column(name = "teacher_fair")
    private Short teacherFair;

    @Column(name = "comment", length = 5000)
    private String comment;

    public static List<CourseInfoRating> getByUser(long user)
    {
        return VADatabase.query(s -> s.createQuery("from CourseInfoRating where user = :user")
            .setParameter("user", user).list());
    }
}
