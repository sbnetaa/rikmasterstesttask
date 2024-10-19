package ru.terentyev.rikmasterstesttask.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.terentyev.rikmasterstesttask.entities.Roasting;

@Repository
public interface RoastingRepository extends JpaRepository<Roasting, Integer> {

	@Query("SELECT (SUM(r.lossesPercentage) / COUNT(r)) FROM Roasting r WHERE r.brigadeNumber = ?1")
	double takeLossesPerBrigade(UUID brigadeNumber);
	
	@Query("SELECT DISTINCT r.brigadeNumber FROM Roasting r")
	UUID[] findAllBrigades();
	
	@Query("SELECT (SUM(r.lossesPercentage) / COUNT(r)) FROM Roasting r WHERE r.country = ?1")
	double takeLossesPerCountry(String country);
	
	@Query("SELECT DISTINCT r.country FROM Roasting r")
	String[] findAllCountries();
}
