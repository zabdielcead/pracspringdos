package com.bolsadeideas.springboot.reactor.app.models;

import java.util.ArrayList;
import java.util.List;

public class Comentarios {
	private List<String> comentarios;
	
	public Comentarios() {
		this.comentarios = new ArrayList<>();
	}

	

	public void addComentarios(String comentarios) {
		this.comentarios.add(comentarios);
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "comentarios="+ comentarios;
	}
	
	
	
	
	
	
}
