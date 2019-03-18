package no.ntnu.idi.tdt4240;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.idi.tdt4240.Components.SpriteComponent;
import no.ntnu.idi.tdt4240.EntitySystems.RenderSystem;

public class RiskyRisk extends ApplicationAdapter {
    static Engine engine;
    SpriteBatch batch;
    Texture img;

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        engine = new Engine();
        engine.addSystem(new RenderSystem());
        Entity entity = new Entity();
        Sprite sprite = new Sprite(img);
        sprite.setPosition(200, 100);
        entity.add(new SpriteComponent(sprite));
        engine.addEntity(entity);
    }

    @Override
    public void render () {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.begin();
        //batch.draw(img, 0, 0);
        //batch.end();
        RenderSystem renderSystem = engine.getSystem(RenderSystem.class);
        renderSystem.update();

    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}
