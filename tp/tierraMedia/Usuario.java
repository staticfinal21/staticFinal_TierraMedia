package tierraMedia;

public class Usuario {

	private final int USUARIO_ID;
	private String nombreUsuario;
	private double presupuesto;
	private double horasDisponibles;

	public Usuario(int USUARIO_ID, String nombre, double presupuesto, int horasDisponibles) {
		this.USUARIO_ID = USUARIO_ID;
		this.nombreUsuario = nombre;
		this.presupuesto = presupuesto;
		this.horasDisponibles = horasDisponibles;
	}

	public int getId() {
		return USUARIO_ID;
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

	public void comprar(Ofertable cadaOferta) {
		this.presupuesto -= cadaOferta.getPrecio();
		this.horasDisponibles -= cadaOferta.getDuracion();
	}
}
