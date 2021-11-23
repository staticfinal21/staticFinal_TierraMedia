package tierraMedia;

import java.sql.SQLException;
import jdbc.ConnectionProvider;

public class App {

	public static void main(String[] args) throws SQLException {

		TierraMedia parque = new TierraMedia();

		parque.cargarListas();

		parque.hacerSugerencia();

		// parque.restaurarCupo();

		ConnectionProvider.close();
	}

}
