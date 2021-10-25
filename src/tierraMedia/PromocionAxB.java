package tierraMedia;

import java.util.List;

public class PromocionAxB extends Promocion {
	private double cantidadAtraccionesGratis;
	private List<Atraccion> listaAtracciones;

	public PromocionAxB(String nombrePromocion, double cantidadAtraccionesGratis, List<Atraccion> listaAtracciones) {
		super(nombrePromocion, listaAtracciones);
		this.nombrePromocion = nombrePromocion;
		this.cantidadAtraccionesGratis = cantidadAtraccionesGratis;
		this.listaAtracciones = listaAtracciones;
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
