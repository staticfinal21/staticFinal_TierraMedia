package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import tierraMedia.Ofertable;

public class OfertableDAOSQLite {

	public List<Integer> getAtraccionesIDEnPromo(int ofertaID) {
		List<Integer> listaAtraccionesID = new ArrayList<Integer>();

		try {
			String sql = "select atraccion_promocion.atraccion_id\r\n" + "from atraccion_promocion\r\n"
					+ "where promocion_id =" + ofertaID;
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				listaAtraccionesID.add(result.getInt(1));
			}
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return listaAtraccionesID;
	}

	public int updateCupo(Ofertable oferta) {
		int rows = 0;

		if (oferta.soyPromocion()) {
			for (int atraccionID : getAtraccionesIDEnPromo(oferta.getId())) {
				String sql = "UPDATE atracciones SET cupo_atraccion=cupo_atraccion - ? WHERE atraccion_id=?";

				try {
					Connection connection = ConnectionProvider.getConnection();
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setInt(1, 1);
					statement.setInt(2, atraccionID);
					rows = statement.executeUpdate();
				} catch (Exception e) {
					throw new MissingDataException(e);
				}
			}

		} else {
			String sql = "UPDATE atracciones SET cupo_atraccion= cupo_atraccion - ? WHERE atraccion_id=?";

			try {
				Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, 1);
				statement.setInt(2, oferta.getId());
				rows = statement.executeUpdate();

			} catch (Exception e) {
				throw new MissingDataException(e);
			}
		}
		return rows;
	}

	public int recuperarCupo() {
		int rows = 0;

		try {
			String sql = "UPDATE atracciones SET cupo_atraccion=cupo_limite_atraccion";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			rows = statement.executeUpdate();

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return rows;
	}

}
