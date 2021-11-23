package tierraMedia;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Itinerario {

	private double gastoTotal;
	private double horasConsumidas;
	private List<Ofertable> ofertasCompradas;
	private List<String> listaNombres;
	private Usuario usuario;

	public Itinerario(Usuario usuario) {
		this.usuario = usuario;
		this.listaNombres = new ArrayList<>();
		this.ofertasCompradas = new ArrayList<>();
	}

	public void AddOfertas(Ofertable cadaOferta) {
		ofertasCompradas.add(cadaOferta);
	}

	public List<Ofertable> getOfertasCompradas() {
		return ofertasCompradas;
	}

	public double getGastoTotal() {
		for (Ofertable cadaOferta : ofertasCompradas) {
			gastoTotal += cadaOferta.getPrecio();
		}
		return gastoTotal;
	}

	public double getDuracionTotal() {
		for (Ofertable cadaOferta : ofertasCompradas) {
			horasConsumidas += cadaOferta.getDuracion();
		}
		return horasConsumidas;
	}

	public List<String> getNombreOfertasCompradas() {
		for (Ofertable cadaOferta : ofertasCompradas) {
			listaNombres.add(cadaOferta.getNombre());
		}
		return listaNombres;
	}

	public String formatoItinerario() {
		DecimalFormat df = new DecimalFormat("0.00");
		String presupuestoString = df.format(usuario.getPresupuesto());
		String gastoString = df.format(getGastoTotal());
		return String.format(
				"\n \t  -- Itinerario -- \n \n -Usuario: %s \n \n -Ofertas compradas:\n\n%s------------------------------ \n -Ofertas: %s \n -Duración Total: %s horas \n -Gasto Total: $%s \n------------------------------\n -Dinero disponible: %s \n -Tiempo disponible: %s",
				usuario.getNombre(), ofertasCompradas.toString().replace("[", "").replace("]", ""),
				getNombreOfertasCompradas().toString().replace("[", "").replace("]", ""), getDuracionTotal(),
				gastoString.replace(",", "."), presupuestoString.replace(",", "."), usuario.getHorasDisponibles());
	}

}
