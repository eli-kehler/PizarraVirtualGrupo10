package py.una.pol.distribuidos.pizarra.servidor;

import java.awt.Dimension;
import java.awt.Point;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PizarraInterfaz extends Remote {
	
	public boolean Registrar(String nombre, String direccion, int puerto) throws RemoteException;
	public Dimension obtenerDimensiones() throws RemoteException;
	public boolean[][] obtenerMatriz() throws RemoteException;
	public boolean actualizar(Punto[] puntos) throws RemoteException;
	
	public class Punto
	{
		public Point posicion;
		public boolean estado;
		public Punto(Point posicion, boolean estado) {
			this.posicion = posicion;
			this.estado = estado;
		}
	}
}
