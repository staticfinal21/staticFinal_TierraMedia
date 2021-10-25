package tierraMedia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TierraMedia {
	private List<Usuario> listaUsuarios;
	private List<Atraccion> listaAtracciones;
	private List<Promocion> listaPromociones;
	private List<Ofertable> listaOfertas;
	private static String respuesta = "a";

	public TierraMedia() {
		listaUsuarios = new ArrayList<>();
		listaAtracciones = new ArrayList<>();
		listaPromociones = new ArrayList<>();
		listaOfertas = new ArrayList<>();
	}

	public boolean agregarListaPersonas(String rutaArchivo) {
		return listaUsuarios.addAll(LectorUsuario.cargarUsuario(rutaArchivo));
	}

	public boolean agregarListaAtracciones(String rutaArchivo) {
		return listaAtracciones.addAll(LectorAtracciones.cargarAtracciones(rutaArchivo));
	}

	public boolean agregarListaPromociones(String rutaArchivo) {
		return listaPromociones.addAll(LectorPromociones.cargarPromociones(rutaArchivo, listaAtracciones));
	}

	public Atraccion buscar(int posicion) {
		return listaAtracciones.get(posicion);
	}

	public List<Atraccion> getAtracciones() {
		return listaAtracciones;
	}

	public List<Promocion> getPromociones() {
		return listaPromociones;
	}

	public List<Usuario> getUsuarios() {
		return listaUsuarios;
	}

	public void creandoOfertas() {
		for (Promocion cadaPromo : listaPromociones)
			listaOfertas.add(cadaPromo);

		for (Atraccion cadaAtraccion : listaAtracciones)
			listaOfertas.add(cadaAtraccion);
	}

	public List<Ofertable> getOfertas(List<Ofertable> ofertadas) {
		creandoOfertas();
		Collections.sort(ofertadas, new OrdenadorOfertable());
		return ofertadas;
	}

	public boolean puedeComprar(Ofertable cadaOferta, Usuario cadaUsuario) {
		boolean puede = true;
		int i = 0;
		while (puede && i < cadaUsuario.getItinerario().size()) {
			puede = !cadaOferta.estaIncluida(cadaUsuario.getItinerario().get(i));
			i++;
		}
		return puede;
	}

	public boolean aplicarFiltrosDeCompra(Ofertable cadaOferta, Usuario cadaUsuario) {
		boolean puedeComprar = false;
		if (cadaUsuario.getPresupuesto() >= cadaOferta.getPrecio()
				&& cadaUsuario.getHorasDisponibles() >= cadaOferta.getDuracion() && cadaOferta.hayCupo()
				&& puedeComprar(cadaOferta, cadaUsuario)) {
			puedeComprar = true;
		}
		return puedeComprar;
	}

	public void hacerCompra(Ofertable cadaOferta, Usuario cadaUsuario) throws RuntimeException { // Metodo para
																									// posibilitar el
																									// testeo (junit)

		if (aplicarFiltrosDeCompra(cadaOferta, cadaUsuario)) {
			cadaUsuario.comprar(cadaOferta);
			cadaOferta.restarCupo();

		} else if (cadaUsuario.getPresupuesto() < cadaOferta.getPrecio()) {
			throw new RuntimeException("No se puede comprar la oferta. Saldo insuficiente");
		} else if (cadaUsuario.getHorasDisponibles() < cadaOferta.getDuracion()) {
			throw new RuntimeException("No se puede comprar la oferta. Tiempo disponible insuficiente");
		} else if (!cadaOferta.hayCupo()) {
			throw new RuntimeException("No se puede comprar la oferta. La oferta no tiene cupo");
		} else if (!puedeComprar(cadaOferta, cadaUsuario)) {
			throw new RuntimeException("No se puede comprar la oferta porque ya fue comprada");
		}
	}

	public void hacerSugerencia() {
		List<Ofertable> ofertas = getOfertas(listaOfertas);

		for (Usuario cadaUsuario : listaUsuarios) {
			System.out.println("\t \t - BIENVENIDOS A TIERRA MEDIA -");
			System.out.println("===================================================================\n");
			System.out.println("Nombre del visitante: " + cadaUsuario.getNombre() + "\n");
			System.out.println("Usted tiene: $" + cadaUsuario.getPresupuesto() + " y "
					+ cadaUsuario.getHorasDisponibles() + " horas disponibles");

			for (Ofertable cadaOferta : ofertas) {

				if (aplicarFiltrosDeCompra(cadaOferta, cadaUsuario)) {

					System.out.println("\nSugerencia de compra: \n ");
					System.out.println(String.format("%s", cadaOferta.formatoConsola()));

					usarScanner(); // permite escribir en consola y devuelve la respuesta

					if (respuesta.equals("S")) {
						hacerCompra(cadaOferta, cadaUsuario);
						System.out.println("¡Aceptada! \n");
					}

					DecimalFormat df = new DecimalFormat("0.00"); // rendondeo decimales.
					System.out.println("Usted tiene: $" + df.format(cadaUsuario.getPresupuesto()) + " y "
							+ cadaUsuario.getHorasDisponibles() + " horas disponibles");
					System.out.println("\n-------------------------------------------------------------------");

					respuesta = "a"; // cambio la respuesta para entrar al while en la proxima iteracion
				}
			}
			System.out.println(cadaUsuario.formatoItinerario());
			System.out.println("\n===================================================================");
			getSalida(cadaUsuario);
		}
	}

	public static String usarScanner() {
		while (!respuesta.equals("S")) {
			if (respuesta.equals("N")) {
				break;
			}
			System.out.println("¿Desea comprar la sugerencia? S o N ");

			Scanner s = new Scanner(System.in);
			respuesta = s.next().toUpperCase();
		}
		return respuesta;
	}

	public void getSalida(Usuario cadaUsuario) {
		try {
			String fileName = "salida/" + cadaUsuario.getNombre() + ".txt";
			List<String> lines = Arrays.asList(cadaUsuario.formatoItinerario());
			Path file = Paths.get(fileName);
			Files.write(file, lines, StandardCharsets.UTF_8);
		}

		catch (IOException e) {
			System.out.println("Ocurrió un error al cargar el archivo de salida de usuarios");
			e.printStackTrace();
		}
	}
}
