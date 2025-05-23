package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.SurfaceType;

import java.util.List;

public interface SurfaceTypeDAO {

    void save(SurfaceType surfaceType);

    SurfaceType findById(int id);

    List<SurfaceType> findAll();
}
