package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class BoardSystem extends ApplicationAdapterEntitySystem {
    private ImmutableArray<Entity> entities;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture mapTexture;
    private Sprite testMap;

    public BoardSystem(OrthographicCamera camera) {
        super();
        this.camera = camera;
    }

    public void addedToEngine(Engine engine) {
        batch = new SpriteBatch();

        mapTexture = new Texture("risk_game_map.png");
        testMap = new Sprite(mapTexture);
//        testMap.setSize(mapTexture.getWidth() / 2f, mapTexture.getHeight() / 2f);
    }


    public void render(SpriteBatch batch) {
        testMap.draw(batch);
    }

    @Override
    public void dispose() {
        if (testMap.getTexture().getTextureData().disposePixmap())
            mapPixmap.dispose();
        mapTexture.dispose();
        batch.dispose();
    }
}
