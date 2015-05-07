
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.cliente.gui.PanelPizarra;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class InterfazServidorClienteImpl extends UnicastRemoteObject implements InterfazServidorCliente {

	private PanelPizarra pizarra;
	
	public void actualizar(Punto[] puntos)throws RemoteException{
		
		pizarra.getPizarra().actualizarMatriz(puntos);
		pizarra.repaint();
		
	}

	public InterfazServidorClienteImpl(PanelPizarra pizarra) throws RemoteException {
		super();
		this.pizarra = pizarra;
	}
	
	
}
