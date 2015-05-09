package py.una.pol.distribuidos.pizarra.servidor;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PizarraInterfaz extends Remote {
	
	public boolean Registrar(String nombre, String direccion, int puerto) throws RemoteException;
	public boolean EstaDisponible(String nombre) throws RemoteException;
	public Dimension obtenerDimensiones() throws RemoteException;
	public boolean[][] obtenerMatriz() throws RemoteException;
	public boolean actualizar(Punto[] puntos, String name) throws RemoteException, InterruptedException;
	
	public class Punto implements Serializable
	{
		private static final long serialVersionUID = 7589610008843242764L;
		
		public Point posicion;
		public boolean estado;
		public Punto(Point posicion, boolean estado) {
			this.posicion = posicion;
			this.estado = estado;
		}
	}
}
