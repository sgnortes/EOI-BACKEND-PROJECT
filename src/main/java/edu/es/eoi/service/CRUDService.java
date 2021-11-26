package edu.es.eoi.service;

public interface CRUDService <Dto, PK>{
	
	public Dto crear(Dto dto);
	
	public Dto actualizar(Dto dto);
	
	public Dto leerPorId(PK primaryKey);
	
	public Dto eliminar(PK primaryKey);

}
