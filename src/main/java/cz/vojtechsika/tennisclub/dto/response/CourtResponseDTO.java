package cz.vojtechsika.tennisclub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * CourtResponseDTO is a Data Transfer Object used to send court details in API responses.
 * It contains the court's unique identifier, court number, and associated surface type information.
 *
 * This DTO is typically constructed by the service layer and returned by REST controllers
 * when a client requests information about a court. The {@code surfaceTypeResponseDTO} field
 * provides nested details about the court's surface type.
 *
 * Example JSON representation:
 * <pre>
 * {
 *   "id": 1,
 *   "courtNumber": 101,
 *   "surfaceType": {
 *     "id": 5,
 *     "name": "Clay",
 *     "minutePrice": 1.5
 *   }
 * }
 * </pre>
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtResponseDTO {

    /**
     * The unique identifier of the court.
     */
    private Long id;


    /**
     * The number assigned to this court (e.g., 101, 102).
     */
    private int courtNumber;


    /**
     * The DTO representing the surface type associated with this court.
     * Serialized as the JSON property "surfaceType".
     */
    @JsonProperty("surfaceType")
    private SurfaceTypeResponseDTO surfaceTypeResponseDTO;
}
