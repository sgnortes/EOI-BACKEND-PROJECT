package edu.es.eoi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import edu.es.eoi.dto.ArticuloDto;
import edu.es.eoi.dto.PedidoArticuloDto;
import edu.es.eoi.entity.Articulo;
import edu.es.eoi.repository.ArticuloRepository;

@Service
public class ArticuloServiceJpaImpl implements CRUDService <ArticuloDto, Integer>{
	
	@Autowired
	ArticuloRepository articuloRepo;

	/**
	 * Función que crea artículo en base de datos
	 */
	@Override
	public ArticuloDto crear(ArticuloDto dto) {
		
		Articulo articulo = new Articulo();
		dto.setId(null);
		
		//LOGICA DE NEGOCIO: No pueden existir dos artículos con el mismo nombre en BBDD
		if(articuloRepo.findByNombre(dto.getNombre()).isPresent() == false) {
			BeanUtils.copyProperties(dto, articulo);
			articulo = articuloRepo.save(articulo);
			dto.setId(articulo.getId());
		}
		
		return dto;
		
	}

	/**
	 * Función que actualiza el artículo en base de datos
	 */
	@Override
	public ArticuloDto actualizar(ArticuloDto dto) {
		
		//Necesario hacerlo para actualizar
		Articulo articulo = articuloRepo.findById(dto.getId()).get();
		
		//Cambiamos datos	
		articulo.setNombre(dto.getNombre());
		articulo.setPrecio(dto.getPrecio());
		articulo.setStock(dto.getStock());
		
		//Actualizamos
		articuloRepo.save(articulo);
		
		return dto;
		
	}

	/**
	 * Función que devuelve el artículo por id
	 */
	@Override
	public ArticuloDto leerPorId(Integer primaryKey){
		
		Articulo articulo = new Articulo();
		ArticuloDto articuloDto = new ArticuloDto();
		
		Optional<Articulo> articuloBBDD = articuloRepo.findById(primaryKey);
		
		if(articuloBBDD.isPresent()) {
			articulo = articuloBBDD.get();
			BeanUtils.copyProperties(articulo, articuloDto);
		}
		else {
			articuloDto = null;
		}
		
		return articuloDto;	
	}
	
	//No hacía falta implementarlo
	@Override
	public ArticuloDto eliminar(Integer primaryKey) {
		return null;
	}

	/**
	 * Función que lee parcialmente un campo en la base de datos y devuelve una lista con las coincidencias
	 * @param nombre
	 * @return
	 */
	public List<ArticuloDto> leerParcialmente(String nombre) {
		
		List<ArticuloDto> articulosDto = new ArrayList<ArticuloDto>();
		Optional<List<Articulo>> articulos = articuloRepo.findByNombreContains(nombre);
		
		if(articulos.isPresent()) {
			articulosDto = articulosAArticulosDto(articulos.get());
		}
		else {
			articulosDto = null;
		}
		
		return articulosDto;	
	}
	
	/**
	 * Función que convierte una lista de Articulos a una lista de Articulos DTO
	 * @param articulos
	 * @return
	 */
	public List<ArticuloDto> articulosAArticulosDto(List<Articulo> articulos){
		
		List<ArticuloDto> articulosDto = new ArrayList<ArticuloDto>();
		
		for(Articulo articulo : articulos) {
			ArticuloDto articuloDto = new ArticuloDto();
			BeanUtils.copyProperties(articulo, articuloDto);
			articulosDto.add(articuloDto);
		}
		
		return articulosDto;
		
	}

	/**
	 * Función que comprueba si existe stock para una serie de artículos
	 * @param pedidosArticulosDto
	 * @return
	 */
	public Boolean existeStock(List<PedidoArticuloDto> pedidosArticulosDto) {
		
		//Si al menos uno de los artículos a pedir no tiene stock suficiente devolvemos false
		for(PedidoArticuloDto pedidoArticuloDto : pedidosArticulosDto) {
			Articulo articulo = articuloRepo.getById(pedidoArticuloDto.getIdArticulo());
			//Comprobamos el Stock
			if(articulo.getStock() < pedidoArticuloDto.getCantidad()) {
				return false;
			}
		}
		
		return true;
	}
	
}
