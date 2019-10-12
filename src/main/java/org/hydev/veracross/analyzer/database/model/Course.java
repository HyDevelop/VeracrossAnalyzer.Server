package org.hydev.veracross.analyzer.database.model;

import lombok.*;

import javax.persistence.*;

/**
 * TODO: Write a description for this class!
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
@Entity
@Table(name = "va_courses")
@NamedQueries(
{
    @NamedQuery(name="byId", query="from Course where id=:id"),
})
public class Course
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "assignmentsId")
    private int assignmentsId;

    @Column(name = "name")
    private String name;

    @Column(name = "teacher")
    private String teacher;
}
