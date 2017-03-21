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

	public static void main(String[] args) {

		try {
			inicializaciones();
		} catch (NamingException | SQLException | IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("---------------------------------------------------------------");
		System.out.println("        - Bateria de pruebas para el caso de INSERTAR -        ");
		System.out.println("---------------------------------------------------------------\n");

		try {
			String[] simbolos = { "H", "O" };
			int[] nros = { 2, 1 };
			insertarMolecula("Agua", simbolos, nros);
			System.out.println("Insertar molecula se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
				System.out.println("Insertar molecula con formula existente. OK. ");
			} else {
				System.out.println("Insertar molecula con formula existente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			String[] simbolos = { "C", "H", "V" };
			int[] nros = { 1, 4 };
			insertarMolecula("Agua", simbolos, nros);
			System.out.println("Borrar molecula por un id se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.TAMAÑOS_INADECUADOS) {
				System.out.println("Insertar molecula con arrays de diferente tam. OK. ");
			} else {
				System.out.println("Insertar molecula con arrays de diferente tam. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			String[] simbolos = { "C", "H" };
			int[] nros = { 1, 4 };
			insertarMolecula("Metano", simbolos, nros);
			System.out.println("Insertar molecula se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NO_EXISTE_ATOMO) {
				System.out.println("Insertar molecula con atomo inexistente. OK. ");
			} else {
				System.out.println("Insertar molecula con atomo inexistente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			String[] simbolos = { "H", "O" };
			int[] nros = { 2, 1 };
			insertarMolecula("Agua2", simbolos, nros);
			System.out.println("Insertar molecula se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
				System.out.println("Insertar molecula con formula existente. OK. ");
			} else {
				System.out.println("Insertar molecula con formula existente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			String[] simbolos = { "H", "O" };
			int[] nros = { 4, 5 };
			insertarMolecula("Agua", simbolos, nros);
			System.out.println("Insertar molecula se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE) {
				System.out.println("Insertar molecula con nombre existente. OK. ");
			} else {
				System.out.println("Insertar molecula con nombre existente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		// Acabamos la bateria de pruebas para el caso Insertar con la molecula:
		// H2O.

		System.out.println("\n-----------------------------------------------------------------");
		System.out.println(" - Bateria de pruebas para el caso de ACTUALIZAR - ");
		System.out.println("-----------------------------------------------------------------\n");

		try {
			actualizarMolecula(1, "V", 4);
			System.out.println("ActualizarMolecula mediante Id con simbolo inexistente se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.MOLECULA_NO_CONTIENE_SIMBOLO) {
				System.out.println("ActualizarMolecula mediante Id con simbolo inexistente. OK. ");
			} else {
				System.out.println("ActualizarMolecula mediante Id con simbolo inexistente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		// El valor actual del H es 2
		try {
			actualizarMolecula(1, "H", 2);
			System.out.println("ActualizarMolecula mediante Id con formula existente se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
				System.out.println("ActualizarMolecula mediante Id con formula existente. OK. ");
			} else {
				System.out.println("ActualizarMolecula mediante Id con formula existente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			actualizarMolecula(1, "H", 4);
			System.out.println("ActualizarMolecula mediante Id se ha realizado con éxito.");
		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
				System.out.println("ActualizarMolecula mediante Id. OK. ");
			} else {
				System.out.println("ActualizarMolecula mediante Id. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			actualizarMolecula("Agua", "V", 4);
			System.out.println("ActualizarMolecula mediante Nombre con simbolo inexistente se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.MOLECULA_NO_CONTIENE_SIMBOLO) {
				System.out.println("ActualizarMolecula mediante Nombre con simbolo inexistente. OK. ");
			} else {
				System.out.println("ActualizarMolecula mediante Nombre con simbolo inexistente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		// En la ultima actualizacion, el H se ha quedado con 4
		try {
			actualizarMolecula("Agua", "H", 4);
			System.out.println("ActualizarMolecula mediante Nombre con formula existente se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
				System.out.println("ActualizarMolecula mediante Nombre con formula existente. OK. ");
			} else {
				System.out.println("ActualizarMolecula mediante Nombre con formula existente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			actualizarMolecula("Agua", "H", 2);
			System.out.println("ActualizarMolecula mediante Nombre se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
				System.out.println("ActualizarMolecula mediante Nombre. OK. ");
			} else {
				System.out.println("ActualizarMolecula mediante Nombre. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		// Acabamos la bateria de pruebas para el caso Actualizar con la
		// molecula: H2O.

		System.out.println("\n-----------------------------------------------------------");
		System.out.println("        - Bateria de pruebas para el caso de BORRAR -        ");
		System.out.println("-------------------------------------------------------------\n");

		try {
			borrarMolecula(1);
			System.out.println("Borrar molecula mediante Id se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
				System.out.println("Borrar molecula mediante Id si no existe. OK. ");
			} else {
				System.out.println("Borrar molecula mediante Id si no existe. MAL.		 ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			borrarMolecula(1);
			System.out.println("Borrar molecula mediante Id se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
				System.out.println("Borrar molecula mediante Id si no existe. OK. ");
			} else {
				System.out.println("Borrar molecula mediante Id si no existe. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			String[] simbolos = { "H", "O" };
			int[] nros = { 2, 1 };
			insertarMolecula("Agua", simbolos, nros);
			System.out.println("Insertar molecula se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
				System.out.println("Insertar molecula con formula existente. OK. ");
			} else {
				System.out.println("Insertar molecula con formula existente. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			borrarMolecula("Agua");
			System.out.println("Borrar molecula mediante Nombre se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
				System.out.println("Borrar molecula mediante Nombre si no existe. OK. ");
			} else {
				System.out.println("Borrar molecula mediante Nombre si no existe. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}

		try {
			borrarMolecula("Agua");
			System.out.println("Borrar molecula mediante Nombre se ha realizado con éxito.");

		} catch (ChemistryException e) {
			if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
				System.out.println("Borrar molecula mediante Nombre si no existe. OK. ");
			} else {
				System.out.println("Borrar molecula mediante Nombre si no existe. MAL. ");
			}
			// System.err.println(e.getMessage());
			// logger.error(e.getLocalizedMessage());
		}
	}

	// Nº 1
	public static void insertarMolecula(String nombre, String[] simbolos, int[] nros) throws ChemistryException {

		Connection con = null;

		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		PreparedStatement pst3 = null;
		PreparedStatement pstExc = null;
		PreparedStatement pstExc2 = null;

		ResultSet rs = null;
		ResultSet rsExc = null;
		ResultSet rsExc2 = null;

		String form = "";
		int pesoTotal = 0;
		int insertados = 0;

		try {
			con = pool.getConnection();

			// Comprobamos si ambos arrays son del mismo tamaño.
			if (simbolos.length != nros.length) {
				throw (new ChemistryException(ChemistryError.TAMAÑOS_INADECUADOS));
			}

			// Comprobamos si el nombre de la molecula a insertar ya existe.
			pstExc = con.prepareStatement("SELECT * FROM Moleculas WHERE nombre=?");
			pstExc.setString(1, nombre);
			rsExc = pstExc.executeQuery();

			if (rsExc.next()) {
				throw (new ChemistryException(ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE));
			}

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

				if (rs.next()) {
					pesoTotal += rs.getInt(1) * nros[i];
				} else {
					pool.close(con);
					pool.close(pst);
					pool.close(pst2);
					pool.close(rs);
					throw (new ChemistryException(ChemistryError.NO_EXISTE_ATOMO));
				}

			}

			// Comprobar si la formula ya existe.
			pstExc2 = con.prepareStatement("Select * FROM Moleculas Where formula=?");
			pstExc2.setString(1, form);
			rsExc2 = pstExc2.executeQuery();
			if (rsExc2.next()) {
				throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
			}
			// Insertar en molecula II
			// Insertar molecula en Moleculas:
			pst2 = con.prepareStatement(
					"INSERT INTO Moleculas(id, nombre, pesoMolecular, formula) values(seq_molId.nextval, ?, ?, ?)");
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
				System.out.println("HAGO COMMIT");
				con.commit();
			} else {
			}

		} catch (SQLException e) {
			// Falta algo
			logger.error("La transacion hay que deshacerla.");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();

		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pst);
			pool.close(pst2);
			pool.close(pstExc);
			pool.close(pstExc2);
			pool.close(rs);
			pool.close(rsExc);
			pool.close(rsExc2);

		}

	}

	// Nº 2
	public static void borrarMolecula(String nombreMol) throws ChemistryException {
		Connection con = null;
		PreparedStatement pstId = null;
		ResultSet rsId = null;
		int idMol = 0;

		try {
			con = pool.getConnection();

			pstId = con.prepareStatement("SELECT id FROM MOLECULAS WHERE nombre=?");
			pstId.setString(1, nombreMol);

			rsId = pstId.executeQuery();
			// Si el nombre de la molecula no existe.
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
			e.printStackTrace();

		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pstId);
			pool.close(rsId);
		}
	}

	// Nº 3
	public static void actualizarMolecula(String nombreMol, String simbolo, int nro) throws ChemistryException {
		Connection con = null;

		PreparedStatement pstId = null;
		ResultSet rsId = null;

		int idMol = 0;

		try {
			con = pool.getConnection();
			// Obtenemos el id correspondiente a la molecula de ese nombre.
			pstId = con.prepareStatement("SELECT id FROM MOLECULAS WHERE nombre=?");
			pstId.setString(1, nombreMol);
			rsId = pstId.executeQuery();
			if (rsId.next()) {
				idMol = rsId.getInt(1);
				actualizarMolecula(idMol, simbolo, nro);
			} else {
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			}

		} catch (SQLException e) {
			// Falta algo
			logger.error("La transacion hay que deshacerla.");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();

		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pstId);
			pool.close(rsId);
		}
	}

	// Nº 4
	public static void borrarMolecula(int id) throws ChemistryException {
		Connection con = null;

		PreparedStatement pstComp = null;
		PreparedStatement pstMol = null;

		try {
			con = pool.getConnection();

			pstComp = con.prepareStatement("DELETE FROM COMPOSICION WHERE idmolecula=?");
			pstComp.setInt(1, id);
			int nroComp = pstComp.executeUpdate();

			System.out.println("ID: " + id);
			pstMol = con.prepareStatement("delete from moleculas where id=?");
			pstMol.setInt(1, id);
			int nroM = pstMol.executeUpdate();
			// int nroMol = pstMol.executeUpdate();
			System.out.println("Mol: " + nroM);

			if (nroM != 0 && nroComp != 0) {
				// logger.info("La transacion ha ido bien.");
				con.commit();
			} else {
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			}

		} catch (SQLException e) {
			logger.error("La transacion hay que deshacerla.");
			logger.error(e.getLocalizedMessage());
			pool.undo(con);
			e.printStackTrace();

		} finally {
			// Rellenar por el alumno
			// logger.debug("Cerrando recursos");
			pool.close(con);
			pool.close(pstComp);
			pool.close(pstMol);
		}
	}

	// Nº 5
	public static void actualizarMolecula(int id, String simbolo, int nro) throws ChemistryException {

		Connection con = null;

		PreparedStatement pstActuComp = null;
		PreparedStatement pstActuMol = null;
		PreparedStatement pstMol = null;
		PreparedStatement pstPrue = null;
		PreparedStatement pstPrue8 = null;

		ResultSet rsJoin = null;
		ResultSet rsPrue = null;
		ResultSet rsPrue8 = null;

		int pesoAtomico = 0;
		int pesoMolecularTotal = 0;
		int numAtomos = 0;
		String numAtomosSt = "";
		String formula = "";
		String simb = "";

		try {
			con = pool.getConnection();

			pstPrue8 = con
					.prepareStatement("SELECT * FROM Composicion WHERE simbolo=? and idMolecula=? and nroAtomos=?");
			pstPrue8.setString(1, simbolo);
			pstPrue8.setInt(2, id);
			pstPrue8.setInt(3, nro);

			rsPrue8 = pstPrue8.executeQuery();

			// Comprobacion de que los parametros pasados existen ya enn la
			// tabla.

			if (rsPrue8.next()) {
				throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
			} else {
				pstPrue = con.prepareStatement("SELECT formula FROM Moleculas WHERE id=?");
				pstPrue.setInt(1, id);
				rsPrue = pstPrue.executeQuery();
				rsPrue.next();
				String formulaOriginal = rsPrue.getString("formula");

				// Actualizamos el nroAtomos de la tabla Composicion
				pstActuComp = con
						.prepareStatement("UPDATE Composicion set nroAtomos=? WHERE idMolecula=? and simbolo=?");
				pstActuComp.setInt(1, nro);
				pstActuComp.setInt(2, id);
				pstActuComp.setString(3, simbolo);
				int filActu = pstActuComp.executeUpdate();

				if (filActu == 0) {
					throw (new ChemistryException(ChemistryError.MOLECULA_NO_CONTIENE_SIMBOLO));
				}

				// Hacemos un join entre Elementos y Composicion para tener en
				// la
				// misma tabla todos los campos necesarios para las formulas.

				pstMol = con.prepareStatement(
						"SELECT elementos.simbolo, elementos.nombre, elementos.pesoatomico, composicion.nroatomos "
								+ "FROM Elementos inner join Composicion "
								+ "ON Elementos.simbolo = composicion.simbolo and composicion.idMolecula=?");
				pstMol.setInt(1, id);
				rsJoin = pstMol.executeQuery();

				while (rsJoin.next()) {
					// Obtenemos el peso atomico y el numero de atomos del
					// simbolo.
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

				if (formula == formulaOriginal) {
					throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
				}

				// Actualizar Cadena
				pstActuMol = con.prepareStatement("UPDATE Moleculas set Formula=?, pesoMolecular=? WHERE id=?");
				pstActuMol.setString(1, formula);
				pstActuMol.setInt(2, pesoMolecularTotal);
				pstActuMol.setInt(3, id);
				pstActuMol.executeUpdate();

				con.commit();

			}

		} catch (SQLException e) {
			// Falta algo
			logger.error("Deshaciendo transaccion...");
			pool.undo(con);
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();

		} finally {
			// Rellenar por el alumno
			logger.debug("Cerrando recursos...");
			pool.close(con);
			pool.close(pstMol);
			pool.close(pstActuComp);
			pool.close(pstActuMol);
			pool.close(pstPrue);
			pool.close(pstPrue8);
			pool.close(rsJoin);
			pool.close(rsPrue);
			pool.close(rsPrue8);
		}
	}

	public static void inicializaciones() throws NamingException, SQLException, IOException {
		// Inicializacion de Pool
		pool = PoolDeConexiones.getInstance();
		// Inicializacion del Logger
		logger = LoggerFactory.getLogger(FormulasMoleculares.class);
		// Metodos de logger que nos ha dicho.
		logger.info("Comienzo Ejecución");
		
		ExecuteScript.run(".\\sql\\crear_tablas.sql");
	}

}
