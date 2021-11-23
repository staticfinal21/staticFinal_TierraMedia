package dao;

//agarra la exception de sql 
//y lo trabaja en tiempo de ejecucion (por eso extende de RuntimeException)
//con eso me evito poner los throws SQLException en cada metodo, esperando que alguien agarre la exception

public class MissingDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingDataException(Exception e) {
		super(e);
	}
}
