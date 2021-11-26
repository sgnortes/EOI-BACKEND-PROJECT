package edu.es.eoi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PedidoArticulo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@Column
	private Integer cantidad;

	@ManyToOne(/*cascade = {CascadeType.MERGE}*/)
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "articulo_id")
	private Articulo articulo;
	
	
}
