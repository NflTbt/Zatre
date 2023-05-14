package exceptions;

/**
 * Exception die gegooid wordt wanneer een speler probeert een steen te leggen
 * op een vak niet naast een andere steen.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public class SpelbordOngeldigeZetNietAanpalendException extends ZatreExceptions {

    public SpelbordOngeldigeZetNietAanpalendException() {
	super();
    }

    public SpelbordOngeldigeZetNietAanpalendException(String message) {
	super(message);
    }

    public SpelbordOngeldigeZetNietAanpalendException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelbordOngeldigeZetNietAanpalendException(Throwable cause) {
	super(cause);
    }
}
