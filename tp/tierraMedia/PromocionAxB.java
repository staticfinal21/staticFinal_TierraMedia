package tierraMedia;

import java.util.List;

public class PromocionAxB extends Promocion {

	private final int PROMOCION_ID;
	private int cantidadAtraccionesGratis;

	public PromocionAxB(int PROMOCION_ID, String nombrePromocion, int cantidadAtraccionesGratis,
			List<Atraccion> listaAtracciones) {
		super(nombrePromocion, listaAtracciones);
		this.PROMOCION_ID = PROMOCION_ID;
		this.nombrePromocion = nombrePromocion;
		this.cantidadAtraccionesGratis = cantidadAtraccionesGratis;
	}

	@Override
	public int getId() {
		return PROMOCION_ID;
	}

	@Override
	public String getNombre() {
		return nombrePromocion;
	}

	@Override
	public double getPrecio() {
		double valor = 0;
		for (int i = 0; i < listaAtracciones.size() - cantidadAtraccionesGratis; i++) {
			valor += listaAtracciones.get(i).getPrecio();
		}
		return valor;
	}

}
