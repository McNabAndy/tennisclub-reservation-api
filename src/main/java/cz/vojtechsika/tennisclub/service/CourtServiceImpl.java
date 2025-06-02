package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.ReservationDAO;
import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.CourtMapper;
import cz.vojtechsika.tennisclub.dto.mapper.SurfaceTypeMapper;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import cz.vojtechsika.tennisclub.exception.CourtNotFoundException;
import cz.vojtechsika.tennisclub.exception.CourtNumberAlreadyExistsException;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
/**
 * CourtServiceImpl is the service implementation for managing {@link Court} entities.
 * It handles business logic for creating, retrieving, updating, and deleting courts,
 * delegating persistence operations to {@link CourtDAO}, {@link SurfaceTypeDAO}, and {@link ReservationDAO}.
 * Mapping between DTOs and entities is performed by {@link CourtMapper} and {@link SurfaceTypeMapper}.
 *
 * <p>
 * Upon deletion of a court, this class also marks all related {@link Reservation} entities as deleted.
 * </p>
 */
@Service
public class CourtServiceImpl implements CourtService {

    /**
     * DAO for court persistence operations.
     */
    private final CourtDAO courtDAO;

    /**
     * Mapper to convert between {@link CourtDTO} and {@link Court}.
     */
    private final CourtMapper courtMapper;

    /**
     * DAO for surface type persistence operations.
     */
    private final SurfaceTypeDAO surfaceTypeDAO;


    /**
     * Mapper to convert between {@link SurfaceType} and {@link SurfaceTypeResponseDTO}
     */
    private final SurfaceTypeMapper surfaceTypeMapper;


    /**
     * DAO for reservation persistence operations.
     */
    private final ReservationDAO reservationDAO;


    /**
     * Constructs a new CourtServiceImpl with required dependencies.
     *
     * @param theCourtDAO          DAO for court persistence operations.
     * @param theSurfaceTypeDAO    DAO for surface type persistence operations.
     * @param theCourtMapper       Mapper to convert between {@link CourtDTO} and {@link Court}.
     * @param theSurfaceTypeMapper Mapper to convert between {@link SurfaceType} and {@link SurfaceTypeResponseDTO}.
     * @param theReservationDAO    DAO for reservation persistence operations.
     */
    @Autowired
    public CourtServiceImpl(CourtDAO theCourtDAO,
                            SurfaceTypeDAO theSurfaceTypeDAO,
                            CourtMapper theCourtMapper,
                            SurfaceTypeMapper theSurfaceTypeMapper,
                            ReservationDAO theReservationDAO
                            ) {
        courtDAO = theCourtDAO;
        courtMapper = theCourtMapper;
        surfaceTypeDAO = theSurfaceTypeDAO;
        surfaceTypeMapper = theSurfaceTypeMapper;
        reservationDAO = theReservationDAO;

    }

    /**
     * Creates a new court based on the provided {@link CourtDTO}.
     * <p>
     * Checks for uniqueness of the court number. If the court number already exists, a
     * {@link CourtNumberAlreadyExistsException} is thrown. Also verifies that the specified
     * surface type exists; if not, a {@link SurfaceTypeNotFoundException} is thrown.
     * </p>
     *
     * @param courtDTO DTO containing courtNumber and surfaceTypeId.
     * @return A {@link CourtResponseDTO} representing the newly created court.
     * @throws CourtNumberAlreadyExistsException if a court with the same number already exists.
     * @throws SurfaceTypeNotFoundException      if the referenced surface type does not exist.
     */
    @Transactional
    @Override
    public CourtResponseDTO save(CourtDTO courtDTO) {  // metoda save je blba možna to přepsat na createCourt


        Optional<SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(courtDTO.getSurfaceTypeId());
        Optional<Court> optionalCourt = courtDAO.findByCourtNumber(courtDTO.getCourtNumber());

        if (optionalCourt.isPresent()){
            throw new CourtNumberAlreadyExistsException("Court number "+ optionalCourt.get().getCourtNumber() +
                    " already exists. Unable create new court.");
        }

        if (optionalSurfaceType.isPresent()) {

            SurfaceType surfaceType = optionalSurfaceType.get();
            SurfaceTypeResponseDTO surfaceTypeResponseDTO = surfaceTypeMapper.toResponseDTO(surfaceType);

            Court court = courtMapper.toEntity(courtDTO);
            court.setSurfaceType(surfaceType);
            Court savedCourt = courtDAO.save(court);

            return courtMapper.toResponseDTO(savedCourt,surfaceTypeResponseDTO);
        } else {
            throw new SurfaceTypeNotFoundException("The requested surface type was not found in the database. Unable to save court.");
        }
    }


    /**
     * Retrieves a court by its ID.
     *
     * @param id The unique ID of the court to retrieve.
     * @return A {@link CourtResponseDTO} representing the retrieved court.
     * @throws CourtNotFoundException if no court with the given ID exists.
     */
    @Override
    public CourtResponseDTO getCourtById(Long id) {

        Optional<Court> optionalCourt = courtDAO.findById(id);
        if (optionalCourt.isPresent()) {
            Court court = optionalCourt.get();

            SurfaceTypeResponseDTO surfaceTypeResponseDTO = surfaceTypeMapper.toResponseDTO(court.getSurfaceType());

            return courtMapper.toResponseDTO(court,surfaceTypeResponseDTO);
        } else {
            throw new CourtNotFoundException("Court with id " + id + " not found");
        }
    }



    /**
     * Retrieves all courts in the system.
     *
     * @return A list of {@link CourtResponseDTO} for all existing courts.
     * @throws CourtNotFoundException if no courts are found in the database.
     */
    @Override
    public List<CourtResponseDTO> getAllCourts() {
        List<Court> courts = courtDAO.findAll();
        if (courts.isEmpty()){
            throw new CourtNotFoundException("No Courts found in the database");
        }
        return courts.stream().
                map(court -> {
                    SurfaceTypeResponseDTO surfaceTypeResponseDTO = surfaceTypeMapper.toResponseDTO(court.getSurfaceType());
                    return courtMapper.toResponseDTO(court,surfaceTypeResponseDTO);
                }).
                toList();
    }


    /**
     * Updates an existing court identified by {@code id} using data from the provided {@link CourtDTO}.
     * <p>
     * Verifies that the court exists; if not, a {@link CourtNotFoundException} is thrown. Also checks
     * that the specified surface type exists; if not, a {@link SurfaceTypeNotFoundException} is thrown.
     * </p>
     *
     * @param courtDTO The {@link CourtDTO} containing updated courtNumber and surfaceTypeId.
     * @param id       The unique ID of the court to update.
     * @return A {@link CourtResponseDTO} representing the updated court.
     * @throws CourtNotFoundException      if no court with the given ID exists.
     * @throws SurfaceTypeNotFoundException if the referenced surface type does not exist.
     */
    @Transactional
    @Override
    public CourtResponseDTO updateCourt(CourtDTO courtDTO, Long id) {

        Optional<Court> optionalCourt = courtDAO.findById(id);
        Optional<SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(courtDTO.getSurfaceTypeId());

        if (optionalCourt.isEmpty()) {
            throw new CourtNotFoundException("Update failed: Court with id " + id + " does not exist");
        }
        if (optionalSurfaceType.isPresent()) {
            SurfaceType surfaceType = optionalSurfaceType.get();

            Court court = courtMapper.toEntity(courtDTO);
            court.setId(id);
            court.setSurfaceType(surfaceType);

            Court updateCourt = courtDAO.update(court);
            SurfaceTypeResponseDTO surfaceTypeResponseDTO = surfaceTypeMapper.toResponseDTO(updateCourt.getSurfaceType());
            return courtMapper.toResponseDTO(court,surfaceTypeResponseDTO);

        }
        throw new SurfaceTypeNotFoundException("The requested surface type was not found in the database. Unable to update court.");


    }


    /**
     * Marks a court (and its related reservations) as deleted (soft-delete).
     * <p>
     * If the specified court does not exist, a {@link CourtNotFoundException} is thrown.
     * Otherwise, the court's {@code deleted} flag is set to {@code true}, and all reservations
     * associated with that court are also marked as deleted.
     * </p>
     *
     * @param id The unique ID of the court to delete.
     * @throws CourtNotFoundException if no court with the given ID exists.
     */
    @Transactional
    @Override
    public void deleteCourt(Long id) {

        Optional<Court> optionalCourt = courtDAO.findById(id);
        if (optionalCourt.isPresent()) {
            Court court = optionalCourt.get();
            court.setDeleted(true);
            courtDAO.update(court);

            List<Reservation> reservations = reservationDAO.findAllByCourtNumber(court.getCourtNumber());

            if (!reservations.isEmpty()) {
                reservations.stream()
                        .forEach(reservation -> {
                            reservation.setDeleted(true);
                            reservationDAO.update(reservation);
                        });
            }
        } else {
            throw new CourtNotFoundException("Delete failed: Court with ID " + id + " not found.");
        }

    }

}











