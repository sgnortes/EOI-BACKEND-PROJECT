package edu.es.eoi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloDto {

	private Integer id;
	
	private String nombre;
	
	private Double precio;
	
	private Integer stock;
	
}
