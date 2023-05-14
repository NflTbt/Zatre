package exceptions;

/**
 * Exception die gegooid wordt wanneer een zet een score zou opleveren die hoger
 * is dan 12.
 * 
 * @author Tom Coenen
 *
 */

@SuppressWarnings("serial")
public class SpelbordOngeldigeZetSomHogerDanTwaalfException extends ZatreExceptions {

    public SpelbordOngeldigeZetSomHogerDanTwaalfException() {
	super();
    }

    public SpelbordOngeldigeZetSomHogerDanTwaalfException(String message, Throwable cause) {
	super(message, cause);
    }

    public SpelbordOngeldigeZetSomHogerDanTwaalfException(String message) {
	super(message);
    }

    public SpelbordOngeldigeZetSomHogerDanTwaalfException(Throwable cause) {
	super(cause);
    }
}
