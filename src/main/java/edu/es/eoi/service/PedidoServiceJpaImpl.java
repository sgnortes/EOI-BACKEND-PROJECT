package edu.es.eoi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.es.eoi.dto.PedidoArticuloDto;
import edu.es.eoi.dto.PedidoDto;
import edu.es.eoi.entity.Articulo;
import edu.es.eoi.entity.Pedido;
import edu.es.eoi.entity.PedidoArticulo;
import edu.es.eoi.repository.ArticuloRepository;
import edu.es.eoi.repository.PedidoRepository;
import edu.es.eoi.repository.UsuarioRepository;

@Service
public class PedidoServiceJpaImpl {

	
	@Autowired
	PedidoRepository pedidoRepo;
	
	@Autowired
	ArticuloRepository articuloRepo;
	
	@Autowired
	UsuarioRepository usuarioRepo;
	
	
	/**
	 * FUNCIÓN QUE CREA PEDIDOS EN BBDD
	 * @param dto
	 * @return
	 */
	public PedidoDto crear(PedidoDto dto, Integer idUsuario) {
		
		Pedido pedido = new Pedido();
		
		pedido.setNombre(dto.getNombre());
		pedido.setFecha(dto.getFecha());
		pedido.setUsuario(usuarioRepo.findById(idUsuario).get());
		//pedido.setCompras(new ArrayList<PedidoArticulo>());
		
		List<PedidoArticuloDto> articulosDto = dto.getArticulos();
		List<PedidoArticulo> articulos = new ArrayList<PedidoArticulo>();
		
		for(PedidoArticuloDto articuloDto : articulosDto) {
			
			PedidoArticulo articulo = new PedidoArticulo();
			
			articulo.setArticulo(articuloRepo.findById(articuloDto.getIdArticulo()).get());
			articulo.setCantidad(articuloDto.getCantidad());
			articulo.setPedido(pedido);
			
			articulos.add(articulo);
			
			//Modificación del stock
			modificarStock(articuloDto.getIdArticulo(), (- articuloDto.getCantidad()));
			
		}
		
		pedido.setCompras(articulos);
		
		pedidoRepo.save(pedido);
		
		return dto;	
		
	}
	
	
	/**
	 * FUNCIÓN QUE MODIFICA EL STOCK DE LOS PRODUCTOS EN BBDD
	 * @param id
	 * @param cantidad
	 */
	public void modificarStock(Integer id, Integer cantidad) {
		
		Articulo articulo = articuloRepo.findById(id).get();
		
		articulo.setStock(articulo.getStock() + cantidad);
	
		articuloRepo.save(articulo);
		
	}
	
	
	/**
	 * FUNCIÓN QUE ACTUALIZA UN PEDIDO EN LA BBDD. Si uno de los artículos para actualizar no existe
	 * se lanzará un NoSuchElementException
	 * @param dto
	 * @return
	 */
	public PedidoDto actualizar(PedidoDto dto) {
		
		Pedido pedidoMod = pedidoRepo.findById(dto.getId()).get();
		
		//Reseteo del Stock: a los productos que finalmente no se han comprado les modificamos el Stock
		//Seteo a null de los artículos no deseados del pedido
		for(PedidoArticulo articuloViejo : pedidoMod.getCompras()) {
			modificarStock(articuloViejo.getArticulo().getId(), articuloViejo.getCantidad());
			articuloViejo.setPedido(null);
		}	
		
		pedidoMod.setNombre(dto.getNombre());
		pedidoMod.setFecha(dto.getFecha());
		pedidoMod.setCompras(null);
		
		List<PedidoArticulo> nuevosArticulos = new ArrayList<PedidoArticulo>();
		
		for(PedidoArticuloDto articuloDto : dto.getArticulos()) {
			
			PedidoArticulo nuevoArticulo = new PedidoArticulo();
			nuevoArticulo.setArticulo(articuloRepo.findById(articuloDto.getIdArticulo()).get());
			nuevoArticulo.setCantidad(articuloDto.getCantidad());
			nuevoArticulo.setPedido(pedidoMod);
			
			nuevosArticulos.add(nuevoArticulo);
			
			//Modificación del stock
			modificarStock(articuloDto.getIdArticulo(), (- articuloDto.getCantidad()));
			
		}
		
		pedidoMod.setCompras(nuevosArticulos);
		
		pedidoRepo.save(pedidoMod);
		
		return dto;	
	}
	

	/**
	 * FUNCIÓN QUE DEVUELVE UN PEDIDO POR ID
	 * @param primaryKey
	 * @return
	 */
	public PedidoDto leerPorId(Integer primaryKey) {
		
		PedidoDto pedidoDto = new PedidoDto();
		Optional<Pedido> pedido = pedidoRepo.findById(primaryKey);
		
		if(pedido.isPresent()) {
			BeanUtils.copyProperties(pedido.get(), pedidoDto);
			List<PedidoArticulo> articulos = pedido.get().getCompras();
			List<PedidoArticuloDto> articulosDto = listaDeArticulosADto(articulos);
			pedidoDto.setArticulos(articulosDto);
		}
		else {
			pedidoDto = null;
		}
		
		return pedidoDto;	
	}
	

	/**
	 * FUNCIÓN QUE ELIMINA UN PEDIDO EN BBDD
	 * @param primaryKey
	 * @return
	 */
	public PedidoDto eliminar(Integer primaryKey) {
		
		//Comprobamos si existe en la BBDD el pedido a eliminar
		Optional<Pedido> pedido = pedidoRepo.findById(primaryKey);
		PedidoDto salida = new PedidoDto();
		
		//Si está lo eliminamos
		if(pedido.isPresent()) {
			salida = entidadADto(pedido.get());
			pedidoRepo.deleteById(primaryKey);		
		}
		else {
			salida = null;
		}
		
		return salida;
		
	}
	
	
	/**
	 * FUNCIÓN QUE CON UNA LECTURA PARCIAL DE UN CAMPO DEVUELVE RESULTADOS COINCIDENTES
	 * @param nombre
	 * @return
	 */
	public List<PedidoDto> leerParcialmente(String nombre) {
		
		List<PedidoDto> pedidosDto = new ArrayList<PedidoDto>();
		Optional <List<Pedido>> pedidos = pedidoRepo.findByNombreContains(nombre);
		
		if(pedidos.isPresent()) {
			for(Pedido pedido : pedidos.get()) {
				PedidoDto pedidoDto = leerPorId(pedido.getId());
				pedidosDto.add(pedidoDto);
			}
		}
		else {
			pedidosDto = null;
		}
			
		return pedidosDto;	
		
	}
	
	/**
	 * Función que convierte una lista de PedidoArticulo a una lista de PedidoArticuloDTO
	 * @param pedidosArticulos
	 * @return
	 */
	public List<PedidoArticuloDto> listaDeArticulosADto(List<PedidoArticulo> pedidosArticulos){
		List<PedidoArticuloDto> pedidosArticulosDto = new ArrayList<PedidoArticuloDto>();
		
		for(PedidoArticulo pedidoArticulo : pedidosArticulos) {
			PedidoArticuloDto pedidoArticuloDto = new PedidoArticuloDto();
			BeanUtils.copyProperties(pedidoArticulo, pedidoArticuloDto);
			pedidoArticuloDto.setIdArticulo(pedidoArticulo.getArticulo().getId());
			pedidosArticulosDto.add(pedidoArticuloDto);
		}
		
		return pedidosArticulosDto;
		
	}
	
	/**
	 * Convierte un objeto Pedido a PedidoDto
	 * @param pedido
	 * @return
	 */
	public PedidoDto entidadADto(Pedido pedido) {
		
		PedidoDto salida = new PedidoDto();
		
		salida.setId(pedido.getId());
		salida.setNombre(pedido.getNombre());
		salida.setFecha(pedido.getFecha());
		salida.setArticulos(new ArrayList<PedidoArticuloDto>());
		
		for(PedidoArticulo articulo : pedido.getCompras()) {
			PedidoArticuloDto articuloDto = new PedidoArticuloDto();
			articuloDto.setCantidad(articulo.getCantidad());
			articuloDto.setIdArticulo(articulo.getArticulo().getId());
			salida.getArticulos().add(articuloDto);
		}
		
		return salida;	
	}
	
}
