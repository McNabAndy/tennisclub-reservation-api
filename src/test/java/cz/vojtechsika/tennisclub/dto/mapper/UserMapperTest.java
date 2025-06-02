package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
class UserMapperTest {

    private UserMapper userMapper;



    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    @DisplayName("Map ReservationDTO to User")
    void mapFromReservationDTO_returnUser() {

        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserName("John");
        reservationDTO.setPhoneNumber("1234567890");

        // Act
        User actualUser = userMapper.mapFromReservationDTO(reservationDTO);

        // Assert
        assertEquals(reservationDTO.getUserName(), actualUser.getUserName(),"User name not equal");
        assertEquals(reservationDTO.getPhoneNumber(), actualUser.getPhoneNumber(),"Phone number not equal");
        assertFalse(actualUser.isDeleted(),"User deleted");
    }
}