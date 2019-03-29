package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.Iterator;

import no.ntnu.idi.tdt4240.Components.TeamComponent;
import no.ntnu.idi.tdt4240.TagComponents.Player;


public class TeamSystem extends EntitySystem {
    ImmutableArray<Entity> entities;


    Entity teamsTurn; // gets updated manually by entities.iterator.next()



    public TeamSystem(){
        super();
    }
    public void addedToEngine(Engine engine){ //called when this system is added to the engine
        entities = engine.getEntitiesFor(Family.all(TeamComponent.class).get());
    }

    @Override
    public void update(float deltaTime){
        //let the team pick
    };


    private void nextTeamsTurn(){
        Iterator i = entities.iterator();
        if(i.hasNext())
            i.next();
    }

    /**
     * Sets the total score of a player
     * @param p     Player we want to change the score of
     * @param score Total score of the player
     */
    public void setScore(TeamComponent p, int score){
        p.score = score;
    }
    /**
     * Increase the score of the player by a specific number
     * @param team      Entity we want to change the score of
     * @param score     How much score to add
     */
    public void addScore(Entity team, int score){
        team.getComponent(TeamComponent.class).score += score;
    }


}
