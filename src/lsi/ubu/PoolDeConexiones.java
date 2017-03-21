package lsi.ubu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.pool.OraclePooledConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pool de conexiones.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jes�s Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Ra�l Marticorena</a>
 * @version 1.0
 * @since 1.0 
 */
public class PoolDeConexiones {

	// Configuraci�n de JNDI
	private static final String FILE_SYSTEM_CONTEXT_FACTORY = "com.sun.jndi.fscontext.RefFSContextFactory";
	private static final String FILE_RES = "file:./res";
	private static final String JDBC_TESTDB_DS = "jdbc/testdb_ds";

	// Constantes de conexi�n
	private static final String DRIVER_TYPE = "thin";
	private static final int PORT = 1521;
	private static final String SID = "xe";
	private static final String HOST = "localhost";
	private static final String USER = "hr";
	// Tip: si hay problemas de conexi�n, comprueba que en la BD el password est� en min�sculas,
	// prueba desde SQL*Plus o SQLDeveloper que efectivamente puedes conectarte con hr/hr.
	private static final String PASSWORD = "hr"; 

	/** Pool de conexiones. */
	private static PoolDeConexiones poolDeConexiones;

	/** DataSource. */
	private DataSource ds;

	/** Logger. */
	private static Logger logger;

	/** Inicializa el logger. */
	private void setLogger() throws IOException {
		if (logger == null) {
			logger = LoggerFactory.getLogger(PoolDeConexiones.class);
			logger.info("Comienzo Ejecuci�n");
		}
		return;
	}

	/**
	 * Constructor del pool de conexiones, siguiendo el patr�n de dise�o Singleton.
	 * 
	 * @throws NamingException
	 *             si hay problemas con el nombre del recurso JNDI
	 * @throws SQLException
	 *             si hay errores con la base de datos
	 * @throws IOException
	 *             si hay errores con el fichero de bindings
	 */
	private PoolDeConexiones() throws NamingException, SQLException, IOException {
		setLogger();

		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, FILE_SYSTEM_CONTEXT_FACTORY);
		properties.setProperty(Context.PROVIDER_URL, FILE_RES);

		Context context = new InitialContext(properties);

		ds = (DataSource) context.lookup(JDBC_TESTDB_DS);

		// Ajustes propios de Oracle
		logger.debug(traceOracleSettings());

		return;
	}

	/**
	 * Obtiene la instancia del pool de conexiones si no exist�a.
	 * 
	 * @return conexi�n
	 * 
	 * @throws NamingException
	 *             si hay problemas con el nombre del recurso JNDI
	 * @throws SQLException
	 *             si hay errores con la base de datos
	 * @throws IOException
	 *             si hay errors con el fichero de bindings
	 */
	public static PoolDeConexiones getInstance() throws NamingException, SQLException, IOException {
		if (poolDeConexiones == null) {
			poolDeConexiones = new PoolDeConexiones();
		}
		return poolDeConexiones;
	}

	/**
	 * Deshace la transacci�n.
	 * 
	 * @param conn
	 *            conexi�n que contiene la transacci�n
	 */
	public void undo(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
				logger.info("Rollback OK");
			} catch (SQLException e) {
				logger.error("No permite el ROLLBACK");
			}
		}
	}

	/**
	 * Cierra la conexi�n.
	 * 
	 * @param conn
	 *            conexi�n actual
	 */
	public void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				logger.info("Cierre de la conexi�n OK");
			} catch (SQLException e) {
				logger.error("No permite el cierre de la conexi�n");
			}
		}
	}

	/**
	 * Cierra la sentencia.
	 * 
	 * @param statement
	 *            sentencia
	 */
	public void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
				logger.info("Cierre de la sentencia OK");
			} catch (SQLException e) {
				logger.error("No permite el cierre de la sentencia");
			}
		}
	}

	/**
	 * Cierra el result set.
	 * 
	 * @param rs
	 *            resultset a cerrar
	 */
	public void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Cierre del Resultset OK");
			} catch (SQLException e) {
				logger.error("No permite el cierre del Resultset");
			}
		}
	}

	/**
	 * Obtiene una conexi�n.
	 * 
	 * @return conexi�n
	 * @throws SQLException
	 *             si hay un error con la base de datos
	 */
	public Connection getConnection() throws SQLException {

		Connection conn = null;
		// Comentar que no hace falta el sync, porque ya lo hace el driver
		// synchronized(this) {
		conn = ds.getConnection();
		logger.info("Activacion de Autocommit={}", conn.getAutoCommit());
		// }

		conn.setAutoCommit(false);

		// conn.setTransactionIsolation(Connection.TRANSACTION_NONE); //No
		// v�lido en Oracle
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		// conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		// //No v�lido en Oracle
		// conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		// //No valido en Oracle
		// conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

		logger.info(traceConnectionSettings(conn));

		return conn;
	}

	/**
	 * Consulta la configuraci�n de la conexi�n.
	 * 
	 * @param conn
	 *            conexi�n
	 * @return texto con el modo de autocommit y nivel de aislamiento actual
	 * @throws SQLException
	 *             si hay un error con la base de datos
	 */
	public String traceConnectionSettings(Connection conn) throws SQLException {
		String retorno = "Activacion de Autocommit=" + conn.getAutoCommit() + "\n";
		retorno += "Nivel de Aislamiento=";
		switch (conn.getTransactionIsolation()) {
		case Connection.TRANSACTION_NONE:
			retorno += "TRANSACTION_NONE";
			break;
		case Connection.TRANSACTION_READ_COMMITTED:
			retorno += "TRANSACTION_READ_COMMITTED";
			break;
		case Connection.TRANSACTION_READ_UNCOMMITTED:
			retorno += "TRANSACTION_READ_UNCOMMITTED";
			break;
		case Connection.TRANSACTION_REPEATABLE_READ:
			retorno += "TRANSACTION_REPEATABLE_READ";
			break;
		case Connection.TRANSACTION_SERIALIZABLE:
			retorno += "TRANSACTION_REPEATABLE_READ";
			break;
		default:
			throw new RuntimeException("Nivel de aislamiento no detectado. Revisar configuraci�n.");
		}
		return retorno;
	}

	/**
	 * Consulta la configuraci�n del data source de Oracle.
	 * 
	 * @param conn
	 *            conexi�n
	 * @return texto con las caracter�sticas actuales
	 * @throws SQLException
	 *             si hay un error con la base de datos
	 */
	public String traceOracleSettings() throws SQLException {
		OracleDataSource ods = (OracleDataSource) ds;
		String retorno = "trabajando con OracleDataSource\n";
		retorno += "Activacion de Cache de Conexiones=" + ods.getConnectionCachingEnabled() + "\n";

		Properties props = ods.getConnectionCacheProperties();
		retorno += "Tama�o Inicial Cache de Conexiones=" + props.getProperty("InitialLimit") + "\n";
		retorno += "Tama�o Minimo Cache de Conexiones=" + props.getProperty("MinLimit") + "\n";
		retorno += "Tama�o Maximo Cache de Conexiones=" + props.getProperty("MaxLimit") + "\n";

		retorno += "Activacion de Cache de Sentencias Preparadas="
				+ OraclePooledConnection.ExplicitStatementCachingEnabled;

		return retorno;
	}

	/**
	 * Reconfigura el pool de conexiones volviendo a publicar la nueva
	 * configuraci�n.
	 * 
	 * @throws NamingException
	 *             si el nombre del recurso JNDI genera errores
	 * @throws SQLException
	 *             si hay un error con la base de datos
	 */
	public static void reconfigurarPool() throws NamingException, SQLException {
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, FILE_SYSTEM_CONTEXT_FACTORY);
		properties.setProperty(Context.PROVIDER_URL, FILE_RES);

		Context context = new InitialContext(properties);

		OracleDataSource ods = null;
		// Creaci�n de un objeto DataSource
		ods = new OracleDataSource();

		String url = "jdbc:oracle:" + DRIVER_TYPE + ":" + USER + "/" + PASSWORD + "@" + HOST + ":" + PORT + ":" + SID;
		ods.setURL(url);
		ods.setUser(USER);
		ods.setPassword(PASSWORD);

		ods.setConnectionCachingEnabled(true); // Activacion de la cache de
												// conexiones
		Properties cacheProperties = new Properties();

		// Dimensionamiento de la cache de conexiones
		cacheProperties.setProperty("InitialLimit", "5");
		cacheProperties.setProperty("MinLimit", "3");
		cacheProperties.setProperty("MaxLimit", "10");

		ods.setConnectionCacheProperties(cacheProperties);

		ods.setImplicitCachingEnabled(true); // Activacion de la cache de
												// sentencias
		context.rebind(JDBC_TESTDB_DS, ods);
	}

	/**
	 * Redimensiona la cach� de conexiones.
	 * 
	 * @param initialLimit
	 *            tama�o inicial
	 * @param minLimit
	 *            tama�o m�nimo
	 * @param maxLimit
	 *            tama�o m�ximo
	 * @throws SQLException
	 *             si hay un error con la cach� de conexiones
	 */
	public void resizeCache(int initialLimit, int minLimit, int maxLimit) throws SQLException {
		Properties cacheProperties = new Properties();

		cacheProperties.setProperty("InitialLimit", "" + initialLimit);
		cacheProperties.setProperty("MinLimit", "" + minLimit);
		cacheProperties.setProperty("MaxLimit", "" + maxLimit);

		((OracleDataSource) ds).setConnectionCacheProperties(cacheProperties);
		return;
	}
}
