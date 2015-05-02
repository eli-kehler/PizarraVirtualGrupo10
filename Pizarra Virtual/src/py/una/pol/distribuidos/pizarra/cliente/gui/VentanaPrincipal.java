package py.una.pol.distribuidos.pizarra.cliente.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.cliente.gui.PanelPizarra;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.JToggleButton;
import javax.swing.ButtonGroup;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private PanelPizarra panelPizarra;
	private JPanel panelLateral;
	private JPanel panel;
	private JToggleButton btnLapiz;
	private JToggleButton btnLinea;
	private JToggleButton btnRect;
	private final ButtonGroup botonesHerramientas = new ButtonGroup();
	private ArrayList<Punto> puntosActualizar = null;
	private JLabel lblDebug;



	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 288);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panelLateral = new JPanel();
		contentPane.add(panelLateral, BorderLayout.EAST);
		panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
		
		btnLapiz = new JToggleButton("Lapiz");
		botonesHerramientas.add(btnLapiz);
		panelLateral.add(btnLapiz);
		
		btnLinea = new JToggleButton("Linea");
		botonesHerramientas.add(btnLinea);
		panelLateral.add(btnLinea);
		
		btnRect = new JToggleButton("Rectangulo");
		botonesHerramientas.add(btnRect);
		panelLateral.add(btnRect);
		
		lblDebug = new JLabel("debug");
		panelLateral.add(lblDebug);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		//TODO obtener dimensiones
		int pizarraWidth, pizarraHeight;
		pizarraWidth = 640;
		pizarraHeight = 480;
		panelPizarra = new PanelPizarra(new Pizarra(new boolean[pizarraWidth][pizarraHeight], "Pintor"));

		panelPizarra.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (btnLapiz.isSelected()){
					panelPizarra.getPizarra().actualizarMatriz(puntosActualizar);
					puntosActualizar = null;
					
				}
				
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (puntosActualizar == null)
					puntosActualizar = new ArrayList<>();
			}
		});
		panelPizarra.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				if (btnLapiz.isSelected()){
					if (puntosActualizar == null)	
						puntosActualizar = new ArrayList<Punto>();
					panelPizarra.pintar(new Rectangle(e.getX(), e.getY(), 1, 1));
					puntosActualizar.add(new Punto(e.getPoint(), true));
					
					
				}
			}

		});
		panelPizarra.setPreferredSize(new Dimension(pizarraWidth, pizarraHeight));
		panelPizarra.setMinimumSize(new Dimension(pizarraWidth, pizarraHeight));
		panelPizarra.setMaximumSize(new Dimension(pizarraWidth, pizarraHeight));
		panel.add(panelPizarra);
	}
}
