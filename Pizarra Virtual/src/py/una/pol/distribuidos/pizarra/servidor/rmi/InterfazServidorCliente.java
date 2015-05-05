
package py.una.pol.distribuidos.pizarra.servidor.rmi;

import java.rmi.Remote;

import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public interface InterfazServidorCliente extends Remote {
	public void actualizar(Punto[] puntos);
	
}
