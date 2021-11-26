package edu.es.eoi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.es.eoi.dto.UsuarioDto;
import edu.es.eoi.service.UsuarioServiceJpaImpl;

@RestController
@RequestMapping(value = "marketplace/usuario")
public class UsuarioController {
	
	@Autowired
	UsuarioServiceJpaImpl usuarioService;
	
	
	/**
	 * CREA UN USUARIO EN BBDD
	 * @param dto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<UsuarioDto> crearUsuario(@RequestBody UsuarioDto dto) {

		HttpStatus estadoHTTP;
		UsuarioDto salida = new UsuarioDto();
		//EstadoOperacionDto estadoOperacion = new EstadoOperacionDto();
		
		try {
			salida = usuarioService.crear(dto);
			estadoHTTP = HttpStatus.OK;	
		}catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<UsuarioDto>(salida, estadoHTTP);	
	}
	
	
	/**
	 * COMPRUEBA QUE EL LOGIN DEL USUARIO ES CORRECTO, SI NO LO ES DEVUELVE NULL
	 * @param dto
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<UsuarioDto> loginComprobacion(@RequestBody UsuarioDto dto) {

		HttpStatus estadoHTTP;
		UsuarioDto salida = new UsuarioDto();	
		
		try {
			salida = usuarioService.comprobacionLogin(dto);
			estadoHTTP = HttpStatus.OK;	
		}catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<UsuarioDto>(salida, estadoHTTP);	
		
	}
	
	/**
	 * ACTUALIZA UN USUARIO EN LA BBDD POR ID
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping("/{id}")	
	public ResponseEntity<UsuarioDto> actualizarUsuario(@PathVariable(value = "id") Integer id, @RequestBody UsuarioDto dto) {
		
		HttpStatus estadoHTTP;
		UsuarioDto salida = new UsuarioDto();
		
		try {
			//VALIDACIÓN comprobamos si el usuario existe, y si Id del PathVariable y RequestBody son el mismo
			if((usuarioService.leerPorId(id) != null) && (id.equals(dto.getId()))) {
				salida = usuarioService.actualizar(dto);		
			}
			else {
				salida = null;
			}
			estadoHTTP = HttpStatus.OK;
		}
		catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<UsuarioDto>(salida, estadoHTTP);	
	}
	
	/**
	 * MUESTRA UN LISTADO DE USUARIOS
	 * @return
	 */
	@GetMapping	
	public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
		
		List<UsuarioDto> usuariosDto= new ArrayList<UsuarioDto>();
		HttpStatus estado;
		try {
			usuariosDto = usuarioService.devolverTodosLosUsuarios();
			estado = HttpStatus.OK;
		}
		catch(Exception e) {
			//e.printStackTrace();
			estado = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<List<UsuarioDto>>(usuariosDto, estado);
	}
}
