package py.una.pol.distribuidos.pizarra.servidor;

import java.awt.Dimension;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pizarra implements PizarraInterfaz
{
	// Constantes de ANCHURA y ALTURA de la Pizarra
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 700;
	
	// Lista de Objetos a Clientes remotos (en caso de utilizarlos para notificarlos de eventos)
	private ArrayList<Object> clientes = null;
	
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
			Object cliente = registry.lookup(nombre);
			clientes.add(cliente);
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
		boolean r[][] = pizarra;
	
		return r;
	}

	/* Permite a un Cliente enviar una serie de puntos que fueron modificados
	 *  
	 * M�todo concurrente; si un Cliente intenta utilizar el m�todo y est� bloqueado,
	 * esperar� 5 milisegundos a que se libere, si no consigue utilizar el m�todo en ese
	 * tiempo se rendir� y el m�todo retornar� false. Si el m�todo se ejecut� correctamente
	 * retornar� true.
	 */
	@Override
	public boolean actualizar(Punto[] puntos) throws RemoteException
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
	
	
	
}
