package exceptions;

/**
 * Exception die gegooid wordt wanneer het spel na afloop geen winnaar kan bepalen.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class GeenWinnaarException extends ZatreExceptions {
    public GeenWinnaarException() {
	super();
    }

    public GeenWinnaarException(String message) {
	super(message);
    }

    public GeenWinnaarException(String message, Throwable cause) {
	super(message, cause);
    }

    public GeenWinnaarException(Throwable cause) {
	super(cause);
    }
}
