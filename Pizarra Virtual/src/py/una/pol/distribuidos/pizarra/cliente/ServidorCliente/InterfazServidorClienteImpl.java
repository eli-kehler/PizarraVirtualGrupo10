
package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente;

import py.una.pol.distribuidos.pizarra.cliente.Pizarra;
import py.una.pol.distribuidos.pizarra.servidor.PizarraInterfaz.Punto;

public class InterfazServidorClienteImpl implements InterfazServidorCliente {

	private Pizarra pizarra;
	private boolean[][] matriz = pizarra.getMatriz();
	
	public void actualizar(Punto[] puntos){
		
		for(Punto p:puntos){
			
			matriz[p.posicion.x][p.posicion.y]=p.estado;
		
		}
	}
}
