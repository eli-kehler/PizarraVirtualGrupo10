package py.una.pol.distribuidos.pizarra.cliente;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import py.una.pol.distribuidos.pizarra.cliente.gui.VentanaPrincipal;

public class Main {

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				VentanaPrincipal frame = new VentanaPrincipal();
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
	}

}
