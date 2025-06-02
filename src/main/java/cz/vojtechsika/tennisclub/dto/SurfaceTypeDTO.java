package cz.vojtechsika.tennisclub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * SurfaceTypeDTO is a Data Transfer Object used to carry surface type data between client requests and the service layer.
 * It contains the unique identifier (if present), name, and price per minute for a surface type.
 * This DTO is typically used in REST controller methods to accept or return JSON input/output when creating,
 * updating, or retrieving surface type entities.
 * Example JSON representation:
 * <pre>
 * {
 *   "id": 5,
 *   "name": "Clay",
 *   "minutePrice": 1.50
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurfaceTypeDTO {




    /**
     * The unique identifier of the surface type.
     * This field may be null when creating a new surface type, and populated when performing updates or retrievals.
     */
    //private Long id;

    /**
     * The name of the surface type (e.g., "Clay", "Grass").
     */
    private String name;

    /**
     * The price per minute for using this surface type, as a {@link BigDecimal}.
     */
    private BigDecimal minutePrice;



}
