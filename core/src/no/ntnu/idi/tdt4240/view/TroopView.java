package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class TroopView extends ApplicationAdapter {
    public static final Color TEXT_COLOR = new Color(0xFFFFFFFF);

    private final GameController gameController;

    private SpriteBatch batch;
    private Map<Territory, Sprite> circleSpriteMap;

    private Map<Territory, TextField> circleTextMap;
    private Stage stage;

    private Sprite circleSelectSprite;

    public TroopView(GameController gameController) {
        this.gameController = gameController;
    }

    // TODO: maybe change this to take no parameters and just loop over all territories?
    public void onTerritoryChangeNumTroops(Territory territory) {
        circleTextMap.get(territory).setText(String.valueOf(territory.getNumTroops()));
    }

    public void onSelectTerritory(Territory territory) {
        if (territory != null) {
            Vector2 circlePos = territory.getTroopCircleVector();
            circleSelectSprite.setOriginBasedPosition(circlePos.x, circlePos.y);
        }
        gameController.setSelectedTerritory(territory);
    }

    public void create(TerritoryMap territoryMap, Texture circleTexture, Texture circleSelectTexture) {
        batch = new SpriteBatch();

        List<Territory> territories = territoryMap.getAllTerritories();
        createCircleSprites(territories, circleTexture, circleSelectTexture);
        createCircleText(territories);
    }

    private void createCircleSprites(List<Territory> territories, Texture circleTexture, Texture circleSelectTexture) {
        circleSpriteMap = new HashMap<>();
        for (Territory territory : territories) {
            Vector2 circlePos = territory.getTroopCircleVector();
            Sprite sprite = new Sprite(circleTexture);
            sprite.setOriginBasedPosition(circlePos.x, circlePos.y);
            circleSpriteMap.put(territory, sprite);
        }

        circleSelectSprite = new Sprite(circleSelectTexture);
    }

    private void createCircleText(List<Territory> territories) {
        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = new BitmapFont();
        textStyle.fontColor = TEXT_COLOR;

        circleTextMap = new HashMap<>();
        stage = new Stage();
        for (Territory territory : territories) {
            TextField textField = new TextField(String.valueOf(territory.getNumTroops()), textStyle);
            Vector2 circlePos = territory.getTroopCircleVector();
            textField.setAlignment(Align.center);
            textField.setPosition(circlePos.x, circlePos.y, Align.center);

            circleTextMap.put(territory, textField);
            stage.addActor(textField);
        }
    }

    @Override
    public void render() {
        batch.begin();
        for (Sprite sprite : circleSpriteMap.values())
            sprite.draw(batch);

        if (gameController.getSelectedTerritory() != null)
            circleSelectSprite.draw(batch);
        batch.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
