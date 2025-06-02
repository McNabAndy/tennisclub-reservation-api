package cz.vojtechsika.tennisclub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.vojtechsika.tennisclub.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ReservationDTO is a Data Transfer Object used to capture reservation details from client requests.
 * It includes user information, desired start and end times, the court number, and the type of game.
 *
 * Date‐time fields are formatted as ISO 8601 strings (e.g., "2025-06-15T14:00") for JSON serialization and deserialization.
 *
 * Example JSON representation:
 * <pre>
 * {
 *   "userName": "Jane Doe",
 *   "phoneNumber": "555-6789",
 *   "startTime": "2025-06-15T14:00",
 *   "endTime": "2025-06-15T15:00",
 *   "courtNumber": 101,
 *   "gameType": "DOUBLES"
 * }
 * </pre>
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    /**
     * The full name of the user making the reservation.
     */
    private String userName;

    /**
     * The phone number of the user making the reservation.
     */
    private String phoneNumber;

    /**
     * The desired start date and time of the reservation.
     * Formatted as "yyyy-MM-dd'T'HH:mm" (ISO 8601).
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    /**
     * The desired end date and time of the reservation.
     * Formatted as "yyyy-MM-dd'T'HH:mm" (ISO 8601).
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;

    /**
     * The number of the court to be reserved (e.g., 101, 102).
     */
    private int courtNumber;

    /**
     * The type of game for the reservation (e.g., SINGLES, DOUBLES).
     */
    private GameType gameType;
}
