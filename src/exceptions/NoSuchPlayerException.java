package exceptions;

/**
 * Exception die gegooid wordt wanneer een speler wordt opgevraagd uit de persistentielaag, maar deze niet gevonden kan worden.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class NoSuchPlayerException extends ZatreExceptions {

    public NoSuchPlayerException() {
	super();
    }

    public NoSuchPlayerException(String message) {
	super(message);
    }

    public NoSuchPlayerException(String message, Throwable cause) {
	super(message, cause);
    }

    public NoSuchPlayerException(Throwable cause) {
	super(cause);
    }
}
