package cz.vojtechsika.tennisclub.dao;


import cz.vojtechsika.tennisclub.entity.Court;


import java.util.List;
import java.util.Optional;

public interface CourtDAO {

    Court save(Court court);

    Optional <Court> findById(Long id);

    List<Court> findAll();

    List<Court> findAllBySurfaceTypeId(Long id);

    Court update(Court court);

    Optional<Court> findByCourtNumber(int courtNumber);


}
