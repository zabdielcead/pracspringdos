package com.bolsadeideas.springboot.reactor.app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.reactor.app.models.Comentarios;
import com.bolsadeideas.springboot.reactor.app.models.Usuario;
import com.bolsadeideas.springboot.reactor.app.models.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{
	
	
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ejemploContraPresion();
	}
	
	
	public void ejemploDelayElements() {
		Flux<Integer> rango = Flux.range(1, 12)
									.delayElements(Duration.ofSeconds(1))
									.doOnNext(i -> log.info(i.toString()));
		
		
		rango.subscribe();
						
		
		
	}
	
	
	public void ejemploDesdecreate() {
		Flux.create(emitter -> {
			Timer time = new Timer();
			time.schedule(new TimerTask() {
				
				private Integer contador = 0;

				@Override
				public void run() {
					// TODO Auto-generated method stub
					emitter.next(++contador);
					if(contador == 10) {
						time.cancel();
						emitter.complete();
					}
					
					if(contador == 5) {
						time.cancel();
						emitter.error(new InterruptedException("Error, se ha detendio el flux"));
					}
					
				}
				
			}, 1000, 1000);
		})
//		.doOnNext(next -> log.info(next.toString()))
//		.doOnComplete(() -> log.info("hemos terminado"))
		.subscribe(next -> log.info(next.toString()),
					error -> log.error(error.getMessage()));
		
		
	}
	
	public void ejemploIntervalInfinito() {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux.interval(Duration.ofSeconds(1))
			.doOnTerminate(() -> latch.countDown())
			.flatMap(i -> {
				if(i >=5) {
					return Flux.error(new InterruptedException("Solo hasta 5"));
				}
				return Flux.just(i);
			})
			.map(i ->"Hola "+i)
			.retry(2)
			//.doOnNext(s-> log.info(s))
			.subscribe(s-> log.info(s) , e -> log.error(e.getMessage()));
						
		
		
	}
	
	
	public void ejemploInterval() {
		Flux<Integer> rango = Flux.range(1, 12);
		Flux<Long> 	retraso = Flux.interval(Duration.ofSeconds(1));
		
		rango.zipWith(retraso, (ra,re) -> ra)
			 .doOnNext(i -> log.info(i.toString()))
			 .blockLast();
		
	}
	
	
	
	public void ejemploComentarioZipWithRango() throws Exception {
		
		Flux<Integer> rangos = Flux.range(0, 4);
		
		Flux.just(1,2,3,4)
			.map(i -> (1*2))
			.zipWith(rangos,(uno, dos) -> String.format("Primer flux: %d, Segundo Flux: %d ", uno, dos ))
			.subscribe(texto -> log.info(texto));
			
		
	}
	
	public Usuario crearUsuario() {
		return new Usuario("Jhon", "Doe");
	}
	
	
	public void ejemploComentarioZipWithForma2() throws Exception {
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> crearUsuario());
		Mono<Comentarios> comentarios = Mono.fromCallable(()-> {
			Comentarios comentar = new Comentarios();
			comentar.addComentarios("ESCUELA");
			comentar.addComentarios("PLAYA");
			return comentar;
		});
		
		Mono<UsuarioComentarios> usuarioConcomentarios = usuarioMono
														.zipWith(comentarios)
														.map(tuple -> {
															Usuario u = tuple.getT1();
															Comentarios c = tuple.getT2();
															
															return new UsuarioComentarios(u, c);
														});
		
		usuarioConcomentarios.subscribe(uc -> log.info(uc.toString()));
		
	}
	
	
	public void ejemploComentarioZipWith() throws Exception {
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> crearUsuario());
		Mono<Comentarios> comentarios = Mono.fromCallable(()-> {
			Comentarios comentar = new Comentarios();
			comentar.addComentarios("ESCUELA");
			comentar.addComentarios("PLAYA");
			return comentar;
		});
		
		usuarioMono.zipWith(comentarios, (usuario,comentario) -> new UsuarioComentarios(usuario, comentario))
					.subscribe(uc -> log.info(uc.toString()));
		
	}
	
	public void ejemploComentarioFlatMap() throws Exception {
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> crearUsuario());
		Mono<Comentarios> comentarios = Mono.fromCallable(()-> {
			Comentarios comentar = new Comentarios();
			comentar.addComentarios("ESCUELA");
			return comentar;
		});
		
		usuarioMono.flatMap(u -> comentarios.map(c -> new UsuarioComentarios(u, c)))
					.subscribe(uc -> log.info(uc.toString()));
		
	}
	
	
	public void ejemploCollectList() throws Exception {
		List<Usuario> usuariosList = new ArrayList<Usuario>();
		usuariosList.add(new Usuario("Bruce", "Bonilla"));
		usuariosList.add(new Usuario("Juan", "Mendeta"));
		usuariosList.add(new Usuario("Bruce", "Navajas"));
		usuariosList.add(new Usuario("Bruja", "Nieves"));
		
		Flux.fromIterable(usuariosList)
			.collectList()
			.subscribe(
					listaUsers -> {
						
						listaUsers.forEach( item -> log.info(item.toString()));
					});
	}
	
	
	public void ejemploToString() throws Exception {
		// TODO Auto-generated method stub
		
		List<Usuario> usuariosList = new ArrayList<Usuario>();
		usuariosList.add(new Usuario("Bruce", "Bonilla"));
		usuariosList.add(new Usuario("Juan", "Mendeta"));
		usuariosList.add(new Usuario("Bruce", "Navajas"));
		usuariosList.add(new Usuario("Bruja", "Nieves"));
		
		
		
		 Flux.fromIterable(usuariosList)
		 	.map(usuario -> {
				return usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase());
			})
		 	.flatMap(nombre -> {
		 		if(nombre.contains("bruce".toUpperCase())) {
		 			return Mono.just(nombre);
		 		}else {
		 			return Mono.empty();
		 		}
		 	})
		 	//.filter(usuario ->{ return usuario.getNombre().toLowerCase().equals("bruce"); })
			.map(nombre -> {
					
					
					
					return nombre.toLowerCase();
					
				})
			.subscribe(u  	  -> log.info("log="+u.toString()));
					
					/*
					error -> log.error(error.getMessage()),
					new Runnable() {
						
						@Override
						public void run() {
							log.info("sejecuto exitosamente");
							
						}
					}
				);
					 */
		
		
		
		
		
	}
	
	
	
	public void ejemploFlatMap() throws Exception {
		// TODO Auto-generated method stub
		
		List<String> usuariosList = new ArrayList<String>();
		usuariosList.add("Bruce Bonila");
		usuariosList.add("Juan Mendeta");
		usuariosList.add("Bruce Navajas");
		usuariosList.add("Bruja Nieves");
		
		
		
		 Flux.fromIterable(usuariosList)
		 	.map(nombre -> {
				return new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase());
			})
		 	.flatMap(usuario -> {
		 		if(usuario.getNombre().equalsIgnoreCase("bruce")) {
		 			return Mono.just(usuario);
		 		}else {
		 			return Mono.empty();
		 		}
		 	})
		 	//.filter(usuario ->{ return usuario.getNombre().toLowerCase().equals("bruce"); })
			.map(usuario -> {
					String nombre =  usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					
					return usuario;
					
				})
			.subscribe(u  	  -> log.info("log="+u.toString()));
					
					/*
					error -> log.error(error.getMessage()),
					new Runnable() {
						
						@Override
						public void run() {
							log.info("sejecuto exitosamente");
							
						}
					}
				);
					 */
		
		
		
		
		
	}
	
	public void ejemploIterable() throws Exception {
		// TODO Auto-generated method stub
		
		List<String> usuariosList = new ArrayList<String>();
		usuariosList.add("Andres");
		usuariosList.add("Juan");
		usuariosList.add("Pedro");
		
		
		
		Flux<String> nombresList = Flux.fromIterable(usuariosList);
		
		
		
		
		Flux<Usuario> nombres = Flux.just("Andres Guzman","Pedro Fulano","Diego Valadez","Juan Pistolas","Bruce Lee", "Bruce Willis")
				.map(nombre -> {
					return new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase());
				})
				.filter(usuario ->{ return usuario.getNombre().toLowerCase().equals("bruce"); })
				.doOnNext(
							usuario -> {
								
								if(usuario == null) {
									throw new RuntimeException("Nombres no van vacios");
								}
									System.out.println("hola"+usuario.getNombre().concat(" ").concat(usuario.getApellido()));
								
								
							}
						).map(usuario -> {
							String nombre =  usuario.getNombre().toLowerCase();
							usuario.setNombre(nombre);
							
							return usuario;
							
						})
							;
		
		nombres.subscribe(
							e  	  -> log.info("log="+e.getNombre()),
							error -> log.error(error.getMessage()),
							new Runnable() {
								
								@Override
								public void run() {
									log.info("sejecuto exitosamente");
									
								}
							}
						);
	}
	
	
	
	
	public void ejemploContraPresion() {
		Flux.range(1, 10)
			.log()
			.limitRate(2) //se mandaran dos valores como paginacion
			.subscribe(
					
					
					/*
					new Subscriber<Integer>() {
				
				private Subscription s; 
				private Integer limite = 2;
				private Integer consumido = 0;

				@Override
				public void onSubscribe(Subscription s) {
					// TODO Auto-generated method stub
					this.s = s;
					s.request(limite);
					
				}

				@Override
				public void onNext(Integer t) {
					// TODO Auto-generated method stub
					log.info(t.toString());
					consumido++;
					
					if(consumido == limite) {
						consumido = 0;
						s.request(limite);
						
					}
					
				}

				@Override
				public void onError(Throwable t) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					
				}
				
			}*/
					);
	}

}
