package com.bolsadeideas.springboot.webflux.app.service;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
	public Flux<Producto> findAll();
	
	public Flux<Producto> findAllConNombreUpperCase();
	public Flux<Producto> findAllConNombreRepeat();
	
	public Mono<Producto> findById(String id);
	
	public Mono<Producto> save(Producto producto);
	
	public Mono<Void> delete(Producto producto);
	
	public Flux<Categoria> findAllCategoria();  
	
	
	public Mono<Categoria> findCategoriaById(String id);
	
	
	public Mono<Categoria> saveCategoria(Categoria categoria);
}
