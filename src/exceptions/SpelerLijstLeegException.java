package exceptions;

/**
 * Exception die gegooid wordt wanneer de spelerlijst leeg is, maar toch aangesproken wordt.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class SpelerLijstLeegException extends ZatreExceptions {
    public SpelerLijstLeegException() {
	super();
    }

    public SpelerLijstLeegException(String message) {
	super(message);
    }

    public SpelerLijstLeegException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelerLijstLeegException(Throwable cause) {
	super(cause);
    }
}
