package py.una.pol.distribuidos.pizarra.cliente.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;
import java.util.HashSet;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private PanelPizarra panelPizarra;
	private JPanel panelLateral;
	private JPanel panel;
	private HashSet<Punto> puntosActualizar = null;
	private JCheckBox chckbxBorrar;
	private JSlider slider;
	private JLabel lblPx;
	private JLabel lblPx_1;
	private JTextField textField;
	
	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(PanelPizarra pizarra, final ClienteRMI cliente) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 288);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panelLateral = new JPanel();
		contentPane.add(panelLateral, BorderLayout.EAST);
		
		chckbxBorrar = new JCheckBox("Borrar");
		
		lblPx = new JLabel("Tamaño");
		lblPx.setLabelFor(slider);
		
		textField = new JTextField();
		slider = new JSlider();
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textField.setText(String.valueOf(slider.getValue()));
			}
		});
		slider.setPaintLabels(true);
		slider.setMinimum(1);
		slider.setValue(1);
		
		lblPx_1 = new JLabel("px");
		

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					int valor = Integer.getInteger(textField.getText().trim()).intValue();
					if ( valor <= slider.getMaximum() && 
								valor >= slider.getMinimum()){
						
						slider.setValue(valor);
						
					}
				}
			}
		});
		textField.setColumns(10);
		GroupLayout gl_panelLateral = new GroupLayout(panelLateral);
		gl_panelLateral.setHorizontalGroup(
			gl_panelLateral.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelLateral.createSequentialGroup()
					.addContainerGap(169, Short.MAX_VALUE)
					.addGroup(gl_panelLateral.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelLateral.createSequentialGroup()
							.addGap(12)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPx_1))
						.addComponent(slider, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelLateral.createParallelGroup(Alignment.TRAILING)
							.addComponent(lblPx)
							.addComponent(chckbxBorrar)))
					.addContainerGap())
		);
		gl_panelLateral.setVerticalGroup(
			gl_panelLateral.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLateral.createSequentialGroup()
					.addContainerGap()
					.addComponent(chckbxBorrar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPx)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelLateral.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPx_1))
					.addContainerGap(152, Short.MAX_VALUE))
		);
		panelLateral.setLayout(gl_panelLateral);
		

		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		this.panelPizarra = pizarra;

		panelPizarra.addMouseListener(new MouseAdapter() {
			
			
			/**
			 * Al soltar el mouse, actualiza la matriz de la pizarra
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if (puntosActualizar.size() > 0){
					
					try {
						cliente.sendToServer(puntosActualizar.toArray(new Punto[puntosActualizar.size()]), panelPizarra.getPizarra().getPintor());
						panelPizarra.getPizarra().actualizarMatriz(puntosActualizar.toArray(new Punto[puntosActualizar.size()]));
					} catch (RemoteException | InterruptedException e1) {
						JOptionPane.showMessageDialog(e.getComponent().getParent(), "Error al conectar con servidor\n" + e1.getMessage(),
								"Error RMI", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();

					}
					panelPizarra.repaint();
					puntosActualizar = null;
					
				}
				
			}
			/**
			 * Al presionar el mouse, crea el array donde guardar los cambios realizados
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if (puntosActualizar == null)
					puntosActualizar = new HashSet<>();
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
				
				if (x >= 0 && x < panelPizarra.getWidth() && y >= 0 && y < panelPizarra.getHeight()){
					
					/*
					if (!chckbxBorrar.isSelected()){
						panelPizarra.pintar(new Rectangle(x,y, 1, 1));
						puntosActualizar.add(new Punto(e.getPoint(), true));
						
					} else {
						panelPizarra.borrar(new Rectangle(x,y, 1, 1));
						puntosActualizar.add(new Punto(e.getPoint(), false));
						
					}
					*/
					
					if (!chckbxBorrar.isSelected()){
						panelPizarra.pintar(new Rectangle(x, y, slider.getValue(), slider.getValue()));
						for (int i = x; i < x+slider.getValue(); i++)
							for (int j = y; j < y+slider.getValue(); j++){
								if (i < panelPizarra.getWidth() && j < panelPizarra.getHeight())
									puntosActualizar.add(new Punto(new Point(i, j), true));

							}
					} else {
						panelPizarra.borrar(new Rectangle(x,y, slider.getValue(), slider.getValue()));

						for (int i = x; i < x+slider.getValue(); i++)
							for (int j = y; j < y+slider.getValue(); j++){
								
								if (i < panelPizarra.getWidth() && j < panelPizarra.getHeight())
									puntosActualizar.add(new Punto(new Point(i, j), false));
							}
						
					}
					
					if (puntosActualizar.size() > 10) 
					{ 
						
						try {
							cliente.sendToServer(puntosActualizar.toArray(new Punto[puntosActualizar.size()]), panelPizarra.getPizarra().getPintor());
							panelPizarra.getPizarra().actualizarMatriz(puntosActualizar.toArray(new Punto[puntosActualizar.size()]));
						} catch (RemoteException | InterruptedException e1) {
							JOptionPane.showMessageDialog(e.getComponent().getParent(), "Error al conectar con servidor\n" + e1.getMessage(),
									"Error RMI", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();

						}
						panelPizarra.repaint();
						puntosActualizar.clear();
						
					}
				
				}
			}

		});
		/**
		 * Setea el tamaño del panel
		 */
		
		Dimension pizarraSize = panelPizarra.getPizarra().getDimension();
		panelPizarra.setPreferredSize(pizarraSize);
		panelPizarra.setMinimumSize(pizarraSize);
		panelPizarra.setMaximumSize(pizarraSize);
		panel.add(panelPizarra);
	}
}
