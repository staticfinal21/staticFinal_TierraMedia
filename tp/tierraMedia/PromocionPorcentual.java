package tierraMedia;

import java.util.List;

public class PromocionPorcentual extends Promocion {

	private final int PROMOCION_ID;
	private double descuentoPorcentaje;

	public PromocionPorcentual(int PROMOCION_ID, String nombrePromocion, double descuentoPorcentaje,
			List<Atraccion> listaAtracciones) {
		super(nombrePromocion, listaAtracciones);
		this.PROMOCION_ID = PROMOCION_ID;
		this.nombrePromocion = nombrePromocion;
		this.descuentoPorcentaje = descuentoPorcentaje;
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
		return descuentoPorcentaje * super.getPrecioSinDescuento();
	}

}
