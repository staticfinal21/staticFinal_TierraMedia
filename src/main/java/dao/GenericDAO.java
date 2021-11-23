package dao;

import java.util.List;

public interface GenericDAO<T> { // defino los metodos -minimos- que van a tener los daos

	public List<T> findAll();
}
