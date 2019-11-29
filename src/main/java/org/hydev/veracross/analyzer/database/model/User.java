package org.hydev.veracross.analyzer.database.model;

import lombok.*;
import org.hydev.veracross.sdk.model.VeracrossStudent;

import javax.persistence.*;
import java.util.Date;

/**
 * User database model
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
@Entity
@Table(name = "va_users")
@NamedQueries(
{
    @NamedQuery(name="byUsername", query="from User where username=:username"),
})
public class User
{
    /**
     * Create user from student data
     *
     * @param student Student data
     */
    public User(VeracrossStudent student, String token)
    {
        this("username", new Date(), new Date(), student.getFirstName(), student.getLastName(), student.getFullName(),
                student.getGraduationYear(), "student", student.getEmail(), student.getAllClasses(), student.getBirthday(),
                student.getPhotoUrl(), token);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @Column(name = "username")
    private String username;

    @NonNull
    @Column(name = "last_login")
    private Date lastLogin;

    @NonNull
    @Column(name = "first_login")
    private Date firstLogin;

    @NonNull
    @Column(name = "first")
    private String firstName;

    @NonNull
    @Column(name = "last")
    private String lastName;

    @NonNull
    @Column(name = "nick", length = 32)
    private String nickname;

    @NonNull
    @Column(name = "graduation_year", length = 6)
    private Short graduationYear;

    @NonNull
    @Column(name = "groups")
    private String groups;

    @NonNull
    @Column(name = "emails")
    private String emails;

    @NonNull
    @Column(name = "classes")
    private String classes;

    @NonNull
    @Column(name = "birthday")
    private String birthday;

    @NonNull
    @Column(name = "avatarUrl")
    private String avatarUrl;

    @NonNull
    @Column(name = "token")
    private String token;
}
