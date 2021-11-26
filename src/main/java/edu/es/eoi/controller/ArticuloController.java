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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.es.eoi.dto.ArticuloDto;
import edu.es.eoi.service.ArticuloServiceJpaImpl;

@RestController
@RequestMapping(value = "marketplace/articulo")
public class ArticuloController {

	@Autowired
	ArticuloServiceJpaImpl articuloService;
	
	/**
	 * CREAR ARTÍCULO EN BBDD
	 * @param dto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ArticuloDto> crearArticulo(@RequestBody ArticuloDto dto) {

		HttpStatus estadoHTTP;
		ArticuloDto salida = new ArticuloDto();
		
		try {
			salida = articuloService.crear(dto);
			estadoHTTP = HttpStatus.OK;		
		}catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<ArticuloDto>(salida, estadoHTTP);	
		
	}
	
	
	/**
	 * ACTUALIZA ARTÍCULO EN BBDD
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping("/{id}")	
	public ResponseEntity<ArticuloDto> actualizarArticulo(@PathVariable(value = "id") Integer id, @RequestBody ArticuloDto dto) {
		
		HttpStatus estadoHTTP;
		ArticuloDto salida = new ArticuloDto();
		
		try {
			//VALIDACIÓN comprobamos si el articulo existe, y si Id del PathVariable y RequestBody son el mismo
			if((articuloService.leerPorId(id) != null) && (id.equals(dto.getId()))) {
				salida = articuloService.actualizar(dto);
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
		
		return new ResponseEntity<ArticuloDto>(salida, estadoHTTP);	
	}
	
	/**
	 * DEVUELVE ARTÍCULO POR ID
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")	
	public ResponseEntity<ArticuloDto> devolverArticulo(@PathVariable Integer id) {
		
		HttpStatus estadoHTTP;
		ArticuloDto articuloDto = new ArticuloDto();
		
		try {
			articuloDto = articuloService.leerPorId(id);
			estadoHTTP = HttpStatus.OK;
		}
		catch(Exception e) {
			articuloDto = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<ArticuloDto>(articuloDto, estadoHTTP);
	}
	
	/**
	 * DEVUELVE ARTÍCULO POR BÚSQUEDA PARCIAL DE SU NOMBRE
	 * @param nombreParcial
	 * @return
	 */
	@GetMapping("")
	public ResponseEntity<List<ArticuloDto>> devolverPorLecturaParcial(@RequestParam String nombreParcial) {
		
		HttpStatus estadoHTTP;
		List<ArticuloDto> articulosDto = new ArrayList<ArticuloDto>();

		try {
			articulosDto = articuloService.leerParcialmente(nombreParcial);
			estadoHTTP = HttpStatus.OK;
		}
		catch(Exception e) {
			articulosDto = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<ArticuloDto>>(articulosDto, estadoHTTP);
	}
		
}
