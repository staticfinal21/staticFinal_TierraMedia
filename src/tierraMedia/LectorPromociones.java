package tierraMedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorPromociones {

	public static ArrayList<Promocion> cargarPromociones(String path, List<Atraccion> atracciones) {
		ArrayList<Promocion> promociones = new ArrayList<Promocion>();

		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);

			String linea = br.readLine();

			while (linea != null) {
				String[] datos = linea.split(",");

				String nombre = datos[0];
				double monto = Double.parseDouble(datos[2]);
				String tipoPromo = datos[1].toUpperCase();

				String[] stringListaAtracciones = datos[3].split("-");
				List<Atraccion> atraccionesDePromocion = new ArrayList<Atraccion>();

				for (int i = 0; i < stringListaAtracciones.length; i++)
					atraccionesDePromocion.add(buscarAtraccion(stringListaAtracciones[i], atracciones));

				if (tipoPromo.equals("PORCENTUAL"))
					promociones.add(new PromocionPorcentual(nombre, monto, atraccionesDePromocion));

				if (tipoPromo.equals("ABSOLUTA"))
					promociones.add(new PromocionAbsoluta(nombre, monto, atraccionesDePromocion));

				if (tipoPromo.equals("AXB"))
					promociones.add(new PromocionAxB(nombre, monto, atraccionesDePromocion));

				linea = br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					System.out.println("Ocurrió un error al cargar el archivo de las promociones");
					e.printStackTrace();
				}
			}
		}
		return promociones;
	}

	private static Atraccion buscarAtraccion(String cadena, List<Atraccion> listaAtracciones) {
		Atraccion a = null;
		for (Atraccion cadaAtraccion : listaAtracciones) {
			if (cadaAtraccion.getNombre().equals(cadena)) {
				a = cadaAtraccion;
			}
		}
		return a;
	}
}
