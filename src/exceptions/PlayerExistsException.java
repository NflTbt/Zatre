package exceptions;

/**
 * Exception die gegooid wordt wanneer de persistentielaag een speler probeert aan te maken die reeds in de MYSQL database aanwezig is.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class PlayerExistsException extends ZatreExceptions {

    public PlayerExistsException() {

    }

    public PlayerExistsException(String message) {
	super(message);
    }

    public PlayerExistsException(String message, Throwable cause) {
	super(message, cause);
    }

    public PlayerExistsException(Throwable cause) {
	super(cause);
    }
}
