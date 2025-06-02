package cz.vojtechsika.tennisclub.enums;

/**
 * GameType represents the type of tennis game for a reservation.
 * <p>
 * Two types of games are supported:
 * <ul>
 *   <li>{@link #SINGLES} – a one-on-one match.</li>
 *   <li>{@link #DOUBLES} – a two-on-two match.</li>
 * </ul>
 * </p>
 * <p>
 * This enum is used in reservation entities and DTOs to specify whether a booking is for singles or doubles play.
 * </p>
 */
public enum GameType {
    SINGLES,
    DOUBLES
}
