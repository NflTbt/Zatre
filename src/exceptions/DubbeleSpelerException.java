package exceptions;

/**
 * Exception die gegooid wordt wanneer men probeert een speler aan de SpelerRepository toe te voegen die reeds in de lijst aanwezig is.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class DubbeleSpelerException extends ZatreExceptions {

    public DubbeleSpelerException() {
	super();
    }

    public DubbeleSpelerException(String message) {
	super(message);
    }

    public DubbeleSpelerException(String message, Throwable cause) {
	super(message, cause);
    }

    public DubbeleSpelerException(Throwable cause) {
	super(cause);
    }
}
