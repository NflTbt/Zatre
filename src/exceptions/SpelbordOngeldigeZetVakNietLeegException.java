package exceptions;

/**
 * Exception die gegooid wordt wanneer een speler een zet doet op een reeds
 * ingenomen vak.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public class SpelbordOngeldigeZetVakNietLeegException extends ZatreExceptions {

    public SpelbordOngeldigeZetVakNietLeegException() {
	super();
    }

    public SpelbordOngeldigeZetVakNietLeegException(String message) {
	super(message);
    }

    public SpelbordOngeldigeZetVakNietLeegException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelbordOngeldigeZetVakNietLeegException(Throwable cause) {
	super(cause);
    }
}
