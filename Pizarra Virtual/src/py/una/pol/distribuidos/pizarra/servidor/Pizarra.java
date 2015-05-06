package py.una.pol.distribuidos.pizarra.servidor;

import java.awt.Dimension;
import java.awt.Point;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import py.una.pol.distribuidos.pizarra.servidor.rmi.InterfazServidorCliente;

public class Pizarra implements PizarraInterfaz
{
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
	public Pizarra()
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
	public boolean Registrar(String nombre, InetAddress direccion, int puerto)
			throws RemoteException
	{
	
		Registry registry = LocateRegistry.getRegistry(direccion.toString(), puerto);
		
		try
		{
			InterfazServidorCliente cliente = (InterfazServidorCliente)registry.lookup(nombre);
			notificador.clientes.add(cliente);
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
	 * Método concurrente; si un Cliente intenta utilizar el método y está bloqueado,
	 * esperará 5 milisegundos a que se libere, si no consigue utilizar el método en ese
	 * tiempo se rendirá y el método retornará false. Si el método se ejecutó correctamente
	 * retornará true.
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
					cliente.actualizar(listaPuntos);
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