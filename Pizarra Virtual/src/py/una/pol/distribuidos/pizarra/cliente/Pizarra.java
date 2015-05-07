package py.una.pol.distribuidos.pizarra.cliente;

import java.awt.Dimension;

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
		System.out.println("Matriz recibida tiene dimensiones " + matriz.length + " x " + matriz[0].length);
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
		{
			if (puntos.length < 10)
			//System.out.printf(" %d << Modificando (%d, %d) = %s\n", puntos.length , p.posicion.x, p.posicion.y, Boolean.toString(p.estado));
	
			matriz[p.posicion.y][p.posicion.x] = p.estado;
		}
		
	}
	
	public Dimension getDimension(){
		return new Dimension(matriz[0].length, matriz.length);
	}

}
