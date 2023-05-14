package exceptions;

/**
 * Exception die gegooid wordt wanneer het spel niet afgelopen is, maar toch methodes aangeroepen worden die pas na afloop kunnen gebruikt worden.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class SpelNietAfgelopenException extends ZatreExceptions {
    public SpelNietAfgelopenException() {
	super();
    }

    public SpelNietAfgelopenException(String message) {
	super(message);
    }

    public SpelNietAfgelopenException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelNietAfgelopenException(Throwable cause) {
	super(cause);
    }
}
