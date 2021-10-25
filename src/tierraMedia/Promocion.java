package tierraMedia;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Promocion implements Ofertable {
	protected String nombrePromocion;
	protected List<Atraccion> listaAtracciones;

	public Promocion(String nombrePromocion, List<Atraccion> listaAtracciones) {
		this.listaAtracciones = listaAtracciones;
		this.nombrePromocion = nombrePromocion;
	}

	@Override
	public String getNombre() { // nombre de la promo
		return nombrePromocion;
	}

	public List<String> getNombreAtraccionesIncluidas() { // nombre de cada atraccion
		List<String> listaNombresPromos = new ArrayList<>();
		for (Atraccion cadaAtraccion : listaAtracciones) {
			listaNombresPromos.add(cadaAtraccion.getNombre());
		}
		return listaNombresPromos;
	}

	@Override
	public double getPrecio() { // precio con descuento incluido, que luego sobreescriben las promos hijas
		return 0.0;
	}

	public double getPrecioSinDescuento() {
		double valor = 0;
		for (Atraccion cadaAtraccion : listaAtracciones) {
			valor += cadaAtraccion.getPrecio();
		}
		return valor;
	}

	@Override
	public double getDuracion() { // duracion total, sumando todas las atracciones de la promo
		double duracionTotal = 0;
		for (Atraccion cadaAtraccion : listaAtracciones) {
			duracionTotal += cadaAtraccion.getDuracion();
		}
		return duracionTotal;
	}

	@Override
	public boolean hayCupo() {
		boolean verCupo = true;
		int i = 0;
		while (verCupo && i < listaAtracciones.size()) {
			verCupo = listaAtracciones.get(i).hayCupo();
			i++;
		}
		return verCupo;
	}

	@Override
	public synchronized void restarCupo() { // resta cupo en cada atraccion
		for (Atraccion cadaAtraccion : listaAtracciones) {
			cadaAtraccion.restarCupo();
		}
	}

	@Override
	public boolean soyPromocion() {
		return true;
	}

	@Override
	public boolean estaIncluida(Ofertable oferta) { // se fija si la oferta ya fue comprada o no.
		int i = 0;
		boolean a = false;
		while (!a && i < listaAtracciones.size()) {
			a = oferta.estaIncluida(listaAtracciones.get(i));
			i++;
		}
		return a;
	}

	@Override
	public String formatoConsola() {
		DecimalFormat df = new DecimalFormat("0.00");
		String precioDescuento = df.format(getPrecio());
		return String.format(
				" -Promoción: [%s] \n -Atracciones incluidas: %s \n -Duración: %s horas \n -Precio original: $%s \n -Precio con descuento: $%s \n",
				getNombre(), getNombreAtraccionesIncluidas(), getDuracion(), getPrecioSinDescuento(),
				precioDescuento.replace(",", "."));
	}

	@Override
	public String toString() {
		return formatoConsola() + "\n";
	}

	public List<Atraccion> getListaAtracciones() {
		return listaAtracciones;
	}

}
