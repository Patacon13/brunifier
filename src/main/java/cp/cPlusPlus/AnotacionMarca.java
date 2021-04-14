package main.java.cp.cPlusPlus;

public class AnotacionMarca {
	private int fila;
	private int posicionEnFila;
	private String marca;
	private String comentario = "/*/";
	public AnotacionMarca(int f, int p, String m) {
		fila = f;
		posicionEnFila = p;
		marca = m;
	}
	public AnotacionMarca(int f, int p, String m, String i) {
		fila = f;
		posicionEnFila = p;
		marca = m;
		comentario = i;
	}
	public void setFila(int f) {
		fila = f;
	}
	public int getFila() {
		return fila;
	}
	public void setPos(int f) {
		posicionEnFila = f;
	}
	public int getPos() {
		return posicionEnFila;
	}
	public void setMarca(String m) {
		marca = m;
	}
	public String getMarca() {
		return marca;
	}
	@Override
	public String toString() {
		return "(" + fila + "," + posicionEnFila + "," + marca + ")";
	}
	public String getTexto() {
		return comentario + marca + comentario;
	}
}
