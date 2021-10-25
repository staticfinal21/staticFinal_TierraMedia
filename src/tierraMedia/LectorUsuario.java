package tierraMedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LectorUsuario {

	public static ArrayList<Usuario> cargarUsuario(String path) {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			String linea = br.readLine();

			while (linea != null) {
				String[] datos = linea.split(",");

				String nombre = datos[0];
				Double dinero = Double.parseDouble(datos[1]);
				Integer horasDisponibles = Integer.parseInt(datos[2]);

				usuarios.add(new Usuario(nombre, dinero, horasDisponibles));
				linea = br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					System.out.println("Ocurrió un error al cargar el archivo de usuarios");
					e.printStackTrace();
				}
			}
		}
		return usuarios;
	}
}
