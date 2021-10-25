package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import tierraMedia.Atraccion;
import tierraMedia.Ofertable;
import tierraMedia.PromocionAbsoluta;
import tierraMedia.PromocionAxB;
import tierraMedia.PromocionPorcentual;
import tierraMedia.TierraMedia;
import tierraMedia.Usuario;

public class TierraMediaTest {

	TierraMedia sistema;
	Usuario pepe;
	Usuario juan;

	Atraccion abismo;
	Atraccion bosqueNegro;
	Atraccion moria;
	Atraccion erebor;
	Atraccion mordor;

	List<Atraccion> listaAtraciones;

	PromocionAbsoluta promoAbsoluta;
	PromocionPorcentual promoPorcentual;
	PromocionAxB promoAxB;

	@Before
	public void before() {
		sistema = new TierraMedia();

		// usuarios
		pepe = new Usuario("Pepe", 50, 10);
		juan = new Usuario("Juan", 7, 2);

		// atracciones
		bosqueNegro = new Atraccion("Bosque Negro", 5, 2, 2);
		moria = new Atraccion("Moria", 8, 3, 15);
		abismo = new Atraccion("Abismo de Helm", 1, 10, 20);
		erebor = new Atraccion("Erebor", 3, 1, 2);
		mordor = new Atraccion("Mordor", 5, 1, 1);

		// lista de atracciones
		listaAtraciones = new ArrayList<>();

		listaAtraciones.add(moria);
		listaAtraciones.add(mordor);
		listaAtraciones.add(erebor);

		// Promociones
		promoAbsoluta = new PromocionAbsoluta("Aventura", 10.0, listaAtraciones);
		promoPorcentual = new PromocionPorcentual("Aventura2", 0.9, listaAtraciones);
		promoAxB = new PromocionAxB("Aventura3", 1, listaAtraciones);
	}

	@Test
	public void probarAtraccion() {
		assertTrue(erebor.hayCupo());

		sistema.hacerCompra(mordor, pepe);
		assertFalse(mordor.hayCupo()); // no tiene cupo

		erebor.restarCupo(); // resta cupo
		assertEquals(1, erebor.getCupo(), 0);

		assertFalse(erebor.soyPromocion()); // no es una promo
	}

	@Test
	public void probarPromo() {
		assertTrue(promoAbsoluta.soyPromocion());
		assertEquals("Aventura", promoAbsoluta.getNombre());

		assertEquals(10, promoAbsoluta.getPrecio(), 0); // comparo precio total
		assertEquals(5, promoAbsoluta.getDuracion(), 0); // comparo duracion total

		assertEquals(14.4, promoPorcentual.getPrecio(), 0); // probando promo porcentual
		assertEquals(13, promoAxB.getPrecio(), 0); // probando promo Axb

		sistema.hacerCompra(mordor, pepe);
		assertFalse(promoAbsoluta.hayCupo()); // promo sin cupo porque incluye una atraccion sin cupo
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
	public void comprarAtraccionYPromo() {
		sistema.hacerCompra(bosqueNegro, pepe);

		assertEquals(45, pepe.getPresupuesto(), 0);
		assertEquals(8, pepe.getHorasDisponibles(), 0);

		sistema.hacerCompra(promoAbsoluta, pepe);

		// datos se utilizan en la salida
		assertEquals(35, pepe.getPresupuesto(), 0);
		assertEquals(3, pepe.getHorasDisponibles(), 0);

		assertEquals(15, pepe.getGastoTotal(), 0);
		assertEquals(7, pepe.getDuracionTotal(), 0);

		// probando si ya compro la promocion/atraccion
		assertFalse(sistema.puedeComprar(promoAbsoluta, pepe)); // no puede comprar la misma promo
		assertFalse(sistema.puedeComprar(moria, pepe)); // no puede comprar una atraccion que incluye la promo que
														// compró
		assertTrue(sistema.puedeComprar(abismo, pepe)); // puede comprar porque no la compró todavia
	}

	@Test(expected = RuntimeException.class)
	public void probarComprarAtraccionSinDineroTiempoOCupoYQueEsteRepetida() {
		sistema.hacerCompra(mordor, juan); // juan compra mordor y se va a quedar sin cupo
		sistema.hacerCompra(erebor, pepe); // pepe compra erebor

		// Cada linea que sigue lanza una exception:
		sistema.hacerCompra(erebor, pepe); // no se puede comprar porque ya la compró
		sistema.hacerCompra(mordor, pepe); // la promocion incluye atraccion sin cupo
		sistema.hacerCompra(moria, juan); // no la puede comprar porque no le alcanza la plata
		sistema.hacerCompra(abismo, pepe); // no la puede comprar porque no tiene tiempo
	}

	@Test
	public void probandoItinerario() {
		assertEquals(0, pepe.getItinerario().size());

		sistema.hacerCompra(bosqueNegro, pepe);
		sistema.hacerCompra(promoAbsoluta, pepe);

		assertEquals(2, pepe.getItinerario().size());

		assertEquals(bosqueNegro, pepe.getItinerario().get(0));
		assertEquals(promoAbsoluta, pepe.getItinerario().get(1));
	}

}
