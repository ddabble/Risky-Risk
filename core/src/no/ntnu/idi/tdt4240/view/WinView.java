package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.observer.WinObserver;
import no.ntnu.idi.tdt4240.presenter.WinPresenter;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class WinView extends ScreenAdapter implements WinObserver {
    private final RiskyRisk game;

    private Texture background;
    private Stage stage;
    private Table table;

    private BitmapFont buttonFont;

    public WinView(RiskyRisk game) {
        this.game = game;
        WinPresenter.addObserver(this);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(800, 400));
        background = new Texture("youwin.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        Gdx.input.setInputProcessor(stage);

        buttonFont = UIStyle.INSTANCE.createBoldFont(UIStyle.getStandardButtonFontSize());
        float heightRatio = stage.getHeight() / Gdx.graphics.getHeight();
        buttonFont.getData().setScale(heightRatio);
        Button winButton = UIStyle.INSTANCE.createTextButton("You won!", buttonFont);

        winButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(RiskyRisk.ScreenEnum.MAIN_MENU);
            }
        });
        table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        table.setPosition(0, 0);
        stage.addActor(table);
        table.add(winButton).width(150).height(50).pad(20);
        // Sign out
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        buttonFont.dispose();
        table.clear();
        stage.dispose();
        background.dispose();
        super.dispose();
    }
}
