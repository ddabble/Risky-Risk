package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;


public class BoardSystem extends EntitySystem {
    ImmutableArray<Entity> entities;
    public BoardSystem(){
        super();
    }

    public void addedToEngine(Engine engine){ //called when this system is added to the engine

    }
}
