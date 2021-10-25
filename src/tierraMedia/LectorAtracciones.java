package tierraMedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LectorAtracciones {

	public static ArrayList<Atraccion> cargarAtracciones(String path) {
		ArrayList<Atraccion> atracciones = new ArrayList<Atraccion>();

		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			String linea = br.readLine();

			while (linea != null) {
				String[] datos = linea.split(",");

				String nombre = datos[0];
				int costo = Integer.parseInt(datos[1]);
				double duracion = Double.parseDouble(datos[2]);
				int cupo = Integer.parseInt(datos[3]);

				atracciones.add(new Atraccion(nombre, costo, duracion, cupo));
				linea = br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					System.out.println("Ocurrió un error al cargar el archivo de las atracciones");
					e.printStackTrace();
				}
			}
		}
		return atracciones;
	}
}
