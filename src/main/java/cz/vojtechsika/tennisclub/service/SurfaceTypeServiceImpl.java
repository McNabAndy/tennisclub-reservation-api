package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.SurfaceTypeMapper;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurfaceTypeServiceImpl implements SurfaceTypeService {

    private final SurfaceTypeDAO surfaceTypeDAO;

    private final SurfaceTypeMapper surfaceTypeMapper;

    @Autowired
    public SurfaceTypeServiceImpl(SurfaceTypeDAO theSurfaceTypeDAO,
                                  SurfaceTypeMapper theSurfaceTypeMapper) {
        surfaceTypeDAO = theSurfaceTypeDAO;
        surfaceTypeMapper = theSurfaceTypeMapper;
    }


    @Transactional
    @Override
    public SurfaceTypeResponseDTO save(SurfaceTypeDTO surfaceTypeDTO) {  // může toto vyhodit nějakou vyjímku ?
        SurfaceType surfaceType = surfaceTypeMapper.toEntity(surfaceTypeDTO);
        SurfaceType saveSurfaceType = surfaceTypeDAO.save(surfaceType);

        return surfaceTypeMapper.toResponseDTO(saveSurfaceType);
    }

    @Transactional
    @Override
    public SurfaceTypeResponseDTO update(SurfaceTypeDTO surfaceTypeDTO, Long id) {
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
    public void delete(Long id) {
        Optional <SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);
        if (optionalSurfaceType.isPresent()) {
            SurfaceType surfaceType = optionalSurfaceType.get();
            surfaceType.setDeleted(true);
            surfaceTypeDAO.update(surfaceType);
        } else {
            throw new SurfaceTypeNotFoundException("Delete failed: Surface type with ID " + id + " was not found.");
        }
    }


    @Override
    public SurfaceTypeResponseDTO getById(Long id) {
        Optional<SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);
        if (optionalSurfaceType.isPresent()) {
            return surfaceTypeMapper.toResponseDTO(optionalSurfaceType.get());
        } else {
            throw new SurfaceTypeNotFoundException("Surface type with id " + id + " not found");
        }
    }

    @Override
    public List<SurfaceTypeResponseDTO> getAll() {
        List<SurfaceType> surfaceTypes = surfaceTypeDAO.findAll();
        if (surfaceTypes.isEmpty()){
            throw new SurfaceTypeNotFoundException("No Surface types found in the database");
        }
        return surfaceTypes.stream().
                map(surfaceType -> surfaceTypeMapper.toResponseDTO(surfaceType)).
                collect(Collectors.toList());
    }


}


