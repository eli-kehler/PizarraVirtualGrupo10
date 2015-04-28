package py.una.pol.distribuidos.pizarra.cliente.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class PanelPizarra extends JPanel {

	Rectangle pizarra;
	
	/**
	 * Create the panel.
	 */
	public PanelPizarra(int w, int h) {
		
		this.setSize(w, h);
		pizarra = new Rectangle(0,0,this.getWidth(), this.getHeight());
		
	}
	
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setPaint(Color.WHITE);
		g2d.fill(pizarra);
		
		
	}

}
