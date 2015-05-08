package py.una.pol.distribuidos.pizarra.servidor;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PizarraServer {

	
	
	public PizarraServer(){}
	
	public void iniciarServidor(){
		/* Aca va la ip del servidor */
		System.setProperty("java.rmi.server.hostname", "10.15.15.1");
		
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("miPizarra", new Pizarra());
			System.out.println("Servidor de pizarra iniciado correctamente.");
			System.out.println("IP:" + InetAddress.getLocalHost().getHostAddress());
			System.out.println("Puerto: 1099" );
		} catch (Exception e) {
			System.err.println("Error al iniciar servidor");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		new PizarraServer().iniciarServidor();
		
	}
	
}
