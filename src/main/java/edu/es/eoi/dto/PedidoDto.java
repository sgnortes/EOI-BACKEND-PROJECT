package edu.es.eoi.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

	private Integer id;
	
	private String nombre;
	
	private LocalDate fecha;
	
	private List<PedidoArticuloDto> articulos;
	
}
