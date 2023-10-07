package entities;

public class Cancion {
	private int id;
	private String titulo;
	private String artista;
	private String album;
	private boolean revisado;
	private int rate;
	// Constructor
    public Cancion(int id, String titulo, String artista, String album, boolean revisado, int rate) {
        this.id = id;
    	this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.revisado = revisado;
        this.rate=rate;
    }

    // Getters
    public int getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getAlbum() {
        return album;
    }

	public boolean isRevisado() {
		return revisado;
	}

	public void setRevisado(boolean revisado) {
		this.revisado = revisado;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "Cancion [id=" + id + ", titulo=" + titulo + ", artista=" + artista + ", album=" + album + ", revisado="
				+ revisado + ", rate="+ rate + "]\n";
	}
    
    
    
        
}

