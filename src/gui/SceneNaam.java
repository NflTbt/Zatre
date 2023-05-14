package gui;

/**
 * 
 * @author Robin Verplancke
 *
 */

public final class SceneNaam {

    private static String sceneNaam;

    /**
     * Constructor: bewust niet geimplementeerd. Singleton klasse.
     */
    private SceneNaam() {
    }

    /**
     * Methode retourneert de huidige waarde van het attribuut sceneNaam
     * 
     * @return de waarde van het attribuut sceneNaam
     */
    public static String getSceneNaam() {
	return sceneNaam;
    }

    /**
     * Methode stelt het attribuut sceneNaam van de klasse SceneNaam in naar de
     * meegegeven parameter
     * 
     * @param naam De naam die je aan het attribuut sceneNaam wilt meegeven
     */
    public static void setSceneNaam(String naam) {
	SceneNaam.sceneNaam = naam;
    }
}
