package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;

import java.util.List;

public interface SurfaceTypeService {

    SurfaceTypeResponseDTO saveSurfaceType(SurfaceTypeDTO surfaceTypeDTO);

    SurfaceTypeResponseDTO getSurfaceTypeById(Long id);

    List<SurfaceTypeResponseDTO> getAllSurfaceTypes();

    SurfaceTypeResponseDTO updateSurfaceType(SurfaceTypeDTO surfaceTypeDTO, Long id);

    void deleteSurfaceType(Long id);
}
