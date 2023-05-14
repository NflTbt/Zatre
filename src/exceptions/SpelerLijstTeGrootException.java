package exceptions;

/**
 * Exception die gegooid wordt wanneer de SpelerRepository reeds het maximum aantal spelers bevat, en er nog een speler wordt toegevoegd.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class SpelerLijstTeGrootException extends ZatreExceptions {

    public SpelerLijstTeGrootException() {
	super();
    }

    public SpelerLijstTeGrootException(String message) {
	super(message);
    }

    public SpelerLijstTeGrootException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelerLijstTeGrootException(Throwable cause) {
	super(cause);
    }
}
