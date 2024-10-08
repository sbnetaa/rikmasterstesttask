package ru.terentyev.rikmasterstesttask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.terentyev.rikmasterstesttask.entities.Roasting;

@Repository
public interface RoastingRepository extends JpaRepository<Roasting, Integer> {

}
