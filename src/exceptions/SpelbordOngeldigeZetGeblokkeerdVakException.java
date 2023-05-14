package exceptions;

/**
 * Exception die gegooid wordt wanneer er een poging is om een steen op een
 * geblokkeerd vak te leggen.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public class SpelbordOngeldigeZetGeblokkeerdVakException extends ZatreExceptions {

    public SpelbordOngeldigeZetGeblokkeerdVakException() {
	super();
    }

    public SpelbordOngeldigeZetGeblokkeerdVakException(java.lang.String message, java.lang.Throwable cause) {
	super(message, cause);
    }

    public SpelbordOngeldigeZetGeblokkeerdVakException(java.lang.String message) {
	super(message);
    }

    public SpelbordOngeldigeZetGeblokkeerdVakException(java.lang.Throwable cause) {
	super(cause);
    }
}
