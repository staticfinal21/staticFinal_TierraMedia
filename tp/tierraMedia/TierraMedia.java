package tierraMedia;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import dao.AtraccionDAO;
import dao.FactoryDAO;
import dao.ItinerarioDAOSqLite;
import dao.OfertableDAOSQLite;
import dao.PromocionDAOSQLite;
import dao.UserDAO;

public class TierraMedia {

	private OfertableDAOSQLite ofertableDao = FactoryDAO.getOfertableDAO();
	private AtraccionDAO atraccionDao = FactoryDAO.getAtraccionDao();
	private UserDAO usuarioDao = FactoryDAO.getUserDao();
	private PromocionDAOSQLite promoDAO = FactoryDAO.getPromoDao();
	private ItinerarioDAOSqLite itinerarioDAO = FactoryDAO.getItinerarioDAO();

	Itinerario itinerario;

	private List<Usuario> listaUsuarios;
	private List<Atraccion> listaAtracciones;
	private List<Promocion> listaPromociones;
	private List<Ofertable> listaOfertas;

	public TierraMedia() {
		listaUsuarios = new ArrayList<Usuario>();
		listaAtracciones = new ArrayList<Atraccion>();
		listaPromociones = new ArrayList<Promocion>();
		listaOfertas = new ArrayList<Ofertable>();
	}

	public void cargarListas() {
		listaUsuarios.addAll(usuarioDao.findAll());
		listaAtracciones.addAll(atraccionDao.findAll());
		listaPromociones.addAll(promoDAO.findAll(listaAtracciones));
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

	public void cargarItinerario(Usuario usuario, Itinerario itinerario) {
		for (Ofertable cadaOferta : itinerarioDAO.findItinerario(usuario, listaAtracciones, listaPromociones)) {
			itinerario.AddOfertas(cadaOferta);
		}
	}

	public boolean puedeComprar(Ofertable cadaOferta, Itinerario itinerario) {
		boolean puede = true;
		int i = 0;
		while (puede && i < itinerario.getOfertasCompradas().size()) {
			puede = !cadaOferta.estaIncluida(itinerario.getOfertasCompradas().get(i));
			i++;
		}
		return puede;
	}

	private boolean aplicarFiltrosDeCompra(Ofertable cadaOferta, Usuario cadaUsuario, Itinerario itinerario) {
		boolean puedeComprar = false;
		if (cadaUsuario.getPresupuesto() >= cadaOferta.getPrecio()
				&& cadaUsuario.getHorasDisponibles() >= cadaOferta.getDuracion() && cadaOferta.hayCupo()
				&& puedeComprar(cadaOferta, itinerario)) {
			puedeComprar = true;
		}
		return puedeComprar;
	}

	public void hacerCompra(Ofertable cadaOferta, Usuario cadaUsuario, Itinerario itinerario) throws RuntimeException {

		if (aplicarFiltrosDeCompra(cadaOferta, cadaUsuario, itinerario)) {

			cadaUsuario.comprar(cadaOferta);
			usuarioDao.updateHorasDisponibles(cadaUsuario, cadaOferta);
			usuarioDao.updatePresupuesto(cadaUsuario, cadaOferta);

			cadaOferta.restarCupo();
			ofertableDao.updateCupo(cadaOferta);

			itinerario.AddOfertas(cadaOferta);
			itinerarioDAO.insertarItinerario(cadaUsuario, cadaOferta);

		} else if (cadaUsuario.getPresupuesto() < cadaOferta.getPrecio()) {
			throw new RuntimeException("No se puede comprar la oferta. Saldo insuficiente");
		} else if (cadaUsuario.getHorasDisponibles() < cadaOferta.getDuracion()) {
			throw new RuntimeException("No se puede comprar la oferta. Tiempo disponible insuficiente");
		} else if (!cadaOferta.hayCupo()) {
			throw new RuntimeException("No se puede comprar la oferta. La oferta no tiene cupo");
		} else if (!puedeComprar(cadaOferta, itinerario)) {
			throw new RuntimeException("No se puede comprar la oferta porque ya fue comprada");
		}
	}

	public void hacerSugerencia() {
		DecimalFormat df = new DecimalFormat("0.00"); // rendondeo decimales.
		List<Ofertable> ofertas = getOfertas(listaOfertas);

		for (Usuario cadaUsuario : listaUsuarios) {

			System.out.println("\t \t - BIENVENIDOS A TIERRA MEDIA -");
			System.out.println("===================================================================\n");
			System.out.println("Nombre del visitante: " + cadaUsuario.getNombre() + "\n");
			System.out.println("Usted tiene: $" + df.format(cadaUsuario.getPresupuesto()) + " y "
					+ cadaUsuario.getHorasDisponibles() + " horas disponibles");

			Itinerario itinerario = new Itinerario(cadaUsuario);
			cargarItinerario(cadaUsuario, itinerario);

			for (Ofertable cadaOferta : ofertas) {

				if (aplicarFiltrosDeCompra(cadaOferta, cadaUsuario, itinerario)) {

					System.out.println("\nSugerencia de compra: \n ");
					System.out.println(String.format("%s", cadaOferta.formatoConsola()));

					// --------------SCANNER--------------------------
					Scanner s = new Scanner(System.in);
					String respuesta = "";
					while (!respuesta.equals("S") && !respuesta.equals("N")) {
						System.out.println("¿Desea comprar la sugerencia? S o N ");
						respuesta = s.next().toUpperCase();
					}
					// ---------------------------------------------
					if (respuesta.equals("S")) {
						hacerCompra(cadaOferta, cadaUsuario, itinerario);
						System.out.println("¡Aceptada! \n");
					}

					System.out.println("Usted tiene: $" + df.format(cadaUsuario.getPresupuesto()) + " y "
							+ cadaUsuario.getHorasDisponibles() + " horas disponibles");
					System.out.println("\n-------------------------------------------------------------------");
				}
			}
			System.out.println(itinerario.formatoItinerario());
			System.out.println("\n===================================================================");
		}
	}

	public void restaurarCupo() { // se restauran los cupos de todas las atracciones
		ofertableDao.recuperarCupo();
	}

}
