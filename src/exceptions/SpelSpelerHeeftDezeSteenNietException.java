package exceptions;

/**
 * Exception die gegooid wordt wanneer een Speler een steen probeert te leggen
 * die niet in zijn lijst zit.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public class SpelSpelerHeeftDezeSteenNietException extends ZatreExceptions {

    public SpelSpelerHeeftDezeSteenNietException() {
	super();
    }

    public SpelSpelerHeeftDezeSteenNietException(String message) {
	super(message);
    }

    public SpelSpelerHeeftDezeSteenNietException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelSpelerHeeftDezeSteenNietException(Throwable cause) {
	super(cause);
    }
}
