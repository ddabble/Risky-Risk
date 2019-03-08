package no.ntnu.idi.tdt4240.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class ScreenSpacePositionComponent implements Component {
    public Vector2 position;

    public ScreenSpacePositionComponent(Vector2 position) {
        this.position = position;
    }
}
