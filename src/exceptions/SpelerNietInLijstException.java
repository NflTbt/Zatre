package exceptions;

/**
 * Exception die gegooid wordt wanneer men een speler probeert aan te spreken die niet in de repository lijst voorkomt.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class SpelerNietInLijstException extends ZatreExceptions {

    public SpelerNietInLijstException() {
	super();
    }

    public SpelerNietInLijstException(String message) {
	super(message);
    }

    public SpelerNietInLijstException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelerNietInLijstException(Throwable cause) {
	super(cause);
    }
}
