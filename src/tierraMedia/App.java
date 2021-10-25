package tierraMedia;

public class App {

	public static void main(String[] args) {

		TierraMedia p = new TierraMedia();

		p.agregarListaPersonas("datos/Usuario");
		p.agregarListaAtracciones("datos/Atraccion");
		p.agregarListaPromociones("datos/Promocion");

		p.hacerSugerencia();
	}

}
