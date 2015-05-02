package py.una.pol.distribuidos.pizarra.cliente.ServidosCliente

public class InterfazServidorClienteImpl {

	private ClienteRMI cliente;
	private boolean[][] matriz = cliente.getMatriz();
	
	public void actualizar(Punto[] puntos){
		for(Punto p:puntos){
			matriz[p.posicion.x][p.posicion.y]=p.estado;
		}
	}
}
