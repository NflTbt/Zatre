package exceptions;

/**
 * Exception die gegooid wordt wanneer een speler een steen legt die enkel
 * grenst aan zijn eigen stenen van dezelfde beurt, en niet aan de stenen van
 * een andere speler.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public class SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException extends ZatreExceptions {
    public SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException() {
	super();
    }

    public SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException(String message) {
	super(message);
    }

    public SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException(Throwable cause) {
	super(cause);
    }
}
