package exceptions;

/**
 * Exception die gegooid wordt wanneer de eerste steen op het bord niet in het
 * midden geplaatst wordt.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public class SpelbordOngeldigeZetNietInMiddenException extends ZatreExceptions {

    public SpelbordOngeldigeZetNietInMiddenException() {
	super();
    }

    public SpelbordOngeldigeZetNietInMiddenException(String message) {
	super(message);
    }

    public SpelbordOngeldigeZetNietInMiddenException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelbordOngeldigeZetNietInMiddenException(Throwable cause) {
	super(cause);
    }
}
