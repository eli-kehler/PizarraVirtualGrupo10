package py.una.pol.distribuidos.pizarra.cliente.rmi;

import java.awt.Dimension;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;



public class ClienteRMI {
	private PizarraInterfaz impl;
	
	
	public ClienteRMI(String host, int port) throws AccessException, RemoteException, NotBoundException{
		
		Registry bind = LocateRegistry.getRegistry(host, port);
		//TODO nombre del objeto remoto
		impl = (PizarraInterfaz) bind.lookup("miPizarra");
		
	}
	
	public Dimension obtenerDimensiones() throws RemoteException{
		
		return impl.obtenerDimensiones();
		
	}
	
	public boolean estaDiponible(String nombre) throws RemoteException
	{
		return impl.EstaDisponible(nombre);
	}
	
	public boolean registrarCliente(String nombre, String address, int port) throws RemoteException, NotBoundException{

		return impl.Registrar(nombre, address, port);
		
	}
	
	public boolean[][] getMatriz() throws RemoteException{

		return impl.obtenerMatriz();
	}
	
	public boolean sendToServer(Punto[] puntos, String pintor) throws RemoteException, InterruptedException{

			return impl.actualizar(puntos, pintor);
	}
}