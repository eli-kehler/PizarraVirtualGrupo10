package py.una.pol.distribuidos.pizarra.servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PizarraServer {

	
	
	public PizarraServer(){}
	
	public void iniciarServidor(){
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("miPizarra", new Pizarra());
			System.out.println("Servidor de pizarra iniciado correctamente.");
		} catch (Exception e) {
			System.err.println("Error al iniciar servidor");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		new PizarraServer().iniciarServidor();
		
	}
	
}
