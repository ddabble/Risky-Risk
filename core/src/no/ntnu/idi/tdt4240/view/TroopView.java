package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.model.data.Territory;
import no.ntnu.idi.tdt4240.observer.TroopObserver;
import no.ntnu.idi.tdt4240.presenter.BoardPresenter;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;
import no.ntnu.idi.tdt4240.util.TerritoryMap;
import no.ntnu.idi.tdt4240.util.Utils;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class TroopView extends ApplicationAdapter implements TroopObserver {
    private static final float CIRCLE_SIZE_MAP_RATIO = 1 / 30f;
    private static final Color CIRCLE_COLOR_LIGHT = new Color(0xCCCCCCFF);
    private static final Color CIRCLE_COLOR_DARK = new Color(0x808080FF);
    private static final Color TEXT_COLOR_LIGHT = new Color(Color.WHITE);
    private static final Color TEXT_COLOR_DARK = new Color(Color.DARK_GRAY);

    private final BoardView boardView;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Map<Territory, Sprite> circleSpriteMap;

    private Map<Territory, TextField> circleTextMap;
    private Map<Actor, Territory> textField_territoryMap;
    private Stage stage;

    private Sprite circleSelectSprite;

    private Territory selectedTerritory;

    public TroopView(BoardView boardView) {
        BoardPresenter.addObserver(this);
        PhasePresenter.addObserver(this);
        this.boardView = boardView;
    }

    @Override
    public void onMapRenderingChanged() {
        for (Sprite sprite : circleSpriteMap.values())
            sprite.setScale(camera.zoom);
        circleSelectSprite.setScale(camera.zoom);

        for (Actor textField : stage.getActors()) {
            Territory territory = textField_territoryMap.get(textField);
            Vector2 screenPos = boardView.screenPosRelativeToMap(territory.getTroopCircleVector());
            textField.setPosition(screenPos.x, screenPos.y, Align.center);
        }
    }

    @Override
    public void onSelectTerritory(Territory territory) {
        if (territory != null) {
            Vector2 circlePos = boardView.troopCirclePosToWorldPos(territory.getTroopCircleVector());
            circleSelectSprite.setOriginBasedPosition(circlePos.x, circlePos.y);
            circleSpriteMap.get(territory).setColor(CIRCLE_COLOR_LIGHT);
            circleTextMap.get(territory).setStyle(UIStyle.INSTANCE.createTroopTextStyle(TEXT_COLOR_DARK));
        }

        if (selectedTerritory != null && selectedTerritory != territory) {
            circleSpriteMap.get(selectedTerritory).setColor(CIRCLE_COLOR_DARK);
            circleTextMap.get(selectedTerritory).setStyle(UIStyle.INSTANCE.createTroopTextStyle(TEXT_COLOR_LIGHT));
        }
        selectedTerritory = territory;
    }

    @Override
    public void onTerritoryChangeNumTroops(Territory territory) {
        circleTextMap.get(territory).setText(String.valueOf(territory.getNumTroops()));
    }

    @Override
    public void create(OrthographicCamera camera, TerritoryMap territoryMap, Texture circleTexture, Texture circleSelectTexture) {
        this.camera = camera;
        batch = new SpriteBatch();

        List<Territory> territories = territoryMap.getAllTerritories();
        createCircleSprites(territories, circleTexture, circleSelectTexture);
        createCircleText(territories);
    }

    private void createCircleSprites(List<Territory> territories, Texture circleTexture, Texture circleSelectTexture) {
        circleSpriteMap = new HashMap<>();
        for (Territory territory : territories) {
            Vector2 circlePos = boardView.troopCirclePosToWorldPos(territory.getTroopCircleVector());
            Sprite sprite = new Sprite(circleTexture);
            Utils.setSizeOfSprite(sprite, CIRCLE_SIZE_MAP_RATIO);
            sprite.setOriginBasedPosition(circlePos.x, circlePos.y);
            sprite.setColor(CIRCLE_COLOR_DARK);
            circleSpriteMap.put(territory, sprite);
        }

        circleSelectSprite = new Sprite(circleSelectTexture);
        Utils.setSizeOfSprite(circleSelectSprite, CIRCLE_SIZE_MAP_RATIO);
        circleSelectSprite.setColor(CIRCLE_COLOR_DARK);
    }

    private void createCircleText(List<Territory> territories) {
        TextField.TextFieldStyle textStyle = UIStyle.INSTANCE.createTroopTextStyle(TEXT_COLOR_LIGHT);
        circleTextMap = new HashMap<>();
        textField_territoryMap = new HashMap<>();
        stage = new Stage();

        for (Territory territory : territories) {
            TextField textField = new TextField(String.valueOf(territory.getNumTroops()), textStyle);
            Vector2 circlePos = boardView.screenPosRelativeToMap(territory.getTroopCircleVector());
            textField.setAlignment(Align.center);
            textField.setPosition(circlePos.x, circlePos.y, Align.center);

            circleTextMap.put(territory, textField);
            textField_territoryMap.put(textField, territory);
            stage.addActor(textField);
        }
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
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
        selectedTerritory = null;
        stage.dispose();
        batch.dispose();
        super.dispose();
    }
}
