package exceptions;

/**
 * Exception die gegooid wordt wanneer de speler geen speelkansen over heeft, en dus niet kan gekozen worden.
 * @author  Tom Coenen
 */
@SuppressWarnings("serial")
public class GeenSpeelkansenException extends ZatreExceptions {
    public GeenSpeelkansenException() {
	super();
    }

    public GeenSpeelkansenException(String message) {
	super(message);
    }

    public GeenSpeelkansenException(String message, Throwable cause) {
	super(message, cause);
    }

    public GeenSpeelkansenException(Throwable cause) {
	super(cause);
    }
}
