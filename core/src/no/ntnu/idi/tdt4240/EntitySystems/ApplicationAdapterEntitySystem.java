package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ApplicationListener;

public abstract class ApplicationAdapterEntitySystem extends EntitySystem implements ApplicationListener {
	public ApplicationAdapterEntitySystem() {
		super();
	}

	public ApplicationAdapterEntitySystem(int priority) {
		super(priority);
	}

	@Override
	public void create() {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void render() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}
