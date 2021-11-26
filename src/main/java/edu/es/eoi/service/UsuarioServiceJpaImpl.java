package edu.es.eoi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.es.eoi.dto.UsuarioDto;
import edu.es.eoi.entity.Usuario;
import edu.es.eoi.repository.UsuarioRepository;

@Service
public class UsuarioServiceJpaImpl implements CRUDService <UsuarioDto, Integer>{

	@Autowired
	UsuarioRepository usuarioRepo;
	
	/**
	 * Función que crea un usuario en BBDD
	 */
	@Override
	public UsuarioDto crear(UsuarioDto dto){
		
		Usuario usuario = new Usuario();
		//dto.setId(null);
		
		//LOGICA DE NEGOCIO: No pueden existir dos usuarios con el mismo nombre en bbdd
		if(!usuarioRepo.findByNombre(dto.getNombre()).isPresent()) {	
			BeanUtils.copyProperties(dto, usuario);
			usuario = usuarioRepo.save(usuario);
			dto.setId(usuario.getId());
		}else {
			dto = null;
		}
		
		return dto;
	}

	/**
	 * Función que actualiza un usuario
	 */
	@Override
	public UsuarioDto actualizar(UsuarioDto dto) {
		
		//Necesario hacerlo para actualizar
		Optional<Usuario> usuario = usuarioRepo.findById(dto.getId());
		
		//Cambiamos datos
		usuario.get().setNombre(dto.getNombre());
		usuario.get().setPassword(dto.getPassword());
		
		//Actualizamos
		usuarioRepo.save(usuario.get());
		
		return dto;
		
	}

	/**
	 * Función que devuelve un UsuarioDto si le pasas su id en BBDD
	 */
	@Override
	public UsuarioDto leerPorId(Integer primaryKey) {
		
		UsuarioDto usuarioDto = new UsuarioDto();
		Optional <Usuario> usuario = usuarioRepo.findById(primaryKey);
		
		if(usuario.isPresent()) {
			BeanUtils.copyProperties(usuario.get(), usuarioDto);
		}
		else {
			usuarioDto = null;
		}
		
		return usuarioDto;
		
	}
	
	//No hacía falta implementarlo
	@Override
	public UsuarioDto eliminar(Integer primaryKey) {
		
		return null;
	}
	
	/**
	 * Función que devuelve el dto nulo si no está logueado
	 * @param dto
	 * @return
	 */
	public UsuarioDto comprobacionLogin(UsuarioDto dto) {
		
		Optional <Usuario> usuarioLogueado = usuarioRepo.findByNombreAndPassword(dto.getNombre(), dto.getPassword());
		
		//Si está presente en BBDD podrá loguearse
		if(usuarioLogueado.isPresent()) {
			dto.setId(usuarioLogueado.get().getId());
		}
		else {
			dto = null;
		}
		
		return dto;		
	}
	
	/**
	 * Función que devuelve un listado de todos los usuarios en BBDD
	 * @return
	 */
	public List<UsuarioDto> devolverTodosLosUsuarios(){
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios = usuarioRepo.findAll();
				
		return conversionListaEntidadAListaDto(usuarios);
	}
	
	//SE USA
	/**
	 * Función que convierte una lista de con elementos de la clase Usuario a otra lista con elementos de la clase UsuarioDto
	 * @param usuarios
	 * @return
	 */
	public List<UsuarioDto> conversionListaEntidadAListaDto(List<Usuario> usuarios){
		List<UsuarioDto> usuariosDto = new ArrayList<UsuarioDto>();
		
		for(Usuario usuario : usuarios) {
			UsuarioDto usuarioDto = new UsuarioDto();
			BeanUtils.copyProperties(usuario, usuarioDto);
			usuariosDto.add(usuarioDto);
		}
		
		return usuariosDto;
	}
	
}
