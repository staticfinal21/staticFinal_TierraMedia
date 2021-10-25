package tierraMedia;

import java.util.List;

public class PromocionAbsoluta extends Promocion {
	private Double precioConDescuento;

	public PromocionAbsoluta(String nombrePromocion, Double precioConDescuento, List<Atraccion> listaAtracciones) {
		super(nombrePromocion, listaAtracciones);
		this.nombrePromocion = nombrePromocion;
		this.precioConDescuento = precioConDescuento;
	}

	@Override
	public String getNombre() {
		return nombrePromocion;
	}

	@Override
	public double getPrecio() {
		return precioConDescuento;
	}
}
