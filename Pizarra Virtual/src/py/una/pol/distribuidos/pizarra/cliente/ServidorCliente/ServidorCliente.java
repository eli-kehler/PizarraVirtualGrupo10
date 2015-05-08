
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		
			Registry registry;
			try {

				
				/*Enumeration<?> interfaces = NetworkInterface.getNetworkInterfaces();
				boolean registrado = false;
				while (interfaces.hasMoreElements() && !registrado){
					NetworkInterface interfaz = (NetworkInterface) interfaces.nextElement();
					Enumeration<?> direcciones = interfaz.getInetAddresses();
					while (direcciones.hasMoreElements() && !registrado) {
						
						try {
							
							String direccion = ((InetAddress)direcciones.nextElement()).getHostAddress();
							System.setProperty("java.rmi.server.hostname", direccion );
							registry = LocateRegistry.createRegistry(puerto);
							
							registry.rebind(nombre, new InterfazServidorClienteImpl(pizarra));
							cliente.registrarCliente(nombre,direccion , puerto);
						} catch (NotBoundException e) {
							continue;
						}
					}
				}
				*/
				
				/* Aca va la ip del cliente */
				String direccion = "127.0.0.1";
				System.setProperty("java.rmi.server.hostname", direccion );
				registry = LocateRegistry.createRegistry(puerto);
				
				registry.rebind(nombre, new InterfazServidorClienteImpl(pizarra));
				cliente.registrarCliente(nombre,direccion , puerto);

			} catch (RemoteException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(new JFrame(), "Error de RMI.\n" + e.getMessage());
				System.exit(1);
			} catch (NotBoundException e) {
				e.printStackTrace();
				
			}
			
			
			

	}
}
