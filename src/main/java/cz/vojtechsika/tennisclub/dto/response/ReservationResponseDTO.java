package cz.vojtechsika.tennisclub.dto.response;

import cz.vojtechsika.tennisclub.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


/**
 * ReservationResponseDTO is a Data Transfer Object used to send reservation details in API responses.
 * It contains information about a reservation, including its unique ID, court number, user details,
 * game date and times, game type, price, and creation timestamp.
 *
 * This DTO is typically constructed by the service layer and returned by REST controllers
 * when a client requests information about a reservation.
 *
 * Example JSON representation:
 * <pre>
 * {
 *   "id": 10,
 *   "courtNumber": 101,
 *   "userName": "John Doe",
 *   "phoneNumber": "123456789",
 *   "startTime": "14:00",
 *   "endTime": "15:00",
 *   "gameDate": "06/15/2025",
 *   "gameType": "SINGLES",
 *   "price": 15.00,
 *   "createdAt": "06/01/2025"
 * }
 * </pre>
 *
 * All date and time fields are formatted as localized SHORT style strings.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

    /**
     * The unique identifier of the reservation.
     */
    private Long id;


    /**
     * The number of the court reserved (e.g., 101, 102).
     */
    private int courtNumber;

    /**
     * The full name of the user who made the reservation.
     */
    private String userName;

    /**
     * The phone number of the user who made the reservation.
     */
    private String phoneNumber;

    /**
     * The start time of the reservation, formatted as a localized SHORT time string.
     * Example: "14:00"
     */
    private String startTime;

    /**
     * The end time of the reservation, formatted as a localized SHORT time string.
     * Example: "15:00"
     */
    private String endTime;

    /**
     * The date of the game, formatted as a localized SHORT date string.
     * Example: "06/15/2025"
     */
    private String gameDate;
    /**
     * The type of game for the reservation (e.g., SINGLES, DOUBLES).
     */
    private GameType gameType;

    /**
     * The price of the reservation, in the appropriate currency.
     */
    private BigDecimal price;

    /**
     * The creation date of the reservation, formatted as a localized SHORT date string.
     * Example: "06/01/2025"
     */
    private String createdAt;
}
