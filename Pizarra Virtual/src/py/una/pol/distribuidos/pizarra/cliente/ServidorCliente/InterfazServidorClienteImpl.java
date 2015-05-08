
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import py.una.pol.distribuidos.pizarra.cliente.gui.PanelPizarra;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class InterfazServidorClienteImpl extends UnicastRemoteObject implements InterfazServidorCliente {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1144725160514201643L;
	private PanelPizarra pizarra;
	
	public void actualizar(Punto[] puntos)throws RemoteException{
		
		pizarra.getPizarra().actualizarMatriz(puntos);
		
		/*
		for (Punto p : puntos){
			if (p.estado)
				pizarra.pintar(new Rectangle(p.posicion, new Dimension(1, 1)));
			else
				pizarra.borrar(new Rectangle(p.posicion, new Dimension(1, 1)));
		}
		*/
		pizarra.repaint();
	}

	public InterfazServidorClienteImpl(PanelPizarra pizarra) throws RemoteException {
		super();
		this.pizarra = pizarra;
	}
	
	
}
