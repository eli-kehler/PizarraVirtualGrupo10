package py.una.pol.distribuidos.pizarra.cliente;

import java.awt.Dimension;
import java.util.ArrayList;

import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class Pizarra {
	
	/*
	 * Representa la pizarra
	 */
	
	private boolean[][] matriz;
	private String pintor;
	public Pizarra(boolean[][] matriz, String pintor) {
		this.matriz = matriz;
		this.pintor = pintor;
	}
	
	public Pizarra(String pintor){
		this.pintor = pintor;
	}
	
	

	public boolean[][] getMatriz() {
		return matriz;
	}
	public void setMatriz(boolean[][] matriz) {
		this.matriz = matriz;
	}
	public String getPintor() {
		return pintor;
	}
	public void setPintor(String pintor) {
		this.pintor = pintor;
	}
	
	public synchronized void  actualizarMatriz(Punto[] puntos){
		
		for (Punto p : puntos)
			matriz[p.posicion.x][p.posicion.y] = p.estado;
		
		
	}
	
	public Dimension getDimension(){
		return new Dimension(matriz.length, matriz[0].length);
	}

}
