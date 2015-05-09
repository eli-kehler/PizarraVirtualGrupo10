package py.una.pol.distribuidos.pizarra.servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PizarraServer {

	
	
	public PizarraServer(){}
	
	public void iniciarServidor(String ip){
		
		/* Aca va la ip del servidor */
		System.setProperty("java.rmi.server.hostname", ip);
		
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("miPizarra", new Pizarra());
			System.out.println("Servidor de pizarra iniciado correctamente.");
			System.out.println("IP:" + ip);
			System.out.println("Puerto: 1099" );
		} catch (Exception e) {
			System.err.println("Error al iniciar servidor");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		String ip = null;
		if (args.length == 1)
			ip = args[0];
		else if (args.length > 1){
			System.out.println("Solo puede recibir un argumento. El ip que usara el servidor.");
			System.exit(1);
		} else
			ip = "127.0.0.1";
		new PizarraServer().iniciarServidor(ip);
		
	}
	
}
