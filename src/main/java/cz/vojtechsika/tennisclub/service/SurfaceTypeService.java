package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeResponseDTO;

import java.util.List;

public interface SurfaceTypeService {

    SurfaceTypeResponseDTO save(SurfaceTypeDTO surfaceTypeDTO);

    SurfaceTypeResponseDTO getById(int id);

    List<SurfaceTypeResponseDTO> getAll();
}
