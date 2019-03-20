package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.idi.tdt4240.Controllers.Controller;
import no.ntnu.idi.tdt4240.Views.MainMenuView;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {

    //DRY
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MainMenuView(this));
    }

    @Override
    public void render () {
        super.render();

    @Override
    public void dispose () {
        batch.dispose();
        font.dispose();
    }
}
