package org.hydev.veracross.analyzer.database.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.sdk.model.VeracrossStudent;

import javax.persistence.*;
import java.util.Date;

import static org.hydev.veracross.analyzer.VAConstants.EMAIL_SUFFIX;

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
    public static User create(VeracrossStudent student, String username)
    {
        return User.builder()
                .username(username)
                .schoolPersonPk(student.getPersonPk())
                .firstLogin(new Date())
                .lastLogin(new Date())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .nickname(student.getFullName())
                .graduationYear(student.getGraduationYear())
                .groups("student")
                .emails(student.getEmail() == null ? username + EMAIL_SUFFIX : student.getEmail())
                .classes(student.getAllClasses())
                .birthday(student.getBirthday())
                .avatarUrl(null)
                .token(null)
                .build();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "school_person_pk")
    private Integer schoolPersonPk;

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

    @Column(name = "nick", length = 32)
    private String nickname;

    @NonNull
    @Column(name = "graduation_year", length = 6)
    private Short graduationYear;

    @Column(name = "groups")
    private String groups;

    @NonNull
    @Column(name = "emails")
    private String emails;

    @NonNull
    @Column(name = "classes")
    private String classes;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "avatarUrl")
    private String avatarUrl;

    @Column(name = "token")
    private String token;

    /**
     * Save or update
     *
     * @return Self
     */
    public User save()
    {
        VADatabase.saveOrUpdate(this);
        return this;
    }
}
