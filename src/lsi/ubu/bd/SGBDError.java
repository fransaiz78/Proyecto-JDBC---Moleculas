package lsi.ubu.bd;

/**
 * Errores en un SGBD.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jes�s Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Ra�l Marticorena</a>
 * @version 1.0
 * @since 1.0
 */
public enum SGBDError {
	FK_VIOLATED, // Violaci�n de clave for�nea
	PK_VIOLATED, // Violaci�n de clave primaria
	NOT_EXISTS_SEQUENCE, // No existe la secuencia utilizada
	// A�adir antes de esta l�nea si fuera necesario
	
	UNKNOWN; // No determinado.
}
