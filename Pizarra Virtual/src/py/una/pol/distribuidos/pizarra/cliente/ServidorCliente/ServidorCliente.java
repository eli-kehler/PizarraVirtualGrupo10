
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;


public class ServidorCliente {
	private Pizarra pizarra;
	private int puerto;
	
	public ServidorCliente(Pizarra pizarra, int puerto){
		this.pizarra = pizarra;
		this.puerto = puerto;
		iniciarServidor(pizarra.getPintor());
	}
	
	public void iniciarServidor(String nombre){


		/*
		 * Crear registro de objeto remoto
		 */
		try{
			Registry registry = LocateRegistry.createRegistry(puerto);
			registry.rebind(nombre, new InterfazServidorClienteImpl(pizarra));
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
