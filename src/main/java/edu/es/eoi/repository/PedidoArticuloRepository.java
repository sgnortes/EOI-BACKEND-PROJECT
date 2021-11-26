package edu.es.eoi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.es.eoi.entity.Pedido;
import edu.es.eoi.entity.PedidoArticulo;

@Repository
public interface PedidoArticuloRepository extends JpaRepository<PedidoArticulo, Integer> {
	
	public List<PedidoArticulo> findByPedido(Pedido pedido);

}