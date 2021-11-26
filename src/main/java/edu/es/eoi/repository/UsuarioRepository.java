package edu.es.eoi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.es.eoi.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	public Optional<Usuario> findByNombre(String nombre);
	
	public Optional<Usuario> findByNombreAndPassword(String nombre, String password);
	
}
