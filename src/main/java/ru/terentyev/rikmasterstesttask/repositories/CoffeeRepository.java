package ru.terentyev.rikmasterstesttask.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.terentyev.rikmasterstesttask.entities.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
	
	@Query("SELECT c FROM Coffee c GROUP BY c.id, c.sort, c.country")
	List<Coffee> findAllGroupBySortAndCountry();
	
	List<Coffee> findAllBySortAndCountry(String sort, String country);
	
	@Query("SELECT SUM(c.grams) FROM Coffee c WHERE c.sort = ?1 AND c.country = ?2")
	int takeCommonStockPerSortAndCountry(String sort, String Country);
	
	@Query("SELECT SUM(c.grams - c.roastedGramsAtInput) FROM Coffee c WHERE c.sort = ?1 AND c.country = ?2")
	int takeFreshStockPerSortAndCountry(String sort, String Country);
	
}
