package org.hydev.veracross.analyzer.database.model.msg;

import lombok.*;
import lombok.experimental.Accessors;
import org.hydev.veracross.analyzer.database.DatabaseModel;

import javax.persistence.*;
import java.util.Date;

/**
 * This is the database model for user messages
 * <p>
 * Class created by the HyDEV Team on 2019-11-14!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-14 17:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = "va_msg_user")
@NamedQueries(
{
        @NamedQuery(name="messageByUser", query="from UserMessage where senderId=:senderId"),
})
public class UserMessage extends DatabaseModel<UserMessage>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "sender_id")
    private int senderId;

    @Column(name = "target_id")
    private int targetId;

    @Column(name = "time_sent")
    private Date timeSent;

    @Column(name = "time_read")
    private Date timeRead;
}
