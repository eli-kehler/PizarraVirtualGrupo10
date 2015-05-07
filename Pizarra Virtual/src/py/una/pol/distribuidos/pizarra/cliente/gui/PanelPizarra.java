package py.una.pol.distribuidos.pizarra.cliente.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JPanel;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;


public class PanelPizarra extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6566542631762682094L;
	
	
	private Pizarra pizarra;
	private ClienteRMI cliente;
	
	
	/**
	 * Create the panel.
	 */
	public PanelPizarra(Pizarra p) {
		pizarra = p;
		this.setSize(pizarra.getDimension());
		
		
	}
	/**
	 * Metodo llamado cada vez que se pinta el panel
	 */
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;

		// Dibuja la pizarra al comienzo
		g2d.setPaint(Color.WHITE);
		g2d.fill(new Rectangle(pizarra.getDimension()));

		
		// Pintar los puntos negros
		g2d.setPaint(Color.BLACK);
		boolean[][] puntos = pizarra.getMatriz();
		for (int y=0; y<puntos.length; y++)
			for(int x=0; x<puntos[y].length; x++)
				if (puntos[y][x])
					g2d.fillRect(x, y, 1, 1);
				
		
	}
	
	public void pintar(Shape forma) {
		
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setPaint(Color.BLACK);
		g2d.fill(forma);
		
	}
	
	public void borrar( Shape forma){
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setPaint(Color.WHITE);
		g2d.fill(forma);
		
	}
		
	public Pizarra getPizarra() {
		return pizarra;
	}
	public ClienteRMI getCliente() {
		return cliente;
	}
	
	

}
