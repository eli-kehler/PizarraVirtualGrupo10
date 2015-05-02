
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;


public class ServidorCliente {
	private ClienteRMI cliente;
	private int puerto;
	
	public ServidorCliente(){
		iniciarServidor(cliente.getNombre());
	}
	
	public void iniciarServidor(String nombre){
		
		try{
			Registry registry = LocateRegistry.createRegistry(puerto);
			registry.rebind(nombre, new InterfazServidorClienteImpl());
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
