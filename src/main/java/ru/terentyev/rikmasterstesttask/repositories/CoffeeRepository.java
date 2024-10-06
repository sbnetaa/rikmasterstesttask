package ru.terentyev.rikmasterstesttask.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.terentyev.rikmasterstesttask.entities.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
	Optional<Coffee> findBySortAndCountry(String sort, String country);
}
