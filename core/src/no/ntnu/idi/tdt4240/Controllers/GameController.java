package no.ntnu.idi.tdt4240.Controllers;

import no.ntnu.idi.tdt4240.Models.GameModel;

/**
 * Created by Oivind on 3/21/2019.
 */

public class GameController {

    private GameViewer viewer;
    private GameModel model;

    public GameController(GameViewer viewer, GameModel model) {
        this.viewer = viewer;
        this.model = model;
    }

    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        viewer.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }

    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    /*
    The RenderSystem should maybe just have its own camera? especially if
    the ECS handles its own inputs
    public void setCamera(Camera cam){
        //or w/e idk
        model.renderSystem.setCamera(cam);
    } */
}
