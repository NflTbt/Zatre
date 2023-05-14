package exceptions;

/**
 * Exception die gegooid wordt wanneer een speler een ongeldige zet doet op een
 * grijs vak.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public class SpelbordOngeldigeZetGrijsVakException extends ZatreExceptions {

    public SpelbordOngeldigeZetGrijsVakException() {
	super();
    }

    public SpelbordOngeldigeZetGrijsVakException(String message) {
	super(message);
    }

    public SpelbordOngeldigeZetGrijsVakException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelbordOngeldigeZetGrijsVakException(Throwable cause) {
	super(cause);
    }
}
