package cz.vojtechsika.tennisclub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/**
 * User represents an application user who makes reservations. It contains user details such as phone number,
 * user name, a soft-deletion flag, and a list of associated {@link Reservation} entities.
 *
 * <p>The {@code deleted} flag allows soft deletion of a user without removing their record from the database.</p>
 *
 * <p>Associations:</p>
 * <ul>
 *   <li>One-to-many relationship with {@link Reservation}: a user can make multiple reservations.</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="app_user")
public class User {

    /**
     * The primary key and unique identifier for this user.
     * Generated automatically by the database (IDENTITY strategy).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    /**
     * The phone number of the user. Must be unique and not null.
     */
    @Column(name= "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    /**
     * The display name of the user. Cannot be null.
     */
    @Column(name= "user_name", nullable = false)
    private String userName;

    /**
     * A boolean flag indicating whether this user is marked as deleted (soft deletion).
     * If {@code true}, the user is considered removed from active use but remains in the database.
     */
    @Column(name= "deleted", nullable = false)
    private boolean deleted;


    /**
     * One-to-many association to {@link Reservation}, representing all reservations made by this user.
     * Uses LAZY fetching to defer loading the reservations list until accessed. Cascade settings allow
     * persisting, merging, detaching, and refreshing reservation entities when operating on a user.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Reservation> reservations;
}
