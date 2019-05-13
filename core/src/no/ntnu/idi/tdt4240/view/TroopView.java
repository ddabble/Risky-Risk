package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.observer.TroopObserver;
import no.ntnu.idi.tdt4240.presenter.BoardPresenter;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class TroopView extends AbstractView implements TroopObserver {
    private static final float CIRCLE_SIZE_MAP_RATIO = 1 / 40f;
    public static final Color TEXT_COLOR = new Color(0xFFFFFFFF);

    private final BoardView boardView;
    private final OrthographicCamera camera;

    private SpriteBatch batch;
    private Map<Territory, Sprite> circleSpriteMap;

    private Map<Territory, TextField> circleTextMap;
    private Map<Actor, Territory> textField_territoryMap;
    private Stage stage;

    private Sprite circleSelectSprite;

    private Territory selectedTerritory;

    public TroopView(BoardView boardView, OrthographicCamera camera) {
        BoardPresenter.addObserver(this);
        PhasePresenter.addObserver(this);
        this.boardView = boardView;
        this.camera = camera;
    }

    @Override
    public void onMapMoved() {
        for (Sprite sprite : circleSpriteMap.values())
            sprite.setScale(camera.zoom);

        for (Actor textField : stage.getActors()) {
            Territory territory = textField_territoryMap.get(textField);
            Vector2 screenPos = boardView.screenPosRelativeToMap(territory.getTroopCircleVector());
            textField.setPosition(screenPos.x, screenPos.y, Align.center);
        }
    }

    @Override
    public void onSelectTerritory(Territory territory) {
        selectedTerritory = territory;

        if (territory != null) {
            Vector2 circlePos = boardView.troopCirclePosToWorldPos(territory.getTroopCircleVector());
            circleSelectSprite.setOriginBasedPosition(circlePos.x, circlePos.y);
        }
    }

    @Override
    public void onTerritoryChangeNumTroops(Territory territory) {
        circleTextMap.get(territory).setText(String.valueOf(territory.getNumTroops()));
    }

    @Override
    public void create(TerritoryMap territoryMap, Texture circleTexture, Texture circleSelectTexture) {
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
            setSizeOfSprite(sprite);
            sprite.setOriginBasedPosition(circlePos.x, circlePos.y);
            circleSpriteMap.put(territory, sprite);
        }

        circleSelectSprite = new Sprite(circleSelectTexture);
        setSizeOfSprite(circleSelectSprite);
    }

    private void setSizeOfSprite(Sprite sprite) {
        final float spriteWidth = GameView.getWorldWidth() * CIRCLE_SIZE_MAP_RATIO;
        final float spriteHeight = spriteWidth;
        sprite.setSize(spriteWidth, spriteHeight);
        sprite.setOriginCenter();
    }

    private void createCircleText(List<Territory> territories) {
        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = new BitmapFont();
        textStyle.fontColor = TEXT_COLOR;

        circleTextMap = new HashMap<>();
        textField_territoryMap = new HashMap<>();
//        stage = new Stage(new FitViewport(GameView.getWorldWidth(), GameView.getWorldHeight(), camera));
        stage = new Stage();
        for (Territory territory : territories) {
            TextField textField = new TextField(String.valueOf(territory.getNumTroops()), textStyle);
            Vector2 circlePos = boardView.screenPosRelativeToMap(territory.getTroopCircleVector());
            textField.setPosition(circlePos.x-(textField.getWidth()/2), circlePos.y-(textField.getHeight()/2), Align.center);
            textField.setAlignment(Align.center);

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
        for (Map.Entry<Actor, Territory> map : textField_territoryMap.entrySet()){
            Vector2 worldPos = map.getValue().getTroopCircleVector();
            Vector3 screenPos = camera.project(new Vector3(worldPos.x,worldPos.y,0));
            map.getKey().setPosition(screenPos.x - (map.getKey().getWidth()/2), screenPos.y - (map.getKey().getHeight()/2));
        }
        stage.draw();
    }

    @Override
    public void dispose() {
//        for (TextField textField : circleTextMap.values())
//            textField.clear();
        stage.dispose();
        batch.dispose();
    }
}
