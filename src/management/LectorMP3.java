package management;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import entities.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LectorMP3 {
	public static void lector(ListaCanciones listaCanciones) {
		String carpeta = "D:\\Música";

		File directorio = new File(carpeta);
		File[] archivosMP3 = directorio.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".mp3"));
//		ListaCanciones listaCanciones = new ListaCanciones();

		// Obtener metadatos de archivos en la carpeta y agregarlos a la lista
		for (File archivo : archivosMP3) {
			String[] metadatos = obtenerMetadatosMP3(archivo);
			if (metadatos != null) {
				listaCanciones.agregarCanción(metadatos[0], metadatos[1], metadatos[2], false, 0);
			} else {
				System.out.println("No se encontraron archivos MP3 en la carpeta.");
			}
		}
//		System.out.println(listaCanciones.getListaCanciones());
//		System.out.println(listaCanciones.getListaCanciones().size());
	}

	private static String[] obtenerMetadatosMP3(File archivoMP3) {
		try {
			File file = new File(archivoMP3.getAbsolutePath());
			AudioFile audioFile = AudioFileIO.read(file);
			Tag tag = audioFile.getTag();

			// Obtener el título, artista y álbum de la pista de MP3
			String title = tag.getFirst(FieldKey.TITLE);
			String artist = tag.getFirst(FieldKey.ARTIST);
			String album = tag.getFirst(FieldKey.ALBUM);
			String[] metadatos = { title, artist, album };

			return metadatos;
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Manejo de errores
		}
	}

	public static void main(String[] args) {
		ListaCanciones listaCanciones = new ListaCanciones();
		Scanner scanner = new Scanner(System.in);
		lector(listaCanciones);
		for (int i = 0; i < listaCanciones.getListaCanciones().size(); i++) {
			Cancion cancion = listaCanciones.getListaCanciones().get(i);
			System.out.println(cancion);
			System.out.println("editar?(a para editar, q para saltar, cualquier otra tecla para continuar)\n");
			String entrada = scanner.nextLine();
			if ("a".equals(entrada)) {
				System.out.println("Nombre:");
				String nombre = scanner.nextLine();
				System.out.println("Artista:");
				String artista = scanner.nextLine();
				System.out.println("Álbum:");
				String album = scanner.nextLine();
				cancion.setTitulo(nombre);
				cancion.setArtista(artista);
				cancion.setAlbum(album);
			} else if("q".equals(entrada)){
				//Esto saltea
			} else {
				i--;
				System.out.println("Por favor escriba a o q");
			}
			System.out.println(cancion);
		}
		scanner.close();
	}
}
