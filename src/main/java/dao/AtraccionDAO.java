package dao;

import tierraMedia.Atraccion;

public interface AtraccionDAO extends GenericDAO<Atraccion> {

	public int updateCupo(Atraccion atraccion);

}
