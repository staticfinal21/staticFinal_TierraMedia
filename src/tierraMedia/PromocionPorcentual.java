package tierraMedia;

import java.util.List;

public class PromocionPorcentual extends Promocion {
	private double descuentoPorcentaje;

	public PromocionPorcentual(String nombrePromocion, double descuentoPorcentaje, List<Atraccion> listaAtracciones) {
		super(nombrePromocion, listaAtracciones);
		this.nombrePromocion = nombrePromocion;
		this.descuentoPorcentaje = descuentoPorcentaje;
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
