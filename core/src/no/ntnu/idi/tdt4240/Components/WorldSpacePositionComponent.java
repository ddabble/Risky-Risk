package no.ntnu.idi.tdt4240.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class WorldSpacePositionComponent implements Component {

    public Vector2 position;

    public WorldSpacePositionComponent(Vector2 position) {
        this.position = position;
    }
}
