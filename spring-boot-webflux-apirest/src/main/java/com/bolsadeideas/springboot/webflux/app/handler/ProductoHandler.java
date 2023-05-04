package com.bolsadeideas.springboot.webflux.app.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.service.ProductoService;

import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;


@Component
public class ProductoHandler {
	
	
	@Autowired
	private ProductoService service;

	public Mono<ServerResponse> listar(ServerRequest req){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.findAll(), Producto.class);
	}
	
	
	public Mono<ServerResponse> ver(ServerRequest req){
		
		
		String id = req.pathVariable("id");
		return service.findById(id).flatMap(p -> ServerResponse
															.ok()
															.contentType(MediaType.APPLICATION_JSON)
															.bodyValue(p)
															.switchIfEmpty(ServerResponse.notFound().build()));
	}
	
}
