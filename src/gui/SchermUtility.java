package gui;

import domein.DomeinController;
import taal.Talen;

/**
 * 
 * @author Robin Verplancke
 * @author Valentijn De Borggrave
 *
 */

public interface SchermUtility {

    /**
     * Deze methode wordt in de klasse die de interface implementeren gebruikt om de
     * domeincontroller in te stellen
     * 
     * @param dc De domeincontroller die ingesteld moet worden
     */
    public void setDomeinController(DomeinController dc);

    /**
     * Deze methode wordt in de klasse die de interface implementeren gebruikt om de
     * taal in te stellen
     * 
     * @param taal De taal die ingesteld moet worden
     */
    public void setTaal(Talen taal);

    /**
     * Deze methode wordt in de klasse die de interface implementeren gebruikt om
     * alle nodige methodes op te roepen en attributen in te stellen wanneer een
     * scherm voor het eerst wordt aangeroepen
     */
    public void initScherm();

    /**
     * Deze methode wordt in de klasse die de interface implementeren gebruikt om
     * textfields te legen
     */
    public void clearTextFields();
}
