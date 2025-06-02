package cz.vojtechsika.tennisclub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Court represents a tennis court in the system. Each court has a unique court number,
 * a flag indicating if it has been deleted, an associated {@link SurfaceType}, and a list of
 * {@link Reservation} entities that reference this court.
 *
 * <p>The {@code deleted} flag allows soft deletion of a court without removing its record
 * from the database.</p>
 *
 * <p>Associations:</p>
 * <ul>
 *   <li>Many-to-one relationship with {@link SurfaceType}: each court is linked to one surface type.</li>
 *   <li>One-to-many relationship with {@link Reservation}: a court can have multiple reservations.</li>
 * </ul>
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="court")
public class Court {


    /**
     * The primary key and unique identifier for this court.
     * Generated automatically by the database (IDENTITY strategy).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    /**
     * The assigned number of the court (e.g., 101, 102). This must be unique and not null.
     */
    @Column(name= "court_number", nullable = false, unique = true)
    private int courtNumber;

    /**
     * A boolean flag indicating whether this court is marked as deleted (soft deletion).
     * If {@code true}, the court is considered removed from active use but remains in the database.
     */
    @Column(name= "deleted", nullable = false)
    private boolean deleted;

    /**
     * Many-to-one association to {@link SurfaceType}, representing the type of surface
     * (e.g., Clay, Grass) for this court. Uses LAZY fetching to defer loading until accessed.
     * Cascade settings allow persisting, merging, detaching, and refreshing surface type data
     * when operating on a court.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "surface_type_id")
    private SurfaceType surfaceType;


    /**
     * One-to-many association to {@link Reservation}, representing all reservations
     * made for this court. Uses LAZY fetching to defer loading the reservations list until accessed.
     * Cascade settings allow persisting, merging, detaching, and refreshing reservation data
     * when operating on a court.
     */
    @OneToMany(mappedBy = "court", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Reservation> reservations;
}
