package no.ntnu.idi.tdt4240.Models;

import com.badlogic.ashley.core.Engine;

import no.ntnu.idi.tdt4240.EntitySystems.BoardSystem;
import no.ntnu.idi.tdt4240.EntitySystems.RenderSystem;

/**
 * Created by Oivind on 3/21/2019.
 */

public class GameModel {

    private Engine engine;
    public Engine getEngine() {
        return engine;
    }

    public GameModel() {
        engine = new Engine();
        RenderSystem rs = new RenderSystem();
        BoardSystem bs = new BoardSystem();

        engine.addSystem(rs);
        engine.addSystem(bs);
    }

}
