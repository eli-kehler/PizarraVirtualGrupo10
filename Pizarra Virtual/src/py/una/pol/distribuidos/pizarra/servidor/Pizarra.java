package py.una.pol.distribuidos.pizarra.servidor;

import java.awt.Dimension;
import java.awt.Point;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import py.una.pol.distribuidos.pizarra.cliente.ServidorCliente.InterfazServidorCliente;
public class Pizarra extends UnicastRemoteObject implements PizarraInterfaz
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6999988334775339800L;
	// Constantes de ANCHURA y ALTURA de la Pizarra
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 700;
	
	// Objeto que se encarga de realizar las notificaciones
	private Notificador notificador = null;
	
	// Variables privadas
	private boolean[][] pizarra = null;
	private int width;
	private int height;
	
	
	// Semaforo utilizado para controlar la concurrencia
	private Lock semaforo;
	
	/* Constructor por defecto */
	public Pizarra() throws RemoteException
	{
		super();
		
		width = WIDTH;
		height = HEIGHT;
		pizarra = new boolean[height][width];
		semaforo = new ReentrantLock();
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
	
		Registry registry = LocateRegistry.getRegistry(direccion, puerto);
		
		try
		{
			InterfazServidorCliente cliente = (InterfazServidorCliente)registry.lookup(nombre);
			notificador.clientes.add(cliente);
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

	/* Permite a un Cliente enviar una serie de puntos que fueron modificados
	 *  
	 * M�todo concurrente; si un Cliente intenta utilizar el m�todo y est� bloqueado,
	 * esperar� 5 milisegundos a que se libere, si no consigue utilizar el m�todo en ese
	 * tiempo se rendir� y el m�todo retornar� false. Si el m�todo se ejecut� correctamente
	 * retornar� true.
	 */
	@Override
	public synchronized boolean actualizar(Punto[] puntos) throws RemoteException
	{
		boolean liberar = false;
		try
		{
			if (liberar = semaforo.tryLock(5, TimeUnit.MILLISECONDS))
			{
				boolean[][] pizarra_temp = pizarra;
				for (Punto punto : puntos)
				{
					pizarra_temp[punto.posicion.y][punto.posicion.x] = punto.estado;
					notificador.puntos.put(punto.posicion, punto.estado);
				}
				
				pizarra = pizarra_temp;
				return true;
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		} finally
		{
			if (liberar) semaforo.unlock();
			
		}
		
		return false;
	}
	
	private class Notificador implements Runnable {
		
		// Lista de Objetos a Clientes remotos (en caso de utilizarlos para notificarlos de eventos)
		public ArrayList<InterfazServidorCliente> clientes = null;
		public TreeMap<Point, Boolean> puntos = null;
		
		public Notificador()
		{
			super();
			
			clientes = new ArrayList<>();
			puntos = new TreeMap<>();
		}
		
		@Override
		public void run()
		{
			
			// Si hay cambios que realizar
			if (!puntos.isEmpty())
			{
				Punto listaPuntos [] = new Punto[puntos.size()];
				int k = 0;
				
				// Genera la lista de puntos a enviar
				for (Map.Entry<Point, Boolean> entry : puntos.entrySet())
				{
					listaPuntos[k++] = new Punto(entry.getKey(), entry.getValue());				
				}
				
				// Actualiza los clientes
				for (InterfazServidorCliente cliente : clientes)
				{
					try {
						cliente.actualizar(listaPuntos);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			

				puntos.clear();
			}
			
			try
			{
				Thread.sleep(10);  // Actualiza cada 10 milisegundos
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
}