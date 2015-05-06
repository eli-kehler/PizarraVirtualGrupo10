package py.una.pol.distribuidos.pizarra.cliente;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import py.una.pol.distribuidos.pizarra.cliente.ServidorCliente.ServidorCliente;
import py.una.pol.distribuidos.pizarra.cliente.gui.VentanaPrincipal;
import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;

public class Main {

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		
		/*
		 * Conexion con el servidor
		 */
		
		try {
			ClienteRMI cliente = new ClienteRMI("127.0.1.1", 1099);
			Dimension dimensiones = cliente.obtenerDimensiones();
			
			final int puerto = 44444;
			/*
			 * Encontrar puerto abierto
			 */
			
			/*
			Socket s = new Socket();
			s.connect(new InetSocketAddress("127.0.0.1", 1099), 5);
			int puerto = s.getLocalPort();
			s.close();
			*/
			Pizarra p = new Pizarra(" ");
			
			/*
			 * Comentario: Esto debe ir antes de llamar a registrarCliente(), pues
			 * si el objeto remoto no existe el metodo siempre devolver� false.
			 */
			


			String pintor = JOptionPane.showInputDialog("Ingrese el nombre del pintor.", "");
		
			
			/**
			 * Iniciar el servidor para recibir actualizaciones
			 */
			new Thread(new Runnable(){
				public void run(){
					
					new ServidorCliente(p,puerto).iniciarServidor(pintor, cliente);
					
				}
			}).start();
			/*
			while (!cliente.registrarCliente(pintor, InetAddress.getLocalHost().getHostAddress(), 4746)){

				pintor = JOptionPane.showInputDialog("Nombre utilizado. Ingrese el nombre del pintor.", "");
			}
			*/
			
			p.setMatriz(cliente.getMatriz());
			p.setPintor(pintor);
			

			/*
			 * Iniciar la GUI
			 */
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					VentanaPrincipal frame = new VentanaPrincipal(p);
					try{
						for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
							if("Nimbus".equals(info.getName())){
								UIManager.setLookAndFeel(info.getClassName());
								break;
							}
					} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException
					| UnsupportedLookAndFeelException e2) {
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (ClassNotFoundException | InstantiationException
								| IllegalAccessException
								| UnsupportedLookAndFeelException e1) {
							try {
								UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
							} catch (ClassNotFoundException | InstantiationException
									| IllegalAccessException
									| UnsupportedLookAndFeelException e) {
								JOptionPane.showMessageDialog(frame, "Error configurando LookAndFeel", "Error 01", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
						}
					}
					
					try {
						
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			
		} catch (RemoteException | NotBoundException e3) {
			JOptionPane.showMessageDialog(new JFrame(), "Error de RMI.\nMensaje:\n" + e3.getMessage());
			e3.printStackTrace();
			System.exit(1);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "No existe puertos disponibles.\nMensaje:\n" + e1.getMessage());
			e1.printStackTrace();
			System.exit(1);
		}

		
	}

}
