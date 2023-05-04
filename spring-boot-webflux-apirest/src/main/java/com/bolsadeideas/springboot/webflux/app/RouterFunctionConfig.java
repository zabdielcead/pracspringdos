package com.bolsadeideas.springboot.webflux.app;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.handler.ProductoHandler;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.service.ProductoService;

@Configuration
public class RouterFunctionConfig {
	
	

	
	
	@Bean
	public RouterFunction<ServerResponse> routes(ProductoHandler productohandler){
		
		
		return RouterFunctions.route(RequestPredicates.GET("/api/v2/productos").or(RequestPredicates.GET("/api/v3/productos")), request -> 	productohandler.listar(request))
								.andRoute(RequestPredicates.GET("/api/v2/productos/{id}").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), productohandler::ver);
			
	}

}
