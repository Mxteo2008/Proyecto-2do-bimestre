package repository;

public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public RepositorioException(String mensaje) {
        super(mensaje);
    }
}
