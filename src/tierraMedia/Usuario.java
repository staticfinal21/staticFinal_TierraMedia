package tierraMedia;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
	private String nombreUsuario;
	private double presupuesto;
	private double horasDisponibles;

	private List<Ofertable> itinerario;

	public Usuario(String nombre, double presupuesto, int horasDisponibles) {
		this.nombreUsuario = nombre;
		this.presupuesto = presupuesto;
		this.horasDisponibles = horasDisponibles;
		itinerario = new ArrayList<>();
	}

	public String getNombre() {
		return nombreUsuario;
	}

	public double getPresupuesto() {
		return presupuesto;
	}

	public double getHorasDisponibles() {
		return horasDisponibles;
	}

	public List<Ofertable> getItinerario() {
		return itinerario;
	}

	public void setItinerario(Ofertable cadaOferta) {
		itinerario.add(cadaOferta);
	}

	public double getGastoTotal() {
		double gastoTotal = 0;
		for (Ofertable cadaOferta : itinerario) {
			gastoTotal += cadaOferta.getPrecio();
		}
		return gastoTotal;
	}

	public double getDuracionTotal() {
		double horasConsumidas = 0;
		for (Ofertable cadaOferta : itinerario) {
			horasConsumidas += cadaOferta.getDuracion();
		}
		return horasConsumidas;
	}

	public List<String> getNombreOfertasCompradas() {
		List<String> listaNombres = new ArrayList<>();
		for (Ofertable cadaOferta : itinerario) {
			listaNombres.add(cadaOferta.getNombre());
		}
		return listaNombres;
	}

	public void comprar(Ofertable cadaOferta) {
		setItinerario(cadaOferta);
		this.presupuesto -= cadaOferta.getPrecio();
		this.horasDisponibles -= cadaOferta.getDuracion();
	}

	public String formatoItinerario() {
		DecimalFormat df = new DecimalFormat("0.00");
		String presupuestoString = df.format(presupuesto);
		String gastoString = df.format(getGastoTotal());
		return String.format(
				"\t  -- Itinerario -- \n \n -Usuario: %s \n \n -Ofertas compradas:\n\n%s------------------------------ \n -Ofertas: %s \n -Duración Total: %s horas \n -Gasto Total: $%s \n------------------------------\n -Dinero disponible: %s \n -Tiempo disponible: %s",
				nombreUsuario, itinerario.toString().replace("[", "").replace("]", ""),
				getNombreOfertasCompradas().toString().replace("[", "").replace("]", ""), getDuracionTotal(),
				gastoString.replace(",", "."), presupuestoString.replace(",", "."), horasDisponibles);
	}

	@Override
	public String toString() {
		return "Nombre= " + nombreUsuario + ", Dinero= " + presupuesto + ", Horas Disponibles= " + horasDisponibles
				+ "\n";
	}
}
