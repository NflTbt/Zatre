package exceptions;

/**
 * Exception die gegooid wordt indien een input naar het domein ongeldige karakters bevat, ter preventie van MYSQL en/of domeinissues. Ongeldige karakters: \ ' " # & %
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class OngeldigeKaraktersException extends ZatreExceptions {

    public OngeldigeKaraktersException() {
	super();
    }

    public OngeldigeKaraktersException(String message) {
	super(message);
    }

    public OngeldigeKaraktersException(String message, Throwable cause) {
	super(message, cause);
    }

    public OngeldigeKaraktersException(Throwable cause) {
	super(cause);
    }
}
