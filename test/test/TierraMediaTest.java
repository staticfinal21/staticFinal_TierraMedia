package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import dao.AtraccionDAO;
import dao.FactoryDAO;
import dao.ItinerarioDAOSqLite;
import tierraMedia.Atraccion;
import tierraMedia.Itinerario;
import tierraMedia.Ofertable;
import tierraMedia.PromocionAbsoluta;
import tierraMedia.PromocionAxB;
import tierraMedia.PromocionPorcentual;
import tierraMedia.TierraMedia;
import tierraMedia.Usuario;

public class TierraMediaTest {
	ItinerarioDAOSqLite itinerarioDAO;

	TierraMedia sistema;
	Usuario pepe;
	Usuario juan;

	Atraccion abismo;
	Atraccion bosqueNegro;
	Atraccion moria;
	Atraccion erebor;
	Atraccion mordor;
	Atraccion minas;
	Atraccion comarca;

	List<Atraccion> listaAtraciones;

	PromocionAbsoluta promoAbsoluta;
	PromocionPorcentual promoPorcentual;
	PromocionAxB promoAxB;

	Itinerario itinerarioJuan;
	Itinerario itinerarioPepe;

	@Before
	public void before() {
		itinerarioDAO = FactoryDAO.getItinerarioDAO();

		sistema = new TierraMedia();

		// usuarios
		pepe = new Usuario(22, "Pepe", 50, 10);
		juan = new Usuario(33, "Juan", 7, 2);

		// itinerarios
		itinerarioJuan = new Itinerario(juan);
		itinerarioPepe = new Itinerario(pepe);

		// atracciones
		bosqueNegro = new Atraccion(10, "Bosque Negro", 5, 2, 2, 2);
		moria = new Atraccion(11, "Moria", 8, 3, 15, 15);
		abismo = new Atraccion(12, "Abismo de Helm", 1, 10, 20, 20);
		erebor = new Atraccion(13, "Erebor", 3, 1, 2, 2);
		mordor = new Atraccion(15, "Mordor", 5, 1, 1, 1);
		minas = new Atraccion(16, "Minas Tirith", 5, 1, 0, 1);
		comarca = new Atraccion(16, "La Comarca", 5, 51, 1, 1);

		// lista de atracciones
		listaAtraciones = new ArrayList<>();

		listaAtraciones.add(moria);
		listaAtraciones.add(mordor);
		listaAtraciones.add(erebor);

		// Promociones
		promoAbsoluta = new PromocionAbsoluta(11, "Aventura", 10.0, listaAtraciones);
		promoPorcentual = new PromocionPorcentual(12, "Aventura2", 0.9, listaAtraciones);
		promoAxB = new PromocionAxB(13, "Aventura3", 1, listaAtraciones);
	}

	@Test
	public void probarAtraccion() {
		assertTrue(erebor.hayCupo());

		sistema.hacerCompra(mordor, pepe, itinerarioPepe);

		assertFalse(mordor.hayCupo()); // no tiene cupo

		erebor.restarCupo(); // resta cupo
		assertEquals(1, erebor.getCupo(), 0);

		assertFalse(erebor.soyPromocion()); // no es una promo

		// annihilate => limpieza (se borra la oferta comprada del itinerario)
		itinerarioDAO.eliminarItinerario(pepe, mordor);
	}

	@Test
	public void probarPromo() {
		assertTrue(promoAbsoluta.soyPromocion());
		assertEquals("Aventura", promoAbsoluta.getNombre());

		assertEquals(10, promoAbsoluta.getPrecio(), 0); // comparo precio total
		assertEquals(5, promoAbsoluta.getDuracion(), 0); // comparo duracion total

		assertEquals(14.4, promoPorcentual.getPrecio(), 0); // probando promo porcentual
		assertEquals(13, promoAxB.getPrecio(), 0); // probando promo Axb
		assertEquals(10, promoAbsoluta.getPrecio(), 0); // probando promo absoluta

	}

	@Test
	public void probarOrdenOferta() {
		List<Ofertable> ofertas = new ArrayList<>();
		ofertas.add(mordor);
		ofertas.add(promoAbsoluta);
		ofertas.add(erebor);
		ofertas.add(moria);
		ofertas.add(bosqueNegro);

		// probando el ordenamiento
		assertEquals(mordor, ofertas.get(0)); // lista sin ordernar, mordor está primero

		sistema.getOfertas(ofertas); // ordeno la lista

		assertEquals(promoAbsoluta, ofertas.get(0)); // la promo esta primero
		assertEquals(moria, ofertas.get(1)); // sigue la atracción mas costosa
		assertEquals(bosqueNegro, ofertas.get(2));
		assertEquals(mordor, ofertas.get(3)); // mordor cuesta igual que B.Negro, pero dura menos
	}

	@Test
	public void probarDineroYTiempoDisponible() {
		sistema.hacerCompra(bosqueNegro, pepe, itinerarioPepe);

		assertEquals(45, pepe.getPresupuesto(), 0);
		assertEquals(8, pepe.getHorasDisponibles(), 0);

		sistema.hacerCompra(promoAbsoluta, pepe, itinerarioPepe);

		assertEquals(35, pepe.getPresupuesto(), 0);
		assertEquals(3, pepe.getHorasDisponibles(), 0);

		assertEquals(15, itinerarioPepe.getGastoTotal(), 0);
		assertEquals(7, itinerarioPepe.getDuracionTotal(), 0);

		// annihilate => limpieza (se borra la oferta comprada del itinerario)
		itinerarioDAO.eliminarItinerario(pepe, bosqueNegro);
		itinerarioDAO.eliminarItinerario(pepe, promoAbsoluta);
	}

	@Test
	public void probarComprarAtraccionPromocionQueYaCompró() {
		sistema.hacerCompra(bosqueNegro, pepe, itinerarioPepe);
		sistema.hacerCompra(promoAbsoluta, pepe, itinerarioPepe);

		// probando si ya compro la promocion/atraccion
		assertFalse(sistema.puedeComprar(promoAbsoluta, itinerarioPepe)); // no puede comprar la misma promo
		assertFalse(sistema.puedeComprar(moria, itinerarioPepe)); // no puede comprar atraccion que compró en promo
		assertTrue(sistema.puedeComprar(abismo, itinerarioPepe)); // puede comprar porque no la compró todavia

		// annihilate => limpieza (se borra la oferta comprada del itinerario)
		itinerarioDAO.eliminarItinerario(pepe, bosqueNegro);
		itinerarioDAO.eliminarItinerario(pepe, promoAbsoluta);
		itinerarioDAO.eliminarItinerario(pepe, abismo);
	}

	@Test(expected = RuntimeException.class)
	public void probarComprarAtraccionSinCupo() {
		sistema.hacerCompra(minas, pepe, itinerarioPepe); // la atraccion se quedó sin cupo
	}

	@Test(expected = RuntimeException.class)
	public void probarComprarAtraccionSinDinero() {
		sistema.hacerCompra(moria, juan, itinerarioJuan); // no la puede comprar porque no le alcanza la plata
	}

	@Test(expected = RuntimeException.class)
	public void probarComprarAtraccionSinTiempoDisponible() {
		sistema.hacerCompra(comarca, pepe, itinerarioPepe); // no la puede comprar porque no tiene tiempo
	}

	@Test
	public void probandoItinerario() {
		assertEquals(0, itinerarioPepe.getOfertasCompradas().size());

		sistema.hacerCompra(bosqueNegro, pepe, itinerarioPepe);
		sistema.hacerCompra(promoAbsoluta, pepe, itinerarioPepe);

		assertEquals(2, itinerarioPepe.getOfertasCompradas().size());

		assertEquals(bosqueNegro, itinerarioPepe.getOfertasCompradas().get(0));
		assertEquals(promoAbsoluta, itinerarioPepe.getOfertasCompradas().get(1));

		// annihilate => limpieza (se borra la oferta comprada del itinerario)
		itinerarioDAO.eliminarItinerario(pepe, bosqueNegro);
		itinerarioDAO.eliminarItinerario(pepe, promoAbsoluta);
	}

	@Test
	public void restaurarCupo() {
		List<Atraccion> listaAtracciones = new ArrayList<Atraccion>();
		AtraccionDAO atraccionDao = FactoryDAO.getAtraccionDao();
		listaAtracciones.addAll(atraccionDao.findAll());

		sistema.restaurarCupo();
		assertEquals(6, listaAtracciones.get(0).getCupo());
	}

}
