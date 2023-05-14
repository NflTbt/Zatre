package exceptions;

/**
 * Exception die gegooid wordt wanneer men het spel probeert te starten terwijl de actieve spelerslijst nog niet genoeg spelers bevat.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class SpelerLijstTeKleinException extends ZatreExceptions {

    public SpelerLijstTeKleinException() {
	super();
    }

    public SpelerLijstTeKleinException(String message) {
	super(message);
    }

    public SpelerLijstTeKleinException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelerLijstTeKleinException(Throwable cause) {
	super(cause);
    }
}
