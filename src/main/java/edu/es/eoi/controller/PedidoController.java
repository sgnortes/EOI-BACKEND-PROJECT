package edu.es.eoi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import edu.es.eoi.dto.PedidoDto;
import edu.es.eoi.service.ArticuloServiceJpaImpl;
import edu.es.eoi.service.PedidoServiceJpaImpl;

@RestController
@RequestMapping(value = "marketplace/pedido")
public class PedidoController {
	
	@Autowired
	PedidoServiceJpaImpl pedidoService;
	
	@Autowired
	ArticuloServiceJpaImpl articuloService;

	
	/**
	 * CREAR PEDIDO EN BBDD 
	 * @param pedidoDto
	 * @param idUsuario
	 * @return
	 */
	@PostMapping
	public ResponseEntity<PedidoDto> crearPedido(@RequestBody PedidoDto pedidoDto, @RequestParam Integer idUsuario) {
	
		HttpStatus estadoHTTP;
		PedidoDto salida;
		
		try {
			//VALIDACIÓN: comprobamos que el usuario ha seleccionado artículos y que existe stock
			if((!pedidoDto.getArticulos().isEmpty()) && (articuloService.existeStock(pedidoDto.getArticulos()))) {
				salida = pedidoService.crear(pedidoDto, idUsuario);			
			}else {
				salida = null;
			}
			estadoHTTP = HttpStatus.OK;	
		} 
		//FindById puede devolver un Optional vacío, si intentamos acceder a él saltará esta excepción
		catch(NoSuchElementException e) {
			salida = null;
			estadoHTTP = HttpStatus.OK;	
		}
		catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;	
		}
		
	
		return new ResponseEntity<PedidoDto>(salida, estadoHTTP);		
	}
	
	
	/**
	 * DEVUELVE UN PEDIDO DE LA BBDD BUSCANDO POR EL CAMPO ID
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")	
	public ResponseEntity<PedidoDto> devolverPedido(@PathVariable Integer id) {
		
		HttpStatus estadoHTTP;
		PedidoDto salida = new PedidoDto();
		
		try {
			salida = pedidoService.leerPorId(id);
			estadoHTTP = HttpStatus.OK;
		}
		catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<PedidoDto>(salida, estadoHTTP);
	}
	
	
	/**
	 * DEVUELVE UN LISTADO DE PEDIDOS COINCIDENTES CON UNA BÚSQUEDA PARCIAL DEL CAMPO NOMBRE
	 * @param nombreParcial
	 * @return
	 */
	@GetMapping("")
	public ResponseEntity<List<PedidoDto>> devolverPorLecturaParcial(@RequestParam String nombreParcial) {
		
		HttpStatus estadoHTTP;
		List<PedidoDto> salida = new ArrayList<PedidoDto>();

		try {
			salida = pedidoService.leerParcialmente(nombreParcial);
			estadoHTTP = HttpStatus.OK;
		}
		catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<PedidoDto>>(salida, estadoHTTP);
	}
	
	/**
	 * ELIMINA UN PEDIDO DE LA BBDD POR ID SELECCIONADO
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")	
	public ResponseEntity<PedidoDto> eliminarPedido(@PathVariable(value = "id") Integer id) {

		HttpStatus estado;
		PedidoDto salida = new PedidoDto();
		
		try {
			salida = pedidoService.eliminar(id);
			estado = HttpStatus.OK;
		}
		catch(Exception e) {
			salida = null;
			estado = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<PedidoDto>(salida, estado);
		
	}
	
	/**
	 * ACTUALIZA UN PEDIDO EN LA BBDD POR ID
	 * @param id
	 * @param nuevoPedidoDto
	 * @return
	 */
	@PutMapping("/{id}")	
	public ResponseEntity<PedidoDto> actualizarPedido(@PathVariable(value = "id") Integer id, @RequestBody PedidoDto nuevoPedidoDto) {
		
		HttpStatus estadoHTTP;
		PedidoDto salida = new PedidoDto();
		
		try {
			//VALIDACIÓN: Debemos validar que el id del PathVariable y del RequestBody sean el mismo y que existe el pedido
			if((pedidoService.leerPorId(id) != null) && (id.equals(nuevoPedidoDto.getId()))) {
				salida = pedidoService.actualizar(nuevoPedidoDto);
			}
			else {
				salida = null;
			}
			estadoHTTP = HttpStatus.OK;	
		}
		//FindById puede devolver un Optional vacío, si intentamos acceder a él saltará esta excepción
		catch(NoSuchElementException e) {
			salida = null;
			estadoHTTP = HttpStatus.OK;	
		}
		catch(Exception e) {
			salida = null;
			estadoHTTP = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<PedidoDto>(salida, estadoHTTP);	
	}
	
}
