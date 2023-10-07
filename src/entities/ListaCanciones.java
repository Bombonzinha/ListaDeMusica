package entities;

import java.util.List;
import java.util.ArrayList;

public class ListaCanciones {
	List<Cancion> listaCanciones;

	public ListaCanciones() {
		this.listaCanciones = new ArrayList<>();
	}

	public List<Cancion> getListaCanciones() {
		return listaCanciones;
	}

	public void setListaCanciones(List<Cancion> listaCanciones) {
		this.listaCanciones = listaCanciones;
	}
	
	public boolean agregarCanción(String title, String artist, String album, boolean revisado, int rate){
		boolean retorno=false;
		if(traerCancion(title, artist)==null) {
			int maxId=0;
			if (listaCanciones.size()!=0) {
				maxId=listaCanciones.get(listaCanciones.size()-1).getId()+1;
			}
			retorno=listaCanciones.add(new Cancion(maxId, title, artist, album, revisado, rate));
		}
		return retorno;
	}
	
	public Cancion traerCancion(String titulo, String artista) {
		Cancion retorno= null;
		for(int i=0;i<listaCanciones.size();i++) {
			if (listaCanciones.get(i).getTitulo().contains(titulo) && listaCanciones.get(i).getArtista().contains(artista)) {
				retorno=listaCanciones.get(i);
			}
		}
		return retorno;		
	}
	
	public Cancion traerCancion(int id) {
		Cancion retorno= null;
		for(int i=0;i<listaCanciones.size();i++) {
			if (listaCanciones.get(i).getId()==id) {
				retorno=listaCanciones.get(i);
			}
		}
		return retorno;		
	}
	
	public boolean eliminarCancion(int id) {
		Cancion cancion = traerCancion(id);
		listaCanciones.remove(listaCanciones.indexOf(cancion));
		return true; 
	}
	
	public boolean actualizarCampoCancion(int id, String campo, String valor) {
        Cancion cancion = traerCancion(id);

        if (cancion != null) {
            switch (campo) {
                case "titulo":
                    cancion.setTitulo(valor);
                    break;
                case "artista":
                    cancion.setArtista(valor);
                    break;
                case "album":
                    cancion.setAlbum(valor);
                    break;
                case "revisado":
                    cancion.setRevisado(Boolean.parseBoolean(valor));
                    break;
                case "rate":
                    cancion.setRate(Integer.parseInt(valor));
                    break;
                default:
                    return false;
            }
            return true;
        }

        return false;
    }
}
