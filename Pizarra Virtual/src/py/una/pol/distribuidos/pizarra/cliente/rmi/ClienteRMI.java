package py.una.pol.distribuidos.pizarra.cliente.rmi;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.Dimension;
import java.awt.Point;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
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
	
	public boolean registrarCliente(String nombre, InetAddress address, int port) throws RemoteException, NotBoundException{

		return impl.Registrar(nombre, address, port);
		
	}
	
	public boolean[][] getMatriz() throws RemoteException{

		return impl.obtenerMatriz();
	}
	
	public boolean sendToServer(Punto[] puntos) throws RemoteException{

			return impl.actualizar(puntos);
	}
}