package py.una.pol.distribuidos.pizarra.cliente.rmi;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.Dimension;
import java.awt.Point;



public class ClienteRMI {
	private Registry bind;
	private Dimension dimensiones;
	private boolean[][] matriz;
	PizarraInterfaz impl;
	String nombre;
	
	public ClienteRMI(String nombre){
		this.nombre = nombre;
		registrarCliente(nombre);
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public void obtenerDimensiones() throws RemoteException{
		
		dimensiones = impl.obtenerDimensiones();
		return dimensiones;
	}
	
	public void registrarCliente(String nombre) throws RemoteException{
		Registry bind = LocateRegistry.getRegistry("127.0.0.1",1099);
		impl = (PizarraInterfaz) bind.lookup(nombre);
	}
	
	public boolean[][] getMatriz() throws RemoteException{

		matriz = impl.obtenerMatriz();
		return matriz;
	}
	
	public void sendToServer(Punto[] puntos) throws RemoteException{

			impl.actualizar(puntos);
	}
}