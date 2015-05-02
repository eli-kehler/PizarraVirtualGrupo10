package py.una.pol.distribuidos.pizarra.cliente.ServidorCliente

import java.rmi.Remote;


public interface InterfazServidorCliente extends Remote{
	public void actualizar(Punto[] puntos);
}
