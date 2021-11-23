package dao;

public class FactoryDAO { // CREA los distintos DAOs

	public static UserDAO getUserDao() {
		return new UserDAOSQLite();
	}

	public static AtraccionDAO getAtraccionDao() {
		return new AtraccionDAOSQLite();
	}

	public static PromocionDAOSQLite getPromoDao() {
		return new PromocionDAOSQLite();
	}

	public static OfertableDAOSQLite getOfertableDAO() {
		return new OfertableDAOSQLite();
	}

	public static ItinerarioDAOSqLite getItinerarioDAO() {
		return new ItinerarioDAOSqLite();
	}

}
