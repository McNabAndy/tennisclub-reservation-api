package cz.vojtechsika.tennisclub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * SurfaceTypeResponseDTO is a Data Transfer Object used to send surface type details in API responses.
 * It contains the unique identifier, name, and price per minute for a surface type.
 *
 * This DTO is typically constructed by the service layer and returned by REST controllers
 * when a client requests information about surface types.
 *
 * Example JSON representation:
 * <pre>
 * {
 *   "id": 5,
 *   "name": "Clay",
 *   "minutePrice": 1.50
 * }
 * </pre>
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurfaceTypeResponseDTO {

    /**
     * The unique identifier of the surface type.
     */
    private Long id;

    /**
     * The name of the surface type (e.g., "Clay", "Grass").
     */
    private String name;

    /**
     * The price per minute for using this surface type.
     */
    private BigDecimal minutePrice;
}
