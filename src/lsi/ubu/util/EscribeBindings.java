package lsi.ubu.util;

import lsi.ubu.PoolDeConexiones;

/**
 * Clase de utilidad para reconfigurar el pool de conexiones a la bases de datos.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jes�s Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Ra�l Marticorena</a>
 */
public class EscribeBindings {

	/**
	 * Principal. 
	 * 
	 * @param args argumentos (se ignoran)
	 */
	public static void main(String[] args) {
		try{
			PoolDeConexiones.reconfigurarPool();
			System.out.println("Pool reconfigurado con �xito.");
		}catch (Exception e){
			System.err.println("Error reconfigurando el pool de conexiones.");
			System.err.println(e.getMessage());
		}
	}

}
