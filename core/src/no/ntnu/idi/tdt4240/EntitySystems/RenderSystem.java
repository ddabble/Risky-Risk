package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.idi.tdt4240.Components.SpriteComponent;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private SpriteBatch batch;

    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class).get());
    }

    @Override
    public void update(){
        this.batch = new SpriteBatch();
        this.batch.begin();
        SpriteComponent spriteComp;
        for (Entity e : this.entities){
            spriteComp = sm.get(e);
            this.batch.draw(spriteComp.sprite,  spriteComp.sprite.getX(), spriteComp.sprite.getY());
        }
        this.batch.end();
    }

}
