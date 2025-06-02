package cz.vojtechsika.tennisclub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * CourtDTO is a Data Transfer Object used to carry court data between client requests and the service layer.
 * It contains the court number and the associated surface type ID, which are required to create or update a court.
 *
 * This DTO is typically used in REST controller methods to accept JSON input from clients when creating
 * or updating court entities.
 *
 * Example JSON representation:
 * <pre>
 * {
 *   "courtNumber": 101,
 *   "surfaceTypeId": 5
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtDTO {

    /**
     * The number assigned to this court (e.g., 101, 102).
     */
    private int courtNumber;


    /**
     * The identifier of the surface type associated with this court.
     * This must correspond to an existing SurfaceType entity.
     */
    private Long surfaceTypeId;

}
