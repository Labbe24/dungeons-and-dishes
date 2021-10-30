package com.dungeonsanddishes.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class DoorNorth extends Door{
    public DoorNorth(float x, float y, Stage s) {
        super(x, y, s);
        this.loadTexture("rooms/kitchen_tiles/door_top.png");
    }
}
