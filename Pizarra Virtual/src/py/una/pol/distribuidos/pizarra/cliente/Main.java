package py.una.pol.distribuidos.pizarra.cliente;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import py.una.pol.distribuidos.pizarra.cliente.ServidorCliente.ServidorCliente;
import py.una.pol.distribuidos.pizarra.cliente.gui.PanelPizarra;
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
			
			String ip = JOptionPane.showInputDialog(new JFrame(), "Ingrese el IP del servidor");
			
			final ClienteRMI cliente = new ClienteRMI(ip, 1099);
			
			/**
			 * Encontrar puerto libre
			 */
			
			ServerSocket s = new ServerSocket(0);
			final int puerto = s.getLocalPort();
			s.close();
			

			String t_pintor;
			do
			{
				t_pintor = JOptionPane.showInputDialog("Ingrese el nombre del pintor.", "");
			} while(!cliente.estaDiponible(t_pintor));
				
			final String pintor = t_pintor;
			
			final PanelPizarra p = new PanelPizarra(new Pizarra(cliente.getMatriz(), pintor));
			
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
			
			p.getPizarra().setMatriz(cliente.getMatriz());
			p.getPizarra().setPintor(pintor);
			

			/*
			 * Iniciar la GUI
			 */
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					VentanaPrincipal frame = new VentanaPrincipal(p, cliente);
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
