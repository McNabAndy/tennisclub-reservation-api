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
import java.util.stream.Collectors;

@Service
public class CourtServiceImpl implements CourtService {

    private final CourtDAO courtDAO;

    private final CourtMapper courtMapper;

    private final SurfaceTypeDAO surfaceTypeDAO;

    private final SurfaceTypeMapper surfaceTypeMapper;

    private final ReservationDAO reservationDAO;

    @Autowired
    public CourtServiceImpl(CourtDAO thecourtDAO,
                            SurfaceTypeDAO theSurfaceTypeDAO,
                            CourtMapper thecourtMapper,
                            SurfaceTypeMapper theSurfaceTypeMapper,
                            ReservationDAO thereservationDAO
                            ) {
        courtDAO = thecourtDAO;
        courtMapper = thecourtMapper;
        surfaceTypeDAO = theSurfaceTypeDAO;
        surfaceTypeMapper = theSurfaceTypeMapper;
        reservationDAO = thereservationDAO;

    }

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
                reservations.stream().
                        forEach(reservation -> {
                            reservation.setDeleted(true);
                            reservationDAO.update(reservation);
                        });
            }
        } else {
            throw new CourtNotFoundException("Delete failed: Court with ID " + id + " not found.");
        }

    }

}











