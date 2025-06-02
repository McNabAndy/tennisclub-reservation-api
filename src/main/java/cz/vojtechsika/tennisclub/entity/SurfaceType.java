package cz.vojtechsika.tennisclub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * SurfaceType represents a type of court surface (e.g., Clay, Grass) in the system.
 * It contains the surface's name, price per minute, a soft-deletion flag, and a list of
 * associated {@link Court} entities that use this surface type.
 *
 * <p>The {@code deleted} flag allows soft deletion of a surface type without removing
 * its record from the database.</p>
 *
 * <p>Associations:</p>
 * <ul>
 *   <li>One-to-many relationship with {@link Court}: a surface type can be applied to multiple courts.</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "surface_type")
public class SurfaceType {

    /**
     * The primary key and unique identifier for this surface type.
     * Generated automatically by the database (IDENTITY strategy).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    /**
     * The name of the surface type (e.g., "Clay", "Grass"). Cannot be null.
     */
    @Column(name= "name", nullable = false)
    private String name;

    /**
     * The price per minute for using this surface type. Cannot be null.
     */
    @Column(name= "minute_price", nullable = false)
    private BigDecimal minutePrice;

    /**
     * A boolean flag indicating whether this surface type is marked as deleted (soft deletion).
     * If true, the surface type is considered removed from active use but remains in the database.
     */
    @Column(name= "deleted", nullable = false)
    private boolean deleted;

    /**
     * One-to-many association to {@link Court}, representing all courts that use this surface type.
     * Uses LAZY fetching to defer loading the courts list until accessed. Cascade settings allow persisting,
     * merging, detaching, and refreshing court entities when operating on a surface type.
     */
    @OneToMany(mappedBy = "surfaceType", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})  // tady to zkontrolovat zkontrolovat
    private List<Court> courts;

}
