package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.ReservationDAO;
import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.mapper.CourtMapper;
import cz.vojtechsika.tennisclub.dto.mapper.SurfaceTypeMapper;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import cz.vojtechsika.tennisclub.exception.CourtNotFoundException;
import cz.vojtechsika.tennisclub.exception.CourtNumberAlreadyExistsException;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourtServiceImplTest {



    @Mock
    private  CourtDAO courtDAO;

    @Mock
    private CourtMapper courtMapper;

    @Mock
    private SurfaceTypeDAO surfaceTypeDAO;

    @Mock
    private SurfaceTypeMapper surfaceTypeMapper;

    @Mock
    private ReservationDAO reservationDAO;

    @InjectMocks
    private CourtServiceImpl courtService;




    @BeforeEach
    void setUp() {

    }


    // k tomuhle testu pokud bude čas se vrátit a optimalizovat ho hlavně arrange část
    @Test
    @DisplayName("Create new Court with valid DTO ")
    @Order(1)
    void save_validCourtDTO_shouldCreateNewCourt() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Long surfaceTypeId = 1L;

        SurfaceType surfaceType = new SurfaceType();

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        Court court = new Court();

        Court saveCourt = new Court();
        saveCourt.setSurfaceType(surfaceType);

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setId(courtId);
        courtResponseDTO.setCourtNumber(courtNumber);
        courtResponseDTO.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);

        Optional<SurfaceType> optionalSurfaceType = Optional.of(surfaceType);
        Optional<Court> optionalCourt = Optional.empty();

        when(surfaceTypeDAO.findById(courtDTO.getSurfaceTypeId())).thenReturn(optionalSurfaceType);
        when(courtDAO.findByCourtNumber(courtDTO.getCourtNumber())).thenReturn(optionalCourt);
        when(surfaceTypeMapper.toResponseDTO(surfaceType)).thenReturn(surfaceTypeResponseDTO);
        when(courtMapper.toEntity(courtDTO)).thenReturn(court);
        when(courtDAO.save(court)).thenReturn(saveCourt);
        when(courtMapper.toResponseDTO(saveCourt, surfaceTypeResponseDTO)).thenReturn(courtResponseDTO);


        // Act
        CourtResponseDTO actual =  courtService.save(courtDTO);

        // Assert
        assertEquals(courtResponseDTO, actual, "Should not return the same object");

    }

    @Test
    @DisplayName("Create new Court with existing court id")
    @Order(2)
    void save_inValidDTOWithExistingCourtNumber_shouldReturnCourtNumberAlreadyExistsException() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Long surfaceTypeId = 1L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        Court court = new Court();
        court.setId(courtId);
        court.setCourtNumber(courtNumber);


        Optional<SurfaceType> optionalSurfaceType = Optional.empty();
        Optional<Court> optionalCourt = Optional.of(court);

        when(surfaceTypeDAO.findById(courtDTO.getSurfaceTypeId())).thenReturn(optionalSurfaceType);
        when(courtDAO.findByCourtNumber(courtNumber)).thenReturn(optionalCourt);

        // Act and Assert
        assertThrows(CourtNumberAlreadyExistsException.class, () ->
                courtService.save(courtDTO));
    }

    @Test
    @DisplayName("Create existing Court with non existing surface type")
    @Order(3)
    void save_inValidDTOWithNonExistingSurfaceTypeId_shouldReturnSurfaceTypeNotFoundException() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Long surfaceTypeId = 1L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        Court court = new Court();
        court.setId(courtId);
        court.setCourtNumber(courtNumber);


        Optional<SurfaceType> optionalSurfaceType = Optional.empty();
        Optional<Court> optionalCourt = Optional.empty();

        when(surfaceTypeDAO.findById(courtDTO.getSurfaceTypeId())).thenReturn(optionalSurfaceType);
        when(courtDAO.findByCourtNumber(courtNumber)).thenReturn(optionalCourt);

        // Act and Assert
        assertThrows(SurfaceTypeNotFoundException.class, () ->
                courtService.save(courtDTO));
    }




    @Test
    @DisplayName("Get Court by valid ID")
    @Order(4)
    void getCourtById_validCourtId_shouldReturnCourtResponseDTO() {

        //Arrange
        Long courtId = 1L;

        SurfaceType surfaceType = new SurfaceType();

        Court court = new Court();
        court.setId(courtId);
        court.setSurfaceType(surfaceType);

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();

        Optional<Court> optionalCourt = Optional.of(court);

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);
        when(surfaceTypeMapper.toResponseDTO(court.getSurfaceType())).thenReturn(surfaceTypeResponseDTO);
        when(courtMapper.toResponseDTO(court,surfaceTypeResponseDTO)).thenReturn(courtResponseDTO);

        // Act

        CourtResponseDTO actual = courtService.getCourtById(courtId);

        // Assert
        assertEquals(courtResponseDTO, actual, "Should return the same CourtResponseDTO");
    }


    @Test
    @DisplayName("Get Court by invalid ID")
    @Order(5)
    void getCourtById_inValidCourtId_shouldReturnCourtNotFoundException() {

        //Arrange
        Long courtId = 1L;

        SurfaceType surfaceType = new SurfaceType();

        Court court = new Court();
        court.setId(courtId);
        court.setSurfaceType(surfaceType);

        Optional<Court> optionalCourt = Optional.empty();

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);

        // Act and Assert
        assertThrows(CourtNotFoundException.class, () -> courtService.getCourtById(courtId),
                "Should return the CourtNotFoundException");

    }


    @Test
    @DisplayName("Get all existing courts")
    @Order(6)
    void getAllCourts_shouldReturnAllCourts() {

        // Arrange

        Court court1 = new Court();
        court1.setSurfaceType(new SurfaceType());

        Court court2 = new Court();
        court2.setSurfaceType(new SurfaceType());

        SurfaceTypeResponseDTO surfaceTypeResponseDTO1 = new SurfaceTypeResponseDTO();
        SurfaceTypeResponseDTO surfaceTypeResponseDTO2 = new SurfaceTypeResponseDTO();

        CourtResponseDTO courtResponseDTO1 = new CourtResponseDTO();
        CourtResponseDTO courtResponseDTO2 = new CourtResponseDTO();

        List<Court> courts = List.of(court1, court2);
        List<CourtResponseDTO> courtResponseDTOList = List.of(courtResponseDTO1, courtResponseDTO2);


        when(courtDAO.findAll()).thenReturn(courts);

        when(surfaceTypeMapper.toResponseDTO(court1.getSurfaceType())).thenReturn(surfaceTypeResponseDTO1);
        when(courtMapper.toResponseDTO(court1,surfaceTypeResponseDTO1)).thenReturn(courtResponseDTO1);

        when(surfaceTypeMapper.toResponseDTO(court2.getSurfaceType())).thenReturn(surfaceTypeResponseDTO2);
        when(courtMapper.toResponseDTO(court2,surfaceTypeResponseDTO2)).thenReturn(courtResponseDTO2);

        // Act
        List<CourtResponseDTO> actual = courtService.getAllCourts();

        // Assert
        assertIterableEquals(courtResponseDTOList, actual,
                "Should return the same CourtResponseDTOList");

    }

    @Test
    @DisplayName("Get all non existing courts")
    @Order(7)
    void getAllCourts_shouldReturnCourtNotFoundException() {

        // Arrange
        List<Court> courts = List.of();

        when(courtDAO.findAll()).thenReturn(courts);

        // Act and Assert
        assertThrows(CourtNotFoundException.class, () -> courtService.getAllCourts(),
                "Should return the CourtNotFoundException");
    }

    @Test
    @DisplayName("Update existing Court")
    @Order(8)
    void updateCourt_validCourtDTOAndCourtId_shouldReturnCourtResponseDTO() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Long surfaceTypeId = 1L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        SurfaceType surfaceType = new SurfaceType();

        Court court = new Court();
        court.setId(courtId);
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();


        Optional<SurfaceType> optionalSurfaceType = Optional.of(surfaceType);
        Optional<Court> optionalCourt = Optional.of(court);

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);
        when(surfaceTypeDAO.findById(courtDTO.getSurfaceTypeId())).thenReturn(optionalSurfaceType);
        when(courtMapper.toEntity(courtDTO)).thenReturn(court);
        when(courtDAO.update(court)).thenReturn(court);
        when(surfaceTypeMapper.toResponseDTO(court.getSurfaceType())).thenReturn(surfaceTypeResponseDTO);
        when(courtMapper.toResponseDTO(court, surfaceTypeResponseDTO)).thenReturn(courtResponseDTO);

        // Act
        CourtResponseDTO actual = courtService.updateCourt(courtDTO, courtId);

        // Assert
        assertEquals(courtResponseDTO, actual, "Should return the same CourtResponseDTO");
    }

    @Test
    @DisplayName("Update non existing Court")
    @Order(9)
    void updateCourt_inValidCourtDTOAndCourtId_shouldReturnCourtNotFoundException() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Long surfaceTypeId = 1L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        SurfaceType surfaceType = new SurfaceType();

        Court court = new Court();
        court.setId(courtId);
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);

        Optional<Court> optionalCourt = Optional.empty();

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);


        // Act and Assert
        assertThrows(CourtNotFoundException.class, () -> courtService.updateCourt(courtDTO, courtId),
                "Should return the CourtNotFoundException");
    }

    @Test
    @DisplayName("Update existing Court with non existed surface Type ")
    @Order(10)
    void updateCourt_inValidCourtDTOWithNonExistedSurfaceTypeId_shouldReturnSurfaceTypeNotFoundException() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Long surfaceTypeId = 1L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        SurfaceType surfaceType = new SurfaceType();

        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);

        Optional<Court> optionalCourt = Optional.of(court);
        Optional<SurfaceType> optionalSurfaceType = Optional.empty();

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);
        when(surfaceTypeDAO.findById(courtDTO.getSurfaceTypeId())).thenReturn(optionalSurfaceType);


        // Act and Assert
        assertThrows(SurfaceTypeNotFoundException.class, () -> courtService.updateCourt(courtDTO, courtId),
                "Should return the SurfaceTypeNotFoundException");

        // Verify
        verify(courtDAO, never()).update(court);
    }

    @Test
    @DisplayName("Delete Court by ID with existing reservations")
    @Order(11)
    void deleteCourt_validCourtIdWithReservations_shouldReturnTrue() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Long surfaceTypeId = 1L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        Reservation reservations1 = new Reservation();
        Reservation reservations2 = new Reservation();

        List<Reservation> reservations = List.of(reservations1,reservations2);

        Court court = new Court();
        court.setId(courtId);
        court.setCourtNumber(courtNumber);
        court.setDeleted(false);
        court.setSurfaceType(new SurfaceType());

        Optional<Court> optionalCourt = Optional.of(court);

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);
        when(reservationDAO.findAllByCourtNumber(court.getCourtNumber())).thenReturn(reservations);

        // Act
        courtService.deleteCourt(courtId);

        // Assert
        assertTrue(court.isDeleted(),"Court should be deleted");
        assertTrue(reservations1.isDeleted(),"Reservation should be deleted");
        assertTrue(reservations2.isDeleted(),"Reservation should be deleted");

        // Verify
        verify(courtDAO, times(1)).update(court);
        verify(reservationDAO, times(1)).update(reservations1);
        verify(reservationDAO, times(1)).update(reservations2);

    }

    @Test
    @DisplayName("Delete non existing Court")
    @Order(12)
    void deleteCourt_inValidCourtId_shouldReturnCourtNotFoundException() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Reservation reservations1 = new Reservation();
        reservations1.setDeleted(false);

        Reservation reservations2 = new Reservation();
        reservations2.setDeleted(false);

        List<Reservation> reservations = List.of(reservations1,reservations2);


        Court court = new Court();
        court.setId(courtId);
        court.setCourtNumber(courtNumber);
        court.setDeleted(false);
        court.setSurfaceType(new SurfaceType());

        Optional<Court> optionalCourt = Optional.empty();

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);

        // Act and Assert
        assertThrows(CourtNotFoundException.class, () -> courtService.deleteCourt(courtId),
                "Should return the CourtNotFoundException");

        assertFalse(court.isDeleted(),"Court should not be deleted");
        assertFalse(reservations1.isDeleted(),"Reservation should not be deleted");
        assertFalse(reservations2.isDeleted(),"Reservation should not be deleted");

        // Verify
        verify(courtDAO, never()).update(court);
        verify(reservationDAO, never()).update(reservations1);
        verify(reservationDAO, never()).update(reservations2);

    }


    @Test
    @DisplayName("Delete existing Court with no reservations")
    @Order(12)
    void deleteCourt_validCourtIdWithAndNoExistedReservations_shouldReturnCourtNotFoundException() {

        // Arrange
        Long courtId = 1L;
        int courtNumber = 101;

        Reservation reservations1 = new Reservation();
        Reservation reservations2 = new Reservation();

        List<Reservation> reservations = List.of();

        Court court = new Court();
        court.setId(courtId);
        court.setCourtNumber(courtNumber);
        court.setDeleted(false);
        court.setSurfaceType(new SurfaceType());

        Optional<Court> optionalCourt = Optional.of(court);

        when(courtDAO.findById(courtId)).thenReturn(optionalCourt);
        when(reservationDAO.findAllByCourtNumber(courtNumber)).thenReturn(reservations);

        // Act
        courtService.deleteCourt(courtId);

        // Assert
        assertTrue(court.isDeleted(),"Court should be deleted");
        assertFalse(reservations1.isDeleted(),"Reservation should not be deleted");
        assertFalse(reservations2.isDeleted(),"Reservation should not be deleted");

        // Verify
        verify(reservationDAO, never()).update(reservations1);
        verify(reservationDAO, never()).update(reservations2);

    }


}