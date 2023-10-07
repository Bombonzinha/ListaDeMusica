package management;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import entities.ListaCanciones;

public class Lectores {
	public static String spotify = "spotify.txt";
	public static String youtube = "youtube.json";
	public static String listaSpotify = "";

	public static void lector(ListaCanciones listaCanciones) {
		StringBuilder listaSpotifyBuilder = new StringBuilder();
		listaSpotifyBuilder.append(listaSpotify);
//		ListaCanciones listaCanciones = new ListaCanciones();
		try {
			// Crea un objeto FileReader para abrir el archivo
			FileReader archivo = new FileReader(spotify);
			
			// Crea un objeto BufferedReader para leer líneas de texto
			BufferedReader lector = new BufferedReader(archivo);
			lector.readLine(); // Salta la primera linea
			String linea;
			String nuevaLinea = null;
			String[] datos = new String[3];
			while ((linea = lector.readLine()) != null) {
				datos = nombreArtistaAlbum(linea); // Saco los datos con separadores
				nuevaLinea = datos[0] + "|"+ datos[1] + "|" + datos[2] + "\n";
				if (noExiste(listaSpotify, nuevaLinea)) {
					listaSpotifyBuilder.append(nuevaLinea); // los agregos a la lista
					listaCanciones.agregarCanción(datos[0], datos[1], datos[2], false);
				}
			}
			// Cierra el archivo después de leerlo
			lector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listaSpotify = listaSpotifyBuilder.toString();
//		return listaCanciones;
	}

	public static String[] nombreArtistaAlbum(String linea) {
		// Divide la línea en partes utilizando el carácter ","
		String[] partes = linea.split(",");
		String[] retorno = new String[3];
		if (partes.length >= 3) {
			retorno[0] = partes[0].replaceAll("\"", ""); // Elimina las comillas
			retorno[1] = partes[1].replaceAll("\"", "");
			retorno[2] = partes[2].replaceAll("\"", "");
		} else {
			System.out.println("La línea no contiene al menos 3 partes.");
		}
		return retorno;
	}

	public static void lectorJson(ListaCanciones listaCanciones) {
//		ListaCanciones listaCanciones = new ListaCanciones();
		try (FileReader fileReader = new FileReader("youtube.json")) {
			JSONTokener tokener = new JSONTokener(fileReader);
			JSONArray jsonArray = new JSONArray(tokener); // estas serian las lineas

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String title = jsonObject.getString("title");
				String uploader = jsonObject.getString("uploader");
				listaCanciones.agregarCanción(title, uploader, null, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
//		return listaCanciones;
	}

	// Si ya está en la lista esta misma canción entonces no se agrega
	public static boolean noExiste(String lista, String naa) {
		try (Scanner scanner = new Scanner(lista)) {
			while (scanner.hasNextLine()) {
				String linea = scanner.nextLine();
				if (naa.equals(linea)) {
					return false; // Se encontró la coincidencia, noExiste = false
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static String modificarJson(String ruta) {
		String archivoSalida = "youtubeModificado.json";

		try (BufferedReader reader = new BufferedReader(new FileReader(ruta));
				BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {

			String linea;
			boolean esPrimerObjeto = true;

			while ((linea = reader.readLine()) != null) {
				if (!esPrimerObjeto) {
					// Agregar una coma al final de la línea anterior
					writer.write(",");
					writer.newLine();
				}
				writer.write(linea);
				esPrimerObjeto = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return archivoSalida;
	}

	public static void main(String[] args) {
//		System.out.println(lector(spotify));
	}
}
