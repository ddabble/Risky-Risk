package no.ntnu.idi.tdt4240.Controllers;

/**
 * Created by Oivind on 3/21/2019.
 */
//interface for Views that want to use/view SettingsModel
    /*
    This might not be needed since the views create the controllers and pass
    the events to the controllers, this would be more usefull if other
    objects were creating the controllers
     */
public interface SettingsViewer {

    void setSetting1(String setting1);
}