package eu.tjago.apps.ivonatalkerapp;

import eu.tjago.apps.ivonatalkerapp.api.CreateSpeech;
import eu.tjago.apps.ivonatalkerapp.api.ListVoicesService;

/**
 * Created by Tomasz on 2015-12-24.
 */
public class IvonaTalkerApp {
    public static void main(String[] args) {
        System.out.println("Hello, I'm Ivona, we will have a talk, when you plug the API");
        System.out.println("Listing voices:");
        new ListVoicesService();
        new CreateSpeech();
    }
}
