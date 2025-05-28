package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.SurfaceTypeMapper;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SurfaceTypeServiceImpl implements SurfaceTypeService {

    private final SurfaceTypeDAO surfaceTypeDAO;

    private final SurfaceTypeMapper surfaceTypeMapper;

    private final CourtDAO courtDAO;

    private final CourtService courtService;

    @Autowired
    public SurfaceTypeServiceImpl(SurfaceTypeDAO theSurfaceTypeDAO,
                                  SurfaceTypeMapper theSurfaceTypeMapper,
                                  CourtDAO theCourtDAO,
                                  CourtService theCourtService) {
        surfaceTypeDAO = theSurfaceTypeDAO;
        surfaceTypeMapper = theSurfaceTypeMapper;
        courtDAO = theCourtDAO;
        courtService = theCourtService;


    }


    @Transactional
    @Override
    public SurfaceTypeResponseDTO saveSurfaceType(SurfaceTypeDTO surfaceTypeDTO) {
        SurfaceType surfaceType = surfaceTypeMapper.toEntity(surfaceTypeDTO);
        SurfaceType saveSurfaceType = surfaceTypeDAO.save(surfaceType);

        return surfaceTypeMapper.toResponseDTO(saveSurfaceType);
    }

    @Transactional
    @Override
    public SurfaceTypeResponseDTO updateSurfaceType(SurfaceTypeDTO surfaceTypeDTO, Long id) {
        Optional<SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);

        if (optionalSurfaceType.isEmpty()) {
            throw new SurfaceTypeNotFoundException("Update failed: Surface type with ID " + id + " does not exist");
        }

        SurfaceType surfaceType = surfaceTypeMapper.toEntity(surfaceTypeDTO);
        surfaceType.setId(id);

        SurfaceType updateSurfaceType = surfaceTypeDAO.update(surfaceType);
        return surfaceTypeMapper.toResponseDTO(updateSurfaceType);
    }

    @Transactional
    @Override
    public void deleteSurfaceType(Long id) {

        Optional <SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);
        if (optionalSurfaceType.isPresent()) {
            SurfaceType surfaceType = optionalSurfaceType.get();
            surfaceType.setDeleted(true);
            surfaceTypeDAO.update(surfaceType);

            List<Court> courts = courtDAO.findAllBySurfaceTypeId(id);

            if (!courts.isEmpty()) {
                courts.stream().
                        forEach(court -> {
                            courtService.deleteCourt(court.getId());
                        });
            }

        } else {
            throw new SurfaceTypeNotFoundException("Delete failed: Surface type with ID " + id + " was not found.");
        }
    }


    @Override
    public SurfaceTypeResponseDTO getSurfaceTypeById(Long id) {
        Optional<SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);
        if (optionalSurfaceType.isPresent()) {
            return surfaceTypeMapper.toResponseDTO(optionalSurfaceType.get());
        } else {
            throw new SurfaceTypeNotFoundException("Surface type with id " + id + " not found");
        }
    }

    @Override
    public List<SurfaceTypeResponseDTO> getAllSurfaceTypes() {
        List<SurfaceType> surfaceTypes = surfaceTypeDAO.findAll();
        if (surfaceTypes.isEmpty()){
            throw new SurfaceTypeNotFoundException("No Surface types found in the database");
        }
        return surfaceTypes.stream().
                map(surfaceType -> surfaceTypeMapper.toResponseDTO(surfaceType)).
                toList();
    }


}


