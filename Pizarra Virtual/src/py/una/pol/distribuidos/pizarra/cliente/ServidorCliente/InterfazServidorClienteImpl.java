
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class InterfazServidorClienteImpl extends UnicastRemoteObject implements InterfazServidorCliente {

	private Pizarra pizarra;
	
	public void actualizar(Punto[] puntos)throws RemoteException{
		
		pizarra.actualizarMatriz(puntos);
	}

	public InterfazServidorClienteImpl(Pizarra pizarra) throws RemoteException {
		super();
		this.pizarra = pizarra;
	}
	
	
}
