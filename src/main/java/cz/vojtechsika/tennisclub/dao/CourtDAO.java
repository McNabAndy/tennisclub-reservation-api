package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.Court;

import java.util.List;
import java.util.Optional;

public interface CourtDAO {

    void save(Court court);

    List<Court> findAll();

    Court findById(Long id);

    Optional<Court> findByCourtNumber(int courtNumber);


}
