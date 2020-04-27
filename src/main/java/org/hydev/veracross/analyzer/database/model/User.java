package org.hydev.veracross.analyzer.database.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hydev.veracross.analyzer.database.DatabaseModel;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeraStudent;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hydev.veracross.analyzer.VAConstants.EMAIL_SUFFIX;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.database.VADatabase.query;
import static org.hydev.veracross.analyzer.utils.L$.l$;

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
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = "va_users")
public class User extends DatabaseModel<User>
{
    /**
     * Create user from student data
     *
     * @param student Student data
     */
    private static User create(VeraStudent student, String username)
    {
        return User.builder()
                .username(username)
                .schoolPersonPk(student.getPersonPk())
                .firstLogin(new Date())
                .lastLogin(new Date())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .nickname(student.getFirstName())
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
    public long id;

    @Column(name = "school_person_pk")
    public Integer schoolPersonPk;

    @NonNull
    @Column(name = "username")
    public String username;

    @NonNull
    @Column(name = "last_login")
    public Date lastLogin;

    @NonNull
    @Column(name = "first_login")
    public Date firstLogin;

    @NonNull
    @Column(name = "first")
    public String firstName;

    @NonNull
    @Column(name = "last")
    public String lastName;

    @Column(name = "nick", length = 32)
    public String nickname;

    @NonNull
    @Column(name = "graduation_year", length = 6)
    public Short graduationYear;

    @Column(name = "groups")
    public String groups;

    @NonNull
    @Column(name = "emails")
    public String emails;

    @NonNull
    @Column(name = "classes")
    public String classes;

    @Column(name = "birthday")
    public String birthday;

    @Column(name = "avatarUrl")
    public String avatarUrl;

    @Column(name = "token", length = LENGTH_TOKEN)
    public String token;

    /**
     * Find user by the username or return null
     *
     * @param username Username
     * @return User
     */
    public static User get(String username) throws IOException
    {
        // Check database
        List<User> users = query(s -> s.createQuery("from User where username=:username", User.class)
                .setParameter("username", username).list());

        // Return
        return l$(users).first();
    }

    /**
     * Find user by the user id or return null
     *
     * @param id User ID
     * @return User
     */
    public static User getById(long id) throws IOException
    {
        // Check database
        List<User> users = query(s -> s.createQuery("from User where id=:id", User.class)
                .setParameter("id", id).list());

        // Return
        return l$(users).first();
    }

    /**
     * Find user by the veracross ID or return null
     *
     * @param id Veracross ID
     * @return User or null
     */
    public static User getByVeracrossPersonPk(long id) throws IOException
    {
        // Check database
        List<User> users = query(s -> s.createQuery("from User where schoolPersonPk=:id", User.class)
                .setParameter("id", id).list());

        // Return
        return l$(users).first();
    }

    /**
     * Register user
     *
     * @param username Username
     */
    public static User register(String username, VeracrossHttpClient client) throws IOException
    {
        // Get person pk
        Integer personPk = client.getCourses().getPersonPk();

        // Get user data from Veracross
        VeraStudent student = l$(client.getDirectoryStudents()).find(s -> s.getPersonPk().equals(personPk));

        // Create user
        return User.create(student, username).insert();
    }
}
