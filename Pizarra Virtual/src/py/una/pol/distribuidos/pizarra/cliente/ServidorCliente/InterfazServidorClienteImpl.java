
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class InterfazServidorClienteImpl implements InterfazServidorCliente {

	private Pizarra pizarra;
	
	public void actualizar(Punto[] puntos){
		
		
		for(Punto p:puntos){
			
			pizarra.getMatriz()[p.posicion.x][p.posicion.y]=p.estado;
		
		}
	}
}
