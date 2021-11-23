package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import tierraMedia.Atraccion;

public class AtraccionDAOSQLite implements AtraccionDAO {

	public List<Atraccion> findAll() {
		List<Atraccion> listaAtracciones = new ArrayList<Atraccion>();
		try {
			String sql = "SELECT * FROM atracciones";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				listaAtracciones.add(toAtraccion(result));
			}

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return listaAtracciones;
	}

	private Atraccion toAtraccion(ResultSet result) {
		try {
			return new Atraccion(result.getInt("atraccion_id"), result.getString("nombre_atraccion"),
					result.getDouble("costo_atraccion"), result.getDouble("duracion_atraccion"),
					result.getInt("cupo_atraccion"), result.getInt("cupo_limite_atraccion"));

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	public int updateCupo(Atraccion atraccion) {
		int rows = 0;
		try {
			Connection connection = ConnectionProvider.getConnection();
			String sql = "UPDATE atracciones SET atracciones.cupo_atraccion=? WHERE atracciones.atraccion_id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, atraccion.getCupo() - 1);
			statement.setInt(2, atraccion.getId());
			rows = statement.executeUpdate();

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return rows;
	}

}
