package com.dungeonsanddishes.game.FishRoomLib;

import Framework.BaseActor;

public class WaterPool extends BaseActor {
    public boolean hasShark;

    public WaterPool(float x, float y) {
        super(x, y);
        this.loadTexture("FishRoom/water_pool.png");

    }
}
