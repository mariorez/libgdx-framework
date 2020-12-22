package org.seariver;

import org.seariver.screen.LevelScreen;

public class CustomGame extends BaseGame {

    public void create() {
        super.create();
        setActiveScreen(new LevelScreen());
    }
}
