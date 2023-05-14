package exceptions;

/**
 * Superklasse van alle exceptions voor de Zatre app.
 * 
 * @author Tom Coenen
 *
 */
@SuppressWarnings("serial")
public abstract class ZatreExceptions extends Exception {

    public ZatreExceptions() {
	super();
    }

    public ZatreExceptions(String message) {
	super(message);
    }

    public ZatreExceptions(String message, Throwable cause) {
	super(message, cause);
    }

    public ZatreExceptions(Throwable cause) {
	super(cause);
    }
}
