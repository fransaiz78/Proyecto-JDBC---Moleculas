package lsi.ubu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lsi.ubu.util.*;

public class FormulasMoleculares {

	private static PoolDeConexiones pool;
	private static Logger logger;

	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		Connection con = null;

		// Para tener el sql original cargado para cada transacion
		ExecuteScript ejecScript = new ExecuteScript();

		try {
			inicializaciones();

			// ejecScript.main(args);
			// // Transacion insertarMolecula(nombre, simbolos[], nros[]):
			// String[] simbolos = { "H", "O" };
			// int[] nros = { 2, 1 };
			// System.out.println("La transaccion:insertarMolecula(nombre,
			// simbolos[], nros[]) es: "
			// + insertarMolecula("AGUA", simbolos, nros) + " = true\n");

			ejecScript.main(args);
			// Transacion borrarMolecula(nombreMolecula):
			borrarMolecula("Agua");

			// ejecScript.main(args);
			// // Transacion borrarMolecula(id)
			// borrarMolecula(967);

			// ejecScript.main(args);
			// // Transacion actualizarMolecula (id, simbolo, nro)
			// System.out.println("La transaccion: actualizarMolecula (id,
			// simbolo, nro) es: "
			// + actualizarMolecula(962, "H", 3) + " = true\n");

			// ejecScript.main(args);
			// // TransacionactualizarMolecula (nombre, simbolo, nro):
			// System.out.println("La transaccion: actualizarMolecula (nombre,
			// simbolo, nro) es: "
			// + actualizarMolecula("Agua", "H", 3) + " = true\n");

		} catch (Exception e) {
			System.err.println(e.getMessage());
			logger.error(e.getLocalizedMessage());
			pool.undo(con);
		}
	}

	// Nº 1
	public static boolean insertarMolecula(String nombre, String[] simbolos, int[] nros) {

		Connection con = null;
		boolean retorno = false;

		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		PreparedStatement pst3 = null;

		ResultSet rs = null;

		String form = "";
		int pesoTotal = 0;
		int insertados = 0;

		try {
			con = pool.getConnection();

			// Calcular formula y sacar pesos.
			pst = con.prepareStatement("SELECT pesoAtomico FROM Elementos WHERE simbolo=?");

			for (int i = 0; i < simbolos.length; i++) {
				form = form.concat(simbolos[i]);
				if (nros[i] > 1) {
					form = form.concat("" + nros[i]);
				}

				// Sacamos el peso de ese simbolo y nos creamos un array
				// auxiliar de los pesos.
				pst.setString(1, simbolos[i]);
				rs = pst.executeQuery();
				rs.next();
				// int peso = rsPeso.getInt(1);
				pesoTotal += rs.getInt(1) * nros[i];
			}

			// Insertar en molecula II
			// Insertar molecula en Moleculas:
			pst2 = con.prepareStatement(
					"Insert into Moleculas(id, nombre, pesoMolecular, formula) values(seq_molId.nextval, ?, ?, ?)");
			pst2.setString(1, nombre);
			pst2.setInt(2, pesoTotal);
			pst2.setString(3, form);
			insertados = pst2.executeUpdate();

			pst3 = con.prepareStatement(
					"INSERT INTO Composicion(simbolo, idMolecula, nroAtomos) values(?, seq_molId.currval, ?)");
			// Relleno Composicion
			for (int i = 0; i < simbolos.length; i++) {

				pst3.setString(1, simbolos[i]);
				pst3.setInt(2, nros[i]);
				insertados += pst3.executeUpdate();
			}

			if (insertados == 3) {
				logger.info("La transacion ha ido bien.");
				con.commit();
				retorno = true;
			} else {
				retorno = false;
			}

		} catch (SQLException e) {
			// Falta algo
			logger.error("La transacion hay que deshacerla.");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());

		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pst);
			pool.close(pst2);
			pool.close(rs);
		}

		return retorno;
	}

	// Nº 2
	public static boolean borrarMolecula(String nombreMol) {
		boolean retorno = false;
		Connection con = null;
		PreparedStatement pstId = null;
		ResultSet rsId = null;
		int idMol = 0;

		try {
			con = pool.getConnection();

			pstId = con.prepareStatement("SELECT id FROM MOLECULAS WHERE nombre=?");
			pstId.setString(1, nombreMol);

			rsId = pstId.executeQuery();
			// Si el nombre d la molecula existe.
			if (rsId.next()) {
				idMol = rsId.getInt(1);
				borrarMolecula(idMol);

				con.commit();
			} else {
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			}

		} catch (SQLException e) {
			// Falta algo
			logger.error("La transacion hay que deshacerla.");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());

		} catch (ChemistryException e) {
			// TODO Auto-generated catch block
			logger.error("La transacion hay que deshacerla.");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());

		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pstId);
			pool.close(rsId);
		}

		return retorno;
	}

	// Nº 3
	public static boolean actualizarMolecula(String nombreMol, String simbolo, int nro) {
		Connection con = null;
		boolean retorno = false;

		PreparedStatement pstId = null;
		ResultSet rsId = null;

		int idMol = 0;

		try {
			con = pool.getConnection();
			// Obtenemos el id correspondiente a la molecula de ese nombre.
			pstId = con.prepareStatement("SELECT id FROM MOLECULAS WHERE nombre=?");
			pstId.setString(1, nombreMol);
			rsId = pstId.executeQuery();
			rsId.next();
			idMol = rsId.getInt(1);

			retorno = actualizarMolecula(idMol, simbolo, nro);

		} catch (SQLException e) {
			// Falta algo
			logger.error("La transacion hay que deshacerla.");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());

		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pstId);
			pool.close(rsId);

		}

		return retorno;
	}

	// Nº 4
	public static void borrarMolecula(int id) throws ChemistryException {
		Connection con = null;

		PreparedStatement pstComp = null;
		PreparedStatement pstMol = null;

		int nroComp = 0;
		int nroMol = 0;

		try {
			con = pool.getConnection();

			pstComp = con.prepareStatement("DELETE FROM COMPOSICION WHERE IdMolecula=?");
			pstComp.setInt(1, id);
			nroComp = pstComp.executeUpdate();

			pstMol = con.prepareStatement("DELETE FROM MOLECULAS WHERE id=?");
			pstMol.setInt(1, id);
			nroMol = pstMol.executeUpdate();

			if (nroMol == 1 && nroComp == 2) {
				logger.info("La transacion ha ido bien.");
				con.commit();

			} else {
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			}

		} catch (ChemistryException e) {
			// Falta algo
			logger.error("La transacion hay que deshacerla.");
			logger.error(e.getLocalizedMessage());

			pool.undo(con);

		} catch (SQLException e) {
			logger.error("La transacion hay que deshacerla.");
			logger.error(e.getLocalizedMessage());
			pool.undo(con);
		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pstComp);
			pool.close(pstMol);
		}
	}

	// Nº 5
	public static boolean actualizarMolecula(int id, String simbolo, int nro) {

		boolean retorno = false;

		Connection con = null;

		PreparedStatement pstActuComp = null;
		PreparedStatement pstActuMol = null;
		PreparedStatement pstMol = null;
		PreparedStatement pstPrue = null;

		ResultSet rsJoin = null;
		ResultSet rsPrue = null;

		int pesoAtomico = 0;
		int pesoMolecularTotal = 0;
		int numAtomos = 0;
		String numAtomosSt = "";
		String formula = "";
		String simb = "";

		try {
			con = pool.getConnection();

			pstPrue = con.prepareStatement("SELECT formula FROM Moleculas WHERE id=?");
			pstPrue.setInt(1, id);
			rsPrue = pstPrue.executeQuery();
			rsPrue.next();
			String formulaOriginal = rsPrue.getString("formula");
			
			// Actualizamos el nroAtomos de la tabla Composicion
			pstActuComp = con.prepareStatement("UPDATE Composicion set nroAtomos=? WHERE idMolecula=? and simbolo=?");
			pstActuComp.setInt(1, nro);
			pstActuComp.setInt(2, id);
			pstActuComp.setString(3, simbolo);
			int filActu = pstActuComp.executeUpdate();
			
//			if(filActu == 0){
//				throw(new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));
//			}

			// Hacemos un join entre Elementos y Composicion para tener en la
			// misma tabla todos los campos necesarios para las formulas.

			pstMol = con.prepareStatement("SELECT simbolo, nombre, pesoAtomico, nroAtomos, idMolecula"
					+ " FROM Elementos inner join Composicion"
					+ " ON Elementos.simbolo = composicion.simbolo and composicion.idmolecula=?");
			pstMol.setInt(1, id);
			rsJoin = pstMol.executeQuery();

			while (rsJoin.next()) {
				// Obtenemos el peso atomico y el numero de atomos del simbolo.
				simb = rsJoin.getString(1);
				pesoAtomico = rsJoin.getInt(3);
				numAtomos = rsJoin.getInt(4);
				numAtomosSt = rsJoin.getString(4);
				// Calculo el peso molecular.
				pesoMolecularTotal += (pesoAtomico * numAtomos);
				// Concatenamos la formula.
				formula = formula.concat(simb);
				if (numAtomos > 1) {
					formula = formula.concat(numAtomosSt);
				}
			}

			if(formula==formulaOriginal){
				throw(new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
			}
			
			// Actualizar Cadena
			pstActuMol = con.prepareStatement("UPDATE Moleculas set Formula=?, pesoMolecular=? WHERE id=?");
			pstActuMol.setString(1, formula);
			pstActuMol.setInt(2, pesoMolecularTotal);
			pstActuMol.setInt(3, id);
			pstActuMol.executeUpdate();

			if ((pesoMolecularTotal == 21)) {
				logger.info("La transacion ha ido bien.");
				con.commit();
				retorno = true;
			} else {
				retorno = false;
			}

		} catch (SQLException e) {
			// Falta algo
			logger.error("Deshaciendo transaccion...");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());

		} catch (ChemistryException e) {
			// TODO Auto-generated catch block
			logger.error("Deshaciendo transaccion...");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());
		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos...");
			pool.close(con);
			pool.close(pstMol);
			pool.close(pstActuComp);
			pool.close(pstActuMol);
			pool.close(rsJoin);
		}

		return retorno;
	}

	public static void inicializaciones() throws NamingException, SQLException, IOException {
		// Inicializacion de Pool
		pool = PoolDeConexiones.getInstance();
		// Inicializacion del Logger
		logger = LoggerFactory.getLogger(FormulasMoleculares.class);
		// Metodos de logger que nos ha dicho.
		logger.info("Comienzo Ejecución");
	}

}
