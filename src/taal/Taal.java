package taal;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Statische klasse als front voor resourcebundles.
 * 
 * @author Tom Coenen
 *
 */
public abstract class Taal {

    /**
     * Methode die een string returnt uit de resourcebundel. Kan aangeroepen worden
     * met enkel taal en sleutel OF taal, sleutel en 1 dynamische domeinregelwaarde
     * OF taal, sleutel en een array van dynamische domeinregelwaarden
     * 
     * @param taal                 Taal van de gewenste string, komt uit ENUM
     * @param sleutel              Key gebruikt om de gewenste string op te halen
     * @param domeinRegelParameter Waarde in te vullen in de {} velden van de
     *                             String. Kan een enkele waarde zijn (als er enkel
     *                             {0} in de String staat), of een array in het
     *                             geval van meerdere in te vullen waarden ({0} is
     *                             positie 0, {1} positie 1, enz
     * @return String met de gevraagde message
     */
    public static String getString(Talen taal, String sleutel, Object... domeinRegelParameter) {
		return MessageFormat.format(
			ResourceBundle.getBundle("taal/resource_bundle", getLocale(taal)).getString(sleutel),
			domeinRegelParameter);
    }

    /**
     * Returnt de locale die hoort bij de aangegeven taal
     * 
     * @param taal De taal die locale bepaalt
     * @return De bijhorende Locale
     */
    private static Locale getLocale(Talen taal) {
		return switch (taal) {
			case NL -> new Locale("nl");
			case EN -> Locale.ENGLISH;
			case FR -> Locale.FRENCH;
			default -> new Locale("nl");
		};
    }
}
