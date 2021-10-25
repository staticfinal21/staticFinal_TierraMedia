package tierraMedia;

public class Atraccion implements Ofertable {

	private String nombreAtraccion;
	private double costoAtraccion;
	private double duracionAtraccion;
	private int cupoAtraccion;

	public Atraccion(String nombre, double costo, double duracion, int cupo) {
		this.nombreAtraccion = nombre;
		this.costoAtraccion = costo;
		this.duracionAtraccion = duracion;
		this.cupoAtraccion = cupo;
	}

	@Override
	public String getNombre() {
		return nombreAtraccion;
	}

	@Override
	public double getPrecio() {
		return costoAtraccion;
	}

	@Override
	public double getDuracion() {
		return duracionAtraccion;
	}

	public int getCupo() {
		return cupoAtraccion;
	}

	@Override
	public boolean hayCupo() {
		return cupoAtraccion > 0;

	}

	public synchronized void restarCupo() {
		this.cupoAtraccion--;
	}

	@Override
	public boolean soyPromocion() {
		return false;
	}

	@Override
	public boolean estaIncluida(Ofertable oferta) {
		if (oferta.soyPromocion()) {
			return oferta.estaIncluida(this);
		}
		return this.equals(oferta);
	}

	public String formatoConsola() {
		return String.format(" -Atracción: [%s] \n -Duración: %s horas \n -Precio: $%s \n", nombreAtraccion,
				duracionAtraccion, costoAtraccion);
	}

	@Override
	public String toString() {
		return formatoConsola() + "\n";
	}

}
