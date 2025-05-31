package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.mapper.SurfaceTypeMapper;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SurfaceTypeServiceImplTest {

    @Mock
    private SurfaceTypeDAO surfaceTypeDAO;

    @Mock
    private SurfaceTypeMapper surfaceTypeMapper;

    @Mock
    private CourtDAO courtDAO;

    @Mock
    private CourtService courtService;

    @InjectMocks
    private SurfaceTypeServiceImpl surfaceTypeService;


    private SurfaceTypeDTO surfaceTypeDTO;

    private SurfaceTypeResponseDTO surfaceTypeResponseDTO;

    private SurfaceType surfaceType;

    private SurfaceType saveSurfaceType;




    @BeforeEach
    public void setUp() {
        Long surfaceTypeId = 1L;
        String surfaceTypeName = "Clay";
        BigDecimal minutePrice = BigDecimal.valueOf(10);

        // Set SurfaceDTO mock
        surfaceTypeDTO = new SurfaceTypeDTO(
                surfaceTypeId,
                surfaceTypeName,
                minutePrice
        );

        // Set SurfaceResponseDTO mock
       surfaceTypeResponseDTO = new SurfaceTypeResponseDTO(
                surfaceTypeId,
                surfaceTypeName,
                minutePrice
        );

        // Set SurfaceType from mapper mock
        surfaceType = new SurfaceType();
        surfaceType.setId(surfaceTypeId);
        surfaceType.setName(surfaceTypeName);
        surfaceType.setMinutePrice(minutePrice);
        surfaceType.setDeleted(false);

        // Set SurfaceType from saveMethod mock
        saveSurfaceType = new SurfaceType();
        saveSurfaceType.setId(surfaceTypeId);
        saveSurfaceType.setName(surfaceTypeName);
        saveSurfaceType.setMinutePrice(minutePrice);
        saveSurfaceType.setDeleted(false);



    }

    @Test
    @DisplayName("Create new SurfaceType with valid DTO")
    @Order(1)
    void saveSurfaceType_withValidSurfaceTypeDTO_shouldReturnSurfaceTypeResponseDTO() {

        //Arrange
        when(surfaceTypeMapper.toEntity(surfaceTypeDTO)).thenReturn(surfaceType);
        when(surfaceTypeDAO.save(surfaceType)).thenReturn(saveSurfaceType);
        when(surfaceTypeMapper.toResponseDTO(saveSurfaceType)).thenReturn(surfaceTypeResponseDTO);

        //Act
        SurfaceTypeResponseDTO actual = surfaceTypeService.saveSurfaceType(surfaceTypeDTO);

        //Assert
        assertEquals(surfaceTypeResponseDTO, actual,
                "Returned SurfaceTypeResponseDTO should be the same instance as expected.");

    }

    @Test
    @DisplayName("Update SurfaceType with valid DTO and ID")
    @Order(6)
    void updateSurfaceType_withValidSurfaceTypeDTOAndId_shouldReturnSurfaceTypeResponseDTO() {

        // Arrange
        Long existSurfaceTypeId = 1L;
        Optional<SurfaceType> optionalSurfaceType = Optional.of(surfaceType);

        when(surfaceTypeDAO.findById(existSurfaceTypeId)).thenReturn(optionalSurfaceType);
        when(surfaceTypeMapper.toEntity(surfaceTypeDTO)).thenReturn(surfaceType);
        when(surfaceTypeDAO.update(surfaceType)).thenReturn(saveSurfaceType);
        when( surfaceTypeMapper.toResponseDTO(saveSurfaceType)).thenReturn(surfaceTypeResponseDTO);

        // Act
        SurfaceTypeResponseDTO actual = surfaceTypeService.updateSurfaceType(surfaceTypeDTO, existSurfaceTypeId);

        // Assort
        assertEquals(surfaceTypeResponseDTO, actual,
                "Returned SurfaceTypeResponseDTO should be the same instance as expected.");

    }

    @Test
    @DisplayName("Update SurfaceType with invalid ID")
    @Order(7)
    void updateSurfaceType_withInvalidId_shouldReturnSurfaceTypeNotFoundException() {

        // Arrange
        Long nonExistentSurfaceTypeId = 999L;
        Optional<SurfaceType> optionalSurfaceType = Optional.empty();

        when(surfaceTypeDAO.findById(nonExistentSurfaceTypeId)).thenReturn(optionalSurfaceType);


        // Assort and Assert
        assertThrows(SurfaceTypeNotFoundException.class, () ->
                surfaceTypeService.updateSurfaceType(surfaceTypeDTO, nonExistentSurfaceTypeId),
                "Should throw SurfaceTypeNotFoundException");

    }

    @Test
    @DisplayName("Delete SurfaceType by ID and no exited connected courts")
    @Order(8)
    void deleteSurfaceType_validSurfaceTypeIdAndNoExistedCourts_shouldReturnTrue() {

        // Arrange
        Long existSurfaceTypeId = 1L;

        Optional<SurfaceType> optionalSurfaceType = Optional.of(surfaceType);
        List<Court> courts = List.of();

        when(surfaceTypeDAO.findById(existSurfaceTypeId)).thenReturn(optionalSurfaceType);
        when(surfaceTypeDAO.update(surfaceType)).thenReturn(surfaceType);
        when(courtDAO.findAllBySurfaceTypeId(existSurfaceTypeId)).thenReturn(courts);

        // Act
        surfaceTypeService.deleteSurfaceType(existSurfaceTypeId);

        // Assert
        assertTrue(surfaceType.isDeleted(), "Deleted SurfaceType should be deleted.");

        // možná ověřit že byla volána metoda update
    }

    @Test
    @DisplayName("Delete SurfaceType by ID with existing courts")
    @Order(9)
    void deleteSurfaceType_validSurfaceTypeIdWithCourts_shouldReturnTrue() {

        // Arrange
        Long existSurfaceTypeId = 1L;

        Long courtId1 = 1L;
        Long courtId2 = 2L;
        int courtNumber1 = 101;
        int courtNumber2 = 102;

        Court court1 = new Court();
        court1.setId(courtId1);
        court1.setCourtNumber(courtNumber1);
        court1.setDeleted(false);

        Court court2 = new Court();
        court2.setId(courtId2);
        court2.setCourtNumber(courtNumber2);
        court2.setDeleted(false);

        Optional<SurfaceType> optionalSurfaceType = Optional.of(surfaceType);
        List<Court> courts = List.of(court1, court2);

        when(surfaceTypeDAO.findById(existSurfaceTypeId)).thenReturn(optionalSurfaceType);
        when(surfaceTypeDAO.update(surfaceType)).thenReturn(surfaceType);
        when(courtDAO.findAllBySurfaceTypeId(existSurfaceTypeId)).thenReturn(courts);

        // Act
        surfaceTypeService.deleteSurfaceType(existSurfaceTypeId);

        // Assert
        assertTrue(surfaceType.isDeleted(), "Deleted SurfaceType should be deleted.");

        // Verify
        verify(courtService, times(1)).deleteCourt(courtId1);
        verify(courtService, times(1)).deleteCourt(courtId2);

    }

    @Test
    @DisplayName("Delete SurfaceType with invalid ID")
    @Order(10)
    void deleteSurfaceType_invalidSurfaceTypeId_shouldReturnSurfaceTypeNotFoundException() {

        // Arrange
        Long nonExistentSurfaceTypeId = 999L;
        Optional<SurfaceType> optionalSurfaceType = Optional.empty();

        // Act and Assert
        assertThrows(SurfaceTypeNotFoundException.class, () ->
                surfaceTypeService.deleteSurfaceType(nonExistentSurfaceTypeId),
                "Should throw SurfaceTypeNotFoundException");
    }

    @Test
    @DisplayName("Get SurfaceType by valid ID")
    @Order(2)
    void getSurfaceTypeById_validId_shouldReturnSurfaceTypeResponseDTO() {

        // Arrange
        Long existSurfaceTypeId = 1L;
        Optional<SurfaceType> optionalSurfaceType = Optional.of(surfaceType);

        when(surfaceTypeDAO.findById(existSurfaceTypeId)).thenReturn(optionalSurfaceType);
        when(surfaceTypeMapper.toResponseDTO(optionalSurfaceType.get())).thenReturn(surfaceTypeResponseDTO);

        // Act
        SurfaceTypeResponseDTO actual = surfaceTypeService.getSurfaceTypeById(existSurfaceTypeId);

        //Assert
        assertEquals(surfaceTypeResponseDTO, actual,
                "Returned SurfaceTypeResponseDTO should be the same instance as expected.");

    }

    @Test
    @DisplayName("Get SurfaceType by inValid ID")
    @Order(3)
    void getSurfaceTypeById_onValidId_shouldReturnSurfaceTypeNotFoundException() {

        // Arrange
        Long nonExistSurfaceTypeId = 999L;
        Optional<SurfaceType> optionalSurfaceType = Optional.empty();

        when(surfaceTypeDAO.findById(nonExistSurfaceTypeId)).thenReturn(optionalSurfaceType);

        // Act and Assert
        assertThrows(SurfaceTypeNotFoundException.class, () ->
                surfaceTypeService.getSurfaceTypeById(nonExistSurfaceTypeId),
                "Should throw SurfaceTypeNotFoundException");


    }

    @Test
    @DisplayName("Get All SurfaceTypes")
    @Order(4)
    void getAllSurfaceTypes_shouldReturnAllSurfaceTypesResponseDTO() {

        // Arrange
        SurfaceType surfaceType1 = new SurfaceType();
        surfaceType1.setId(1L);
        surfaceType1.setName("Clay");
        surfaceType1.setMinutePrice(BigDecimal.valueOf(10));
        surfaceType1.setDeleted(false);

        SurfaceType surfaceType2 = new SurfaceType();
        surfaceType2.setId(2L);
        surfaceType2.setName("Grass");
        surfaceType2.setMinutePrice(BigDecimal.valueOf(10));
        surfaceType2.setDeleted(false);

        List<SurfaceType> surfaceTypes = List.of(surfaceType1, surfaceType2);


        SurfaceTypeResponseDTO surfaceTypeResponseDTO1 = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO1.setId(1L);
        surfaceTypeResponseDTO1.setName("Clay");
        surfaceTypeResponseDTO1.setMinutePrice(BigDecimal.valueOf(10));

        SurfaceTypeResponseDTO surfaceTypeResponseDTO2 = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO2.setId(2L);
        surfaceTypeResponseDTO2.setName("Grass");
        surfaceTypeResponseDTO2.setMinutePrice(BigDecimal.valueOf(10));

        List<SurfaceTypeResponseDTO> surfaceTypesResponseDTOs = List.of(
                surfaceTypeResponseDTO1, surfaceTypeResponseDTO2);

        when(surfaceTypeDAO.findAll()).thenReturn(surfaceTypes);
        when(surfaceTypeMapper.toResponseDTO(surfaceType1)).thenReturn(surfaceTypeResponseDTO1);
        when(surfaceTypeMapper.toResponseDTO(surfaceType2)).thenReturn(surfaceTypeResponseDTO2);

        // Act
        List<SurfaceTypeResponseDTO> actual = surfaceTypeService.getAllSurfaceTypes();

        // Assort
        assertEquals(surfaceTypesResponseDTOs, actual,
                "The returned list should match the expected list of SurfaceTypeResponseDTOs.");

    }

    @Test
    @DisplayName("Get all non existing surface types should throw SurfaceTypeNotFoundException")
    @Order(5)
    void getAllSurfaceTypes_shouldReturnSurfaceTypeNotFoundException() {

        // Arrange
        List<SurfaceType> surfaceTypes = List.of();
        when(surfaceTypeDAO.findAll()).thenReturn(surfaceTypes);

        // Act and Assort
        assertThrows(SurfaceTypeNotFoundException.class, () -> surfaceTypeService.getAllSurfaceTypes(),
                "Should throw SurfaceTypeNotFoundException");
    }
}












