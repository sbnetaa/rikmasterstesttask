package ru.terentyev.rikmasterstesttask.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.terentyev.rikmasterstesttask.entities.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
	List<Coffee> findAllBySortAndCountry(String sort, String country);
	
	@Query("SELECT SUM(c.grams) FROM Coffee c HAVING c.sort = ?1 and c.country = ?2;")
	Integer takeStock(String sort, String country);
	
	@Query("SELECT SUM(c.roastedGramsAtInput) FROM Coffee c HAVING c.sort = ?1 and c.country = ?2;")
	Integer takeRoastedStock(String sort, String country);
}
