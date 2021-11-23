package dao;

import tierraMedia.Ofertable;
import tierraMedia.Usuario;

public interface UserDAO extends GenericDAO<Usuario> {

	public int updatePresupuesto(Usuario usuario, Ofertable oferta);

	public int updateHorasDisponibles(Usuario usuario, Ofertable oferta);
}
