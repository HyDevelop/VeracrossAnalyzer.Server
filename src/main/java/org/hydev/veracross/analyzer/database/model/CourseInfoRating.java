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
@Table(name = "va_courses_info_rating")
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

    @Column(name = "person_pk")
    private long personPk;

    @Column(name = "username")
    private String username;

    @Column(name = "user_full_name")
    private String userFullName;

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
        return VADatabase.query(s -> s.createQuery("from CourseInfoRating where id_user = :id_user")
            .setParameter("id_user", user).list());
    }

    public static List<CourseInfoRating> getByPersonPk(long personPk)
    {
        return VADatabase.query(s -> s.createQuery("from CourseInfoRating where personPk = :personPk")
            .setParameter("personPk", personPk).list());
    }

    public static List<CourseInfoRating> getByCourse(int id_ci)
    {
        return VADatabase.query(s -> s.createQuery("from CourseInfoRating where id_ci = :id_ci")
            .setParameter("id_ci", id_ci).list());
    }

    public static List<CourseInfoRating> getCommentsByCourse(int id_ci)
    {
        return VADatabase.query(s -> s.createQuery("from CourseInfoRating where id_ci = :id_ci and LENGTH(comment) > 0")
            .setParameter("id_ci", id_ci).list());
    }

    public static List<CourseInfoRating> getByUserAndCourse(long user, int id_ci)
    {
        return VADatabase.query(s -> s.createQuery("from CourseInfoRating where id_user = :id_user and id_ci = :id_ci")
            .setParameter("id_user", user).setParameter("id_ci", id_ci).list());
    }

    @Data
    @AllArgsConstructor
    public static class ReturnedRating
    {
        int id_ci;
        long id_user;
        String userFullName;
        Short[] ratings;
        String comment;

        public ReturnedRating(CourseInfoRating rating)
        {
            this(rating.id_ci,
                rating.anonymous ? -1 : rating.id_user,
                rating.anonymous ? "Anonymous]=[Student" : rating.userFullName,
                new Short[]{rating.courseEnjoyable, rating.courseKnowledge,
                rating.teacherInteresting, rating.teacherEloquence, rating.teacherFair},
                rating.comment);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ObtainedRating
    {
        int id_ci;
        boolean anonymous;
        Short[] ratings;
        String comment;

        public CourseInfoRating toCourseInfoRating(CourseInfoRating rating)
        {
            return rating.id_ci(id_ci).comment(comment).anonymous(anonymous)
                .courseEnjoyable(ratings[0]).courseKnowledge(ratings[1]).teacherInteresting(ratings[2])
                .teacherEloquence(ratings[3]).teacherFair(ratings[4]);
        }
    }
}
