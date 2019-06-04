package no.ntnu.idi.tdt4240.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicController {
    enum GameMusic {
        MAIN_MENU_THEME("menutheme.mp3"),
        GAME_THEME("gametheme.mp3");

        public final FileHandle file;

        GameMusic(String filePath) {
            file = Gdx.files.internal(filePath);
        }
    }

    public static final MusicController INSTANCE = new MusicController();

    private GameMusic currentlyPlaying;
    private Music currentlyPlayingMusic;

    private MusicController() {}

    public void playMainMenuTheme() {
        playMusic(GameMusic.MAIN_MENU_THEME);
    }

    public void playGameTheme() {
        playMusic(GameMusic.GAME_THEME);
    }

    private void playMusic(GameMusic music) {
        if (currentlyPlaying != music) {
            if (currentlyPlayingMusic != null)
                currentlyPlayingMusic.dispose();

            currentlyPlayingMusic = Gdx.audio.newMusic(music.file);
            currentlyPlayingMusic.setLooping(true);
            currentlyPlaying = music;
        }

        currentlyPlayingMusic.play();
    }

    public static void dispose() {
        INSTANCE.currentlyPlayingMusic.dispose();
    }
}
