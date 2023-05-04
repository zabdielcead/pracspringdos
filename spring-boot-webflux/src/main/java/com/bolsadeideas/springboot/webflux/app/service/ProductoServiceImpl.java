package com.bolsadeideas.springboot.webflux.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.webflux.app.models.dao.CategoriaDao;
import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProductoServiceImpl implements ProductoService{
	
	@Autowired
	private ProductoDao dao;
	
	
	@Autowired
	private CategoriaDao categoriaDao;

	@Override
	public Flux<Producto> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public Mono<Producto> findById(String id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		// TODO Auto-generated method stub
		return dao.save(producto);
	}

	@Override
	public Mono<Void> delete(Producto producto) {
		// TODO Auto-generated method stub
		return dao.delete(producto);
	}

	@Override
	public Flux<Producto> findAllConNombreUpperCase() {
		// TODO Auto-generated method stub
		return dao.findAll().map(producto -> {
			
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		});
	}

	@Override
	public Flux<Producto> findAllConNombreRepeat() {
		// TODO Auto-generated method stub
		return findAllConNombreUpperCase().repeat(5000);
	}

	@Override
	public Flux<Categoria> findAllCategoria() {
		// TODO Auto-generated method stub
		return categoriaDao.findAll();
	}

	@Override
	public Mono<Categoria> findCategoriaById(String id) {
		// TODO Auto-generated method stub
		return categoriaDao.findById(id);
	}

	@Override
	public Mono<Categoria> saveCategoria(Categoria categoria) {
		// TODO Auto-generated method stub
		return categoriaDao.save(categoria);
	}

}
