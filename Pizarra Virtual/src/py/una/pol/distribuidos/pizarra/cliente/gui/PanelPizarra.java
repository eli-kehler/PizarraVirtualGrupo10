package py.una.pol.distribuidos.pizarra.cliente.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JPanel;

import py.una.pol.distribuidos.pizarra.cliente.rmi.ClienteRMI;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

import com.sun.javafx.geom.Ellipse2D;

public class PanelPizarra extends JPanel {

	private Rectangle pizarra;
	private ClienteRMI cliente;
	private ArrayList<Point> actualizar = null;
	private Shape temporal;
	private boolean borrar;
	private boolean[][] puntos;
	
	/**
	 * Create the panel.
	 */
	public PanelPizarra(int w, int h) {
		
		this.setSize(w, h);
		pizarra = new Rectangle(0,0,w, h);
		puntos = new boolean[w][h];
		
	}
	/**
	 * Metodo llamado cada vez que se pinta el panel
	 */
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;

		// Dibuja la pizarra al comienzo
		g2d.setPaint(Color.WHITE);
		g2d.fill(pizarra);

		
		// Inicializar los puntos negros
		g2d.setPaint(Color.BLACK);
		for (int x=0; x<puntos.length; x++)
			for(int y=0; y<puntos[x].length; y++)
				if (puntos[x][y])
					g2d.fillRect(x, y, 1, 1);
			
	
		/*
		if (temporal != null){
			if(borrar)
				g2d.setPaint(Color.WHITE);
			else
				g2d.setPaint(Color.BLACK);
			
			g2d.fill(temporal);
		}
		*/
		
	}
	
	public void pintar(Shape forma) {
		
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setPaint(Color.BLACK);
		g2d.fill(forma);
		
	}
	
	
	/*
	public void agregarPuntos(ArrayList<Point> puntosAgregados){
		
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		for(Point punto : puntosAgregados){
			puntos[punto.x][punto.y] = !puntos[punto.x][punto.y];
			if (puntos[punto.x][punto.y])
				g2d.setPaint(Color.BLACK);
			else
				g2d.setPaint(Color.WHITE);
			
			g2d.fillRect(punto.x, punto.y, 1, 1);
		}
		
	}
	*/

	public void actualizarPuntos(ArrayList<Punto> puntosActualizar){
		
		for (Punto p : puntosActualizar)
			puntos[p.posicion.x][p.posicion.y] = p.estado;
			
		//TODO: Actualizar servidor
		
	}
	
	
	/**
	 * Agrega un shape a dibujar(o borrar) cada vez que se pinta el panel
	 * @param forma
	 */
	public void setTemporal(Shape forma, boolean borra){
		
		temporal = forma;
		borrar = borra;
		
	}
	
}
