package py.una.pol.distribuidos.pizarra.servidor;

import java.awt.Dimension;
import java.awt.Point;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import py.una.pol.distribuidos.pizarra.cliente.ServidorCliente.InterfazServidorCliente;
public class Pizarra extends UnicastRemoteObject implements PizarraInterfaz
{
	private static final long serialVersionUID = -6999988334775339800L;
	
	// Constantes de ANCHURA y ALTURA de la Pizarra
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 700;
	
	// Tiempo que pasa para que el Servidor envie la matriz completa
	public static final int DELAY = 100;
	
	// Objeto que se encarga de realizar las notificaciones
	private Notificador notificador = null;
	
	// Variables privadas
	private boolean[][] pizarra = null;
	private int width;
	private int height;
	private int delay;
	
	
	/* Constructor por defecto */
	public Pizarra() throws RemoteException
	{
		super();
		
		width = WIDTH;
		height = HEIGHT;
		pizarra = new boolean[height][width];
		delay = 0;
		notificador = new Notificador();
		
		new Thread(notificador).start();
	}
	
	/* Permite a un cliente registrarse, dandole informacion
	 * al Servidor del nombre de su objeto remoto, direccion y puerto
	 *  Retorna verdadero si el servidor pudo establecer la conexion, falso
	 * en caso contrario */
	@Override
	public boolean Registrar(String nombre, String direccion, int puerto)
			throws RemoteException
	{
		
		if (notificador.clientNames.contains(nombre)) return false;
		
		Registry registry = LocateRegistry.getRegistry(direccion, puerto);
		
		try
		{
			InterfazServidorCliente cliente = (InterfazServidorCliente)registry.lookup(nombre);
			notificador.clientes.add(cliente);
			notificador.clientNames.add(nombre);
			System.out.println(nombre + " REGISTRADO");
		} catch (NotBoundException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	

	/* Permite a un cliente obtener las dimensiones actuales
	 * de la Pizarra */
	@Override
	public Dimension obtenerDimensiones() throws RemoteException
	{
		return new Dimension(width, height);
	}

	/* Permite Obtener la matriz, o estado, actual de la Pizarra */
	@Override
	public boolean[][] obtenerMatriz() throws RemoteException
	{
		return pizarra;
	}

	/* 
	 * Permite a un Cliente enviar una serie de puntos que fueron modificados
	 */
	@Override
	public synchronized boolean actualizar(Punto[] puntos) throws RemoteException
	{
	
				boolean[][] pizarra_temp = pizarra;
				for (Punto punto : puntos)
				{
					pizarra_temp[punto.posicion.y][punto.posicion.x] = punto.estado;
					notificador.puntos.add(punto);
				}
				
				pizarra = pizarra_temp;
				return true;
		
	}
	
	private class Notificador implements Runnable {
		
		// Lista de Objetos a Clientes remotos (en caso de utilizarlos para notificarlos de eventos)
		public ArrayList<InterfazServidorCliente> clientes = null;
		public ArrayList<String> clientNames = null;
		public ArrayList<Punto> puntos = null;
		
		public Notificador()
		{
			super();
			
			clientes = new ArrayList<>();
			clientNames = new ArrayList<>();
			puntos = new ArrayList<>();
		}
		
		@Override
		public void run()
		{
			
			while (true)
			{
				//boolean refresh = (delay > DELAY);
				
	
				
				
				// Si hay cambios que realizar
				if (!puntos.isEmpty() /*|| refresh*/)
				{
					
					Punto listaPuntos[];
					
					//if (!refresh)
					//{
						listaPuntos = new Punto[puntos.size()];
						
						ArrayList<Punto> puntos_temp = puntos;
						// Genera la lista de puntos a enviar
						listaPuntos = puntos_temp.toArray(new Punto[puntos_temp.size()]);
					//}
					
					
					//TODO
					/*
					else
					{
						listaPuntos = new Punto[width * height];
						boolean[][] pizarra_temp = pizarra;
						
						int k = 0;
						
						for (int y = 0; y < height; y++)
							for (int x = 0; x < width; x++)
							{
								listaPuntos[k++] = new Punto(new Point(x, y), pizarra_temp[y][x]);
							}
					}
					*/
					
					
					// Actualiza los clientes
					//for (InterfazServidorCliente cliente : clientes)
					// {
					for (int z = 0; z < clientes.size(); z++)
					{
						InterfazServidorCliente cliente = clientes.get(z);
						try {
							cliente.actualizar(listaPuntos);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							int i = clientes.indexOf(cliente);
							System.out.println("BORRANDO " + clientNames.get(i));
							clientes.remove(i);
							clientNames.remove(i);
							
						}
					}
				
	
					puntos.clear();
				}
				
				try
				{
					Thread.sleep(10);  // Actualiza cada 10 milisegundos
					delay += 10;
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
		
			}
		}
		
	}
	
}