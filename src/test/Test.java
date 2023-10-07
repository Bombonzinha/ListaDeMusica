package test;

import entities.*;
import management.*;
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ListaCanciones listaCanciones = new ListaCanciones();
		Lectores.lector(listaCanciones);
		Lectores.lectorJson(listaCanciones);
		LectorMP3.lector(listaCanciones);
		System.out.println("Canciones:" + listaCanciones.getListaCanciones().size());
	}

}
