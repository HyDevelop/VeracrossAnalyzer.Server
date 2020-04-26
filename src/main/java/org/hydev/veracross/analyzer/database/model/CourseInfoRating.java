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
}
