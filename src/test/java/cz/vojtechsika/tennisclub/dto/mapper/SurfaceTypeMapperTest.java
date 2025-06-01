package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
class SurfaceTypeMapperTest {


    private SurfaceTypeMapper surfaceTypeMapper;

    @BeforeEach
    void setUp() {
        surfaceTypeMapper = new SurfaceTypeMapper();
    }

    @Test
    @DisplayName("Map SurfaceTypeDTO to SurfaceType")
    void toEntity_returnSurfaceType() {

        // Arrange
        SurfaceTypeDTO surfaceTypeDTO = new SurfaceTypeDTO();
        surfaceTypeDTO.setName("Grass");
        surfaceTypeDTO.setMinutePrice(BigDecimal.valueOf(10));

        // Act
        SurfaceType actualSurfaceType = surfaceTypeMapper.toEntity(surfaceTypeDTO);

        // Assert
        assertEquals(surfaceTypeDTO.getName(), actualSurfaceType.getName(),
                "Surface type name mismatch");
        assertEquals(surfaceTypeDTO.getMinutePrice(), actualSurfaceType.getMinutePrice(),
                "Surface type minute price mismatch");
        assertFalse(actualSurfaceType.isDeleted(), "Surface type is not deleted");

    }

    @Test
    @DisplayName("Map SurfaceType to SurfaceTypeResponseDTO")
    void toResponseDTO_returnSurfaceTypeResponseDTO() {

        // Arrange
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setId(1L);
        surfaceType.setName("Grass");
        surfaceType.setMinutePrice(BigDecimal.valueOf(10));

        // Act
        SurfaceTypeResponseDTO actualSurfaceTypeResponseDTO = surfaceTypeMapper.toResponseDTO(surfaceType);

        // Assert
        assertEquals(surfaceType.getId(), actualSurfaceTypeResponseDTO.getId(),
                "Surface type id mismatch");
        assertEquals(surfaceType.getName(), actualSurfaceTypeResponseDTO.getName(),
                "Surface type name mismatch");
        assertEquals(surfaceType.getMinutePrice(), actualSurfaceTypeResponseDTO.getMinutePrice(),
                "Surface type minute price mismatch");
    }
}