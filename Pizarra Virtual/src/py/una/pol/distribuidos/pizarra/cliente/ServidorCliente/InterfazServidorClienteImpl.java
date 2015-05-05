
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class InterfazServidorClienteImpl implements InterfazServidorCliente {

	private Pizarra pizarra;
	
	public void actualizar(Punto[] puntos){
		
		pizarra.actualizarMatriz(puntos);
	}

	public InterfazServidorClienteImpl(Pizarra pizarra) {
		super();
		this.pizarra = pizarra;
	}
	
	
}
