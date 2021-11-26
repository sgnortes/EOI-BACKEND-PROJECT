package edu.es.eoi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.es.eoi.entity.Articulo;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {
	
	public Optional<Articulo> findByNombre(String nombre);
	
	public Optional<List<Articulo>> findByNombreContains(String nombre);
	
}