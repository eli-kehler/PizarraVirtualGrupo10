
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.cliente.gui.PanelPizarra;
import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;


public class ServidorCliente {
	private PanelPizarra pizarra;
	private int puerto;
	
	public ServidorCliente(PanelPizarra pizarra, int puerto){
		this.pizarra = pizarra;
		this.puerto = puerto;
	}
	
	public void iniciarServidor(String nombre, ClienteRMI cliente){


		/*
		 * Crear registro de objeto remoto
		 */
		try{
			Registry registry = LocateRegistry.createRegistry(puerto);
			registry.rebind(nombre, new InterfazServidorClienteImpl(pizarra));
			
			cliente.registrarCliente(nombre, InetAddress.getLocalHost().getHostAddress(), puerto);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
