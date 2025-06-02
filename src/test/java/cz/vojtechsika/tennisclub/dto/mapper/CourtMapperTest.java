package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CourtMapperTest {

    private CourtMapper courtMapper;

    @BeforeEach
    void setUp() {
        courtMapper = new CourtMapper();
    }

    @Test
    @DisplayName("Map from CourtDTO to Court")
    void toEntity() {

        // Arrange
        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(101);

        // Act
        Court actualCourt = courtMapper.toEntity(courtDTO);

        // Assert
        assertEquals(courtDTO.getCourtNumber(), actualCourt.getCourtNumber(),
                "Court number mismatch");

        assertFalse(actualCourt.isDeleted(),
                "Deleted court should not be true");
    }

    @Test
    @DisplayName("Map Court and SurfaceTypeResponse to CourtResponseDTO")
    void toResponseDTO() {

        //Arrange
        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(101);

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(2L);
        surfaceTypeResponseDTO.setName("Grass");
        surfaceTypeResponseDTO.setMinutePrice(BigDecimal.valueOf(10));

        //Act
        CourtResponseDTO actualCourtResponse = courtMapper.toResponseDTO(court, surfaceTypeResponseDTO);

        // Assert

        assertEquals(court.getId(), actualCourtResponse.getId(),"Court id mismatch");
        assertEquals(court.getCourtNumber(), actualCourtResponse.getCourtNumber(),"Court number mismatch");
        assertEquals(surfaceTypeResponseDTO, actualCourtResponse.getSurfaceTypeResponseDTO());
    }
}