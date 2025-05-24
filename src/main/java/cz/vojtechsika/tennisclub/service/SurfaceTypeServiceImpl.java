package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.SurfaceTypeMapper;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SurfaceTypeServiceImpl implements SurfaceTypeService {

    private SurfaceTypeDAO surfaceTypeDAO;

    private SurfaceTypeMapper surfaceTypeMapper;

    @Autowired
    public SurfaceTypeServiceImpl(SurfaceTypeDAO theSurfaceTypeDAO,
                                  SurfaceTypeMapper theSurfaceTypeMapper) {
        surfaceTypeDAO = theSurfaceTypeDAO;
        surfaceTypeMapper = theSurfaceTypeMapper;
    }


    @Transactional
    @Override
    public SurfaceTypeResponseDTO save(SurfaceTypeDTO surfaceTypeDTO) {

        SurfaceType surfaceType = surfaceTypeMapper.toEntity(surfaceTypeDTO);
        SurfaceType saveSurfaceType = surfaceTypeDAO.save(surfaceType);

        return surfaceTypeMapper.toDTO(saveSurfaceType);
    }



    @Override
    public SurfaceTypeResponseDTO getById(int id) {
        SurfaceType surfaceType = surfaceTypeDAO.findById(id);
        return surfaceTypeMapper.toDTO(surfaceType);
    }

    @Override
    public List<SurfaceTypeResponseDTO> getAll() {
        return List.of();
    }
}


