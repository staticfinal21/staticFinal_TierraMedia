package tierraMedia;

import java.util.List;

public class PromocionAbsoluta extends Promocion {

	private final int PROMOCION_ID;
	private double precioConDescuento;

	public PromocionAbsoluta(int PROMOCION_ID, String nombrePromocion, Double precioConDescuento,
			List<Atraccion> listaAtracciones) {
		super(nombrePromocion, listaAtracciones);
		this.PROMOCION_ID = PROMOCION_ID;
		this.nombrePromocion = nombrePromocion;
		this.precioConDescuento = precioConDescuento;
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
		return precioConDescuento;
	}

}
