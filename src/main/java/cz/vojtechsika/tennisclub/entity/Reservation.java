package cz.vojtechsika.tennisclub.entity;

import cz.vojtechsika.tennisclub.enums.GameType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Reservation represents a booking of a tennis court by a user. It contains details about
 * the reservation period, creation timestamp, price, game type, and associations to the
 * corresponding {@link User} and {@link Court}. A soft-deletion flag allows marking a
 * reservation as deleted without removing its record from the database.
 *
 * <p>Associations:</p>
 * <ul>
 *   <li>Many-to-one relationship with {@link User}: each reservation is made by one user.</li>
 *   <li>Many-to-one relationship with {@link Court}: each reservation is for one court.</li>
 * </ul>

 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "reservation")
public class Reservation {

    /**
     * The primary key and unique identifier for this reservation.
     * Generated automatically by the database (IDENTITY strategy).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    /**
     * The start date and time for the reservation. Cannot be null.
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * The end date and time for the reservation. Cannot be null.
     */
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * The timestamp when the reservation record was created. Cannot be null.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The price charged for this reservation. Cannot be null.
     */
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * The type of game for this reservation (e.g., SINGLES, DOUBLES).
     * Stored as a string in the database. Cannot be null.
     */
    @Column(name = "game_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameType gameType;

    /**
     * A boolean flag indicating whether this reservation is marked as deleted (soft deletion).
     * If true, the reservation is considered removed from active use but remains in the database.
     */
    @Column(name= "deleted", nullable = false)
    private boolean deleted;

    /**
     * Many-to-one association to {@link User}, representing the user who made this reservation.
     * Uses LAZY fetching to defer loading until accessed. Cascades persist, merge, detach, and refresh
     * operations to the associated user entity.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "app_user_id")
    private User user;

    /**
     * Many-to-one association to {@link Court}, representing the court reserved by this reservation.
     * Uses LAZY fetching to defer loading until accessed. Cascades persist, merge, detach, and refresh
     * operations to the associated court entity.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "court_id")
    private Court court;


}
