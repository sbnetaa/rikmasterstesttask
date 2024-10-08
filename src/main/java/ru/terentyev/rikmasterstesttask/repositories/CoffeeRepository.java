package ru.terentyev.rikmasterstesttask.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.terentyev.rikmasterstesttask.entities.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
	List<Coffee> findAllBySortAndCountry(String sort, String country);
}
