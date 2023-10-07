package test;

import entities.*;
import management.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Test {
	public static void main(String[] args) {
//		System.out.println(cargar().getListaCanciones());
		ListaCanciones listaCanciones = cargar();
		Lectores.lector(listaCanciones);
		LectorMP3.lector(listaCanciones);
//		modificar(listaCanciones);
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Escriba GUARDAR para guardar");
//		String entrada = scanner.nextLine();
//		if ("GUARDAR".equals(entrada)) {
//			guardar(listaCanciones);
//		}
//		scanner.close();
//		System.out.println("TERMINADO");
		guardar(listaCanciones);
	}

	public static void guardar(ListaCanciones listaCanciones) {
		// TODO Auto-generated method stub
//		ListaCanciones listaCanciones = new ListaCanciones();
//		Lectores.lector(listaCanciones);
//		Lectores.lectorJson(listaCanciones);
//		LectorMP3.lector(listaCanciones);
//		System.out.println("Canciones:" + listaCanciones.getListaCanciones().size());
		String nombreArchivo = "finalist.txt";

		try (FileWriter fileWriter = new FileWriter(nombreArchivo);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

			for (Cancion cancion : listaCanciones.getListaCanciones()) {
				// Escribe cada canción en el archivo
				bufferedWriter.write("Título: " + cancion.getTitulo());
				bufferedWriter.newLine();
				bufferedWriter.write("Artista: " + cancion.getArtista());
				bufferedWriter.newLine();
				bufferedWriter.write("Álbum: " + cancion.getAlbum());
				bufferedWriter.newLine();
				bufferedWriter.write("Revisado: " + cancion.isRevisado());
				bufferedWriter.newLine();
				bufferedWriter.write("Rate: " + cancion.getRate());
				bufferedWriter.newLine();
				bufferedWriter.newLine(); // Separador entre canciones
			}

			System.out.println("Canciones guardadas en el archivo " + nombreArchivo);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ListaCanciones cargar() {
		// Nombre del archivo que contiene las canciones
		String nombreArchivo = "finalist.txt";

		ListaCanciones listaCanciones = new ListaCanciones();

		try (FileReader fileReader = new FileReader(nombreArchivo);
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			String titulo = null;
			String artista = null;
			String album = null;
			boolean revisado = false; // Agregamos una variable para guardar el valor de revisado
			int rate = 0;
			boolean flag = false;
			String linea;
			int i = 0;
			while ((linea = bufferedReader.readLine()) != null) {
				if (linea.startsWith("Título: ")) {
					titulo = linea.substring(8).trim();
				} else if (linea.startsWith("Artista: ")) {
					artista = linea.substring(9).trim();
				} else if (linea.startsWith("Álbum: ")) {
					album = linea.substring(7).trim();
				} else if (linea.startsWith("Revisado: ")) {
					revisado = Boolean.parseBoolean(linea.substring(10).trim());
					flag = true; // Esto es para el if de abajo, se puede obtener el booleano del archivo
				} else if (linea.startsWith("Rate: ")) {
					rate = Integer.parseInt(linea.substring(6).trim());
				}

				// Cuando se han leído todas las partes, agregar la canción
				if (titulo != null && artista != null && album != null && flag == true && rate != -1) {
					listaCanciones.agregarCanción(titulo, artista, album, revisado, rate);
					titulo = artista = album = null; // Reiniciar para la próxima canción
					flag = false; // Reiniciar revisado para la próxima canción
					rate = -1;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return listaCanciones;
	}

	public static void modificar(ListaCanciones listaCanciones) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < listaCanciones.getListaCanciones().size(); i++) {
			Cancion cancion = listaCanciones.getListaCanciones().get(i);
			if (cancion.isRevisado() == false) {
				System.out.println(cancion);
				System.out.println("editar?(a para editar, q para saltar, cualquier otra tecla para continuar)\n");
				String entrada = scanner.nextLine();
				if ("a".equals(entrada)) {
					System.out.println("Nombre:");
					String nombre = scanner.nextLine();
					if (!nombre.isEmpty()) { // Verifica si la entrada no está vacía
						cancion.setTitulo(nombre);
					}
					System.out.println("Artista:");
					String artista = scanner.nextLine();
					if (!artista.isEmpty()) { // Verifica si la entrada no está vacía
						cancion.setArtista(artista);
					}
					System.out.println("Álbum:");
					String album = scanner.nextLine();
					if (!album.isEmpty()) { // Verifica si la entrada no está vacía
						cancion.setAlbum(album);
					}
					cancion.setRevisado(true);
				} else if ("q".equals(entrada)) {
					// Esto confirma
					cancion.setRevisado(true);
				} else if ("s".equals(entrada)) {
					// Esto saltea
					System.out.println();
				} else if ("x".equals(entrada)) {
					// Esto termina
					i = listaCanciones.getListaCanciones().size();
				} else {
					i--;
					System.out.println("Por favor escriba a o q");
				}
				System.out.println(cancion);
			}
		}
		scanner.close();
	}

}
