package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import tierraMedia.Ofertable;
import tierraMedia.Usuario;

public class UserDAOSQLite implements UserDAO {

	public List<Usuario> findAll() {
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		try {
			String sql = "SELECT * FROM usuarios";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				listaUsuarios.add(toUsuario(result));
			}

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return listaUsuarios;
	}

	private Usuario toUsuario(ResultSet result) {
		try {
			return new Usuario(result.getInt("usuario_id"), result.getString("nombre_usuario"),
					result.getDouble("presupuesto"), result.getInt("horas_disponibles"));

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	public int updatePresupuesto(Usuario usuario, Ofertable oferta) {
		int rows = 0;
		try {
			String sql = "UPDATE usuarios SET presupuesto = presupuesto - ? WHERE usuario_id = ? ";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, oferta.getPrecio());
			statement.setInt(2, usuario.getId());
			rows = statement.executeUpdate();

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return rows;
	}

	public int updateHorasDisponibles(Usuario usuario, Ofertable oferta) {
		int rows = 0;
		try {
			String sql = "UPDATE usuarios SET horas_disponibles = horas_disponibles - ? WHERE usuario_id = ? ";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, oferta.getDuracion());
			statement.setInt(2, usuario.getId());
			rows = statement.executeUpdate();

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return rows;
	}

}
