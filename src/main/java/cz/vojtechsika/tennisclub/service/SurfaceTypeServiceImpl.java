package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurfaceTypeServiceImpl implements SurfaceTypeService{

    private SurfaceTypeDAO surfaceTypeDAO;

    @Autowired
    public SurfaceTypeServiceImpl(SurfaceTypeDAO theSurfaceTypeDAO) {
        surfaceTypeDAO = theSurfaceTypeDAO;
    }


    @Override
    public void save(SurfaceType surfaceType) {
        surfaceTypeDAO.save(surfaceType);
    }

    @Override
    public SurfaceType getById(int id) {
        return surfaceTypeDAO.findById(id);
    }

    @Override
    public List<SurfaceType> getAll() {
        return surfaceTypeDAO.findAll();
    }
}
