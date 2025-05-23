package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.entity.SurfaceType;

import java.util.List;

public interface SurfaceTypeService {

    void save(SurfaceType surfaceType);

    SurfaceType getById(int id);

    List<SurfaceType> getAll();
}
