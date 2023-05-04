package com.bolsadeideas.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.service.ProductoService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApirestApplication implements CommandLineRunner{
	
	
	
	
	
	
	@Autowired
	private ProductoService service;
	
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApirestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApirestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();
		
		Categoria electronica 	= new Categoria("Electrónico");
		Categoria deporte 	= new Categoria("Deporte");
		Categoria computacion  	= new Categoria("Computación");
		Categoria muebles	= new Categoria("muebles");
		
		
		Flux.just(electronica, deporte, computacion, muebles)
			.flatMap(c -> service.saveCategoria(c))
			.doOnNext(c -> {
				log.info("Categoria creada: " + c.getNombre() + ", ID: "+ c.getId());
			}).thenMany(
					
					
					Flux.just(
							new Producto("TV PAGA", 119.78,electronica),
							new Producto("Sony", 11.31, electronica),
							new Producto("Ipod", 16.90, electronica),
							new Producto("balon", 12.90, deporte),
							new Producto("pc gamer", 1233.90, computacion),
							new Producto("mesa", 130.90, muebles)
						
					   )
						.flatMap(producto -> {
							producto.setCreateAt(new Date());
							return service.save(producto);
						})
		
				)
					
				.subscribe(producto -> log.info("Insert: "+ producto.getId() + " " + producto.getNombre() ));
		
	}
	
	
	

}
