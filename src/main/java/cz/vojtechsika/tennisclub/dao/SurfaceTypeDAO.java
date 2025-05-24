package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.SurfaceType;

import java.util.List;
import java.util.Optional;

public interface SurfaceTypeDAO {

    SurfaceType save(SurfaceType surfaceType);

    Optional<SurfaceType> findById(Long id);

    List<SurfaceType> findAll();

    SurfaceType update(SurfaceType surfaceType);
}
