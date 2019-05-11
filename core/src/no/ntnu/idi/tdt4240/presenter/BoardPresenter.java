package no.ntnu.idi.tdt4240.presenter;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.model.TroopModel;
import no.ntnu.idi.tdt4240.observer.BoardObserver;
import no.ntnu.idi.tdt4240.observer.TroopObserver;

public class BoardPresenter {
    public static final BoardPresenter INSTANCE = new BoardPresenter();

    private Collection<BoardObserver> boardObservers = new ArrayList<>();
    private Collection<TroopObserver> troopObservers = new ArrayList<>();

    private BoardPresenter() {}

    public void init() {
        BoardModel.INSTANCE.init();
        TroopModel.INSTANCE.init();

        for (BoardObserver observer : boardObservers) {
            observer.create(BoardModel.INSTANCE.getMapTexture(), TerritoryModel.getTerritoryMap().getAllTerritories(),
                            MultiplayerModel.INSTANCE.getPlayerID_colorMap());
        }
        for (TroopObserver observer : troopObservers)
            observer.create(TerritoryModel.getTerritoryMap(), TroopModel.INSTANCE.getCircleTexture(), TroopModel.INSTANCE.getCircleSelectTexture());
    }

    public void onBoardClicked(Vector2 mapPos) {
        Territory clickedTerritory = BoardModel.INSTANCE.getTerritory(mapPos);
        TroopModel.INSTANCE.onSelectTerritory(clickedTerritory);
        for (TroopObserver observer : troopObservers)
            observer.onSelectTerritory(clickedTerritory);

        if (clickedTerritory != null) {
            System.out.println(clickedTerritory.name);

            PhasePresenter.INSTANCE.onTerritoryClicked(clickedTerritory);
            for (TroopObserver observer : troopObservers)
                observer.onTerritoryChangeNumTroops(clickedTerritory);
        } else
            System.out.println("None");
    }

    public void onTerritoryChangedOwner(Territory territory) {
        for (BoardObserver observer : boardObservers)
            observer.onTerritoryChangeColor(territory, MultiplayerModel.INSTANCE.getPlayerColor(territory.getOwnerID()));
    }

    public void reset() {
        TroopModel.INSTANCE.reset();
        BoardModel.INSTANCE.reset();
    }

    public static void addObserver(BoardObserver observer) {
        INSTANCE.boardObservers.add(observer);
    }

    public static void addObserver(TroopObserver observer) {
        INSTANCE.troopObservers.add(observer);
    }
}
