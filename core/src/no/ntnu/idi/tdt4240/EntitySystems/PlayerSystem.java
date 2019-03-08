package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.tdt4240.Components.Player;


public class PlayerSystem {
    ImmutableArray<Entity> entities;
    public PlayerSystem(){
        super();
    }
    public void addedToEngine(Engine engine){ //called when this system is added to the engine
        entities = engine.getEntitiesFor(Family.all(Player.class).get());
    }
}
