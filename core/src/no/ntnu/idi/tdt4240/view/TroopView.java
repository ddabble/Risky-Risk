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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import no.ntnu.idi.tdt4240.model.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class TroopView extends ApplicationAdapter {
    public static final Color TEXT_COLOR = new Color(0xFFFFFFFF);

    private final TerritoryMap territoryMap;

    private SpriteBatch batch;
    private Texture circleTexture;
    private Map<Territory, Sprite> circleSpriteMap;

    private Map<Territory, TextField> circleTextMap;
    private Stage stage;

    private Texture circleSelectTexture;
    private Sprite circleSelectSprite;
    private Territory selectedTerritory;

    public TroopView(TerritoryMap territoryMap) {
        this.territoryMap = territoryMap;
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
        selectedTerritory = territory;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        Collection<Territory> territories = territoryMap.getIDmap().values();
        createCircleSprites(territories);
        createCircleText(territories);
    }

    private void createCircleSprites(Collection<Territory> territories) {
        circleTexture = new Texture("map/troop_circle.png");

        circleSpriteMap = new HashMap<>();
        for (Territory territory : territories) {
            Vector2 circlePos = territory.getTroopCircleVector();
            Sprite sprite = new Sprite(circleTexture);
            sprite.setOriginBasedPosition(circlePos.x, circlePos.y);
            circleSpriteMap.put(territory, sprite);
        }

        circleSelectTexture = new Texture("map/troop_circle_select.png");
        circleSelectSprite = new Sprite(circleSelectTexture);
    }

    private void createCircleText(Collection<Territory> territories) {
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

        if (selectedTerritory != null)
            circleSelectSprite.draw(batch);
        batch.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        circleSelectTexture.dispose();
        circleTexture.dispose();
        batch.dispose();
    }
}
