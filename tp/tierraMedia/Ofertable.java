package tierraMedia;

public interface Ofertable {

	public int getId();

	public String getNombre();

	public double getPrecio();

	public double getDuracion();

	public boolean hayCupo();

	public void restarCupo();

	public void restaurarCupo();

	public boolean soyPromocion();

	public boolean estaIncluida(Ofertable oferta);

	public String formatoConsola();

}
