package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.entity.User;
import org.springframework.stereotype.Component;


/**
 * UserMapper is responsible for converting data from a {@link ReservationDTO} to a {@link User} entity.
 * This mapper is used when creating or updating a user based on reservation information.
 *
 * When mapping, the {@code phoneNumber} and {@code userName} fields are copied from the DTO,
 * and the {@code deleted} flag is initialized to {@code false}.
 */
@Component
public class UserMapper {

    /**
     * Creates a new {@link User} entity based on information from a {@link ReservationDTO}.
     * The resulting {@code User} will have its phone number and user name set from the DTO,
     * and its {@code deleted} flag initialized to {@code false}.
     *
     * @param reservationDTO The {@link ReservationDTO} containing user-related data (phone number and user name).
     * @return A new {@link User} entity populated with values from {@code reservationDTO}.
     */
    public User mapFromReservationDTO(ReservationDTO reservationDTO){
        User user = new User();
        user.setPhoneNumber(reservationDTO.getPhoneNumber());
        user.setUserName(reservationDTO.getUserName());
        user.setDeleted(false);
        return user;
    }


}
