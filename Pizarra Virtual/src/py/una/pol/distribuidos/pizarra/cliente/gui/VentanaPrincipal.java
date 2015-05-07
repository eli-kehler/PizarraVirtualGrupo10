package py.una.pol.distribuidos.pizarra.cliente.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private PanelPizarra panelPizarra;
	private JPanel panelLateral;
	private JPanel panel;
	private final ButtonGroup botonesHerramientas = new ButtonGroup();
	private ArrayList<Punto> puntosActualizar = null;
	private JCheckBox chckbxBorrar;
	private ClienteRMI cliente;


	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(Pizarra pizarra, final ClienteRMI cliente) {
		
		this.cliente = cliente;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 288);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panelLateral = new JPanel();
		contentPane.add(panelLateral, BorderLayout.EAST);
		panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
		
		chckbxBorrar = new JCheckBox("Borrar");
		panelLateral.add(chckbxBorrar);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		//TODO obtener dimensiones
		int pizarraWidth, pizarraHeight;
		pizarraWidth = pizarra.getDimension().width;
		pizarraHeight = pizarra.getDimension().height;
		panelPizarra = new PanelPizarra(pizarra);

		panelPizarra.addMouseListener(new MouseAdapter() {
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 * Al soltar el mouse, actualiza la matriz de la pizarra
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if (puntosActualizar.size() > 0){
					
					try {
						cliente.sendToServer(puntosActualizar.toArray(new Punto[puntosActualizar.size()]));
						panelPizarra.getPizarra().actualizarMatriz(puntosActualizar.toArray(new Punto[puntosActualizar.size()]));
					} catch (RemoteException e1) {
						JOptionPane.showMessageDialog(e.getComponent().getParent(), "Error al conectar con servidor\n" + e1.getMessage(),
								"Error RMI", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();

					}
					panelPizarra.repaint();
					puntosActualizar = null;
					
				}
				
			}
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 * Al presionar el mouse, crea el array donde guardar los cambios realizados
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if (puntosActualizar == null)
					puntosActualizar = new ArrayList<>();
			}
		});
		panelPizarra.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				/**
				 * Al hacer un "drag" del mouse, se pinta o se borra
				 */
				int x = e.getX();
				int y = e.getY();
				
				if (x >= 0 && x < panel.getWidth() && y >= 0 && y < panel.getHeight()){
					
					if (!chckbxBorrar.isSelected()){
						panelPizarra.pintar(new Rectangle(x, y, 1, 1));
						puntosActualizar.add(new Punto(e.getPoint(), true));
					} else {
						panelPizarra.borrar(new Rectangle(x,y, 1, 1));
						puntosActualizar.add(new Punto(e.getPoint(), false));
					}
					
				
				}
			}

		});
		panelPizarra.setPreferredSize(new Dimension(pizarraWidth, pizarraHeight));
		panelPizarra.setMinimumSize(new Dimension(pizarraWidth, pizarraHeight));
		panelPizarra.setMaximumSize(new Dimension(pizarraWidth, pizarraHeight));
		panel.add(panelPizarra);
	}
	
	
	public static void main(String[] args){
		/*
		 * Iniciar la GUI
		 */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				VentanaPrincipal frame = new VentanaPrincipal(null, null);
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
