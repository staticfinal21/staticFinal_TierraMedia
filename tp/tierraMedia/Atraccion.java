package tierraMedia;

public class Atraccion implements Ofertable {

	private final int ATRACCION_ID;
	private String nombreAtraccion;
	private double costoAtraccion;
	private double duracionAtraccion;
	private int cupoAtraccion;
	private final int CUPO_LIMITE;

	public Atraccion(int ATRACCION_ID, String nombre, double costo, double duracion, int cupo, int CUPO_LIMITE) {
		this.ATRACCION_ID = ATRACCION_ID;
		this.nombreAtraccion = nombre;
		this.costoAtraccion = costo;
		this.duracionAtraccion = duracion;
		this.cupoAtraccion = cupo;
		this.CUPO_LIMITE = CUPO_LIMITE;
	}

	@Override
	public int getId() {
		return ATRACCION_ID;
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

	public int getCupoLimite() {
		return CUPO_LIMITE;
	}

	@Override
	public boolean hayCupo() {
		return cupoAtraccion > 0;
	}

	@Override
	public synchronized void restarCupo() {
		this.cupoAtraccion--;
	}

	@Override
	public void restaurarCupo() {
		this.cupoAtraccion = this.CUPO_LIMITE;
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

	@Override
	public String formatoConsola() {
		return String.format(" -Atracción: [%s] \n -Duración: %s horas \n -Precio: $%s \n", nombreAtraccion,
				duracionAtraccion, costoAtraccion);
	}

	@Override
	public String toString() {
		return formatoConsola() + "\n";
	}

}
