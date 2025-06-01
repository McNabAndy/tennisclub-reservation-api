package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapFromReservationDTO(ReservationDTO reservationDTO){
        User user = new User();
        user.setPhoneNumber(reservationDTO.getPhoneNumber());
        user.setUserName(reservationDTO.getUserName());
        user.setDeleted(false);
        return user;
    }


}
