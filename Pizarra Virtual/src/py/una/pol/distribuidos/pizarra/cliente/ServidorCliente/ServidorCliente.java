package py.una.pol.distribuidos.pizarra.cliente.ServidosCliente

import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServidorCliente {
	private ClienteRMI cliente;
	private int puerto = cliente.getPuerto;
	
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
