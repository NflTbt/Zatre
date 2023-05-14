package main;

import cui.ZatreCuiApp;
import domein.DomeinController;

/**
 * Main applicatie om software op te starten.
 * 
 * @author Naoufal Thabet
 *
 */
public class StartUp {

    public static void main(String[] args) {
	new ZatreCuiApp(new DomeinController()).start();
    }
}
