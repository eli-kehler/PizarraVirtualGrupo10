
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;


public class ServidorCliente {
	private Pizarra pizarra;
	private int puerto;
	
	public ServidorCliente(){
		iniciarServidor(pizarra.getPintor());
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
