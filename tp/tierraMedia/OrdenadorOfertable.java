package tierraMedia;

import java.util.Comparator;

public class OrdenadorOfertable implements Comparator<Ofertable> {

	public int compare(Ofertable o1, Ofertable o2) {
		if (o1.soyPromocion() && !o2.soyPromocion())
			return -1;
		else if (!o1.soyPromocion() && o2.soyPromocion())
			return 1;
		else if (o1.getPrecio() > o2.getPrecio())
			return -1;
		else if (o1.getPrecio() < o2.getPrecio())
			return 1;
		else if (o1.getDuracion() > o2.getDuracion())
			return -1;
		else if (o1.getDuracion() < o2.getDuracion())
			return 1;
		else
			return 0;
	}

}
