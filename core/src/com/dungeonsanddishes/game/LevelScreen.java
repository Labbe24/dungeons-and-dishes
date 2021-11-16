package com.dungeonsanddishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Random;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;


import Framework.BaseScreen;
import Framework.RoomTilemap;

public class LevelScreen extends BaseScreen
{
    Character character;
    Enemy enemy;
    TilemapActor map;
    Random random;
    Seeker seeker;
    RoomTilemap map;
    DungeonMap dungeonMap;
    ArrayList<Rectangle> collisionRectangles;

    public boolean scrolled(float a, float b){
        return true;
    }

    /**
     *
     * setup the structure of the dungeon and initialize start room and character
     */
    public void initialize() 
    {

        random = new Random();
        map = new TilemapActor("rooms/start_room.tmx",mainStage);
        character = new Character(0,0, mainStage);
        enemy = new Enemy(random.nextInt(100), random.nextInt(100), mainStage);
        seeker = new Seeker(character, enemy, 400);
        dungeonMap = new DungeonMap(new RandomWalker(new DungeonRoomRepository(1, 7)),mainStage );
        dungeonMap.createDungeon();
        DungeonRoomMeta room = dungeonMap.getCurrentRoom();
        map=(RoomTilemap) room.dungeonRoom.map_layout;
        //map = new RoomTilemap("rooms/start_room.tmx");
        //map.setRoom(mainStage);

        character = new Character(0,0, mainStage);
        character.setMovementStragety(new BasicMovement(character));
        ArrayList<MapObject> spawn_point = map.getRectangleList("spawn_point");
        character.centerAtPosition((float)spawn_point.get(0).getProperties().get("x"),(float)spawn_point.get(0).getProperties().get("y"));
        character.setWorldBounds(1550, 765); // Hardcoded since they never change.

    }

    public void update(float dt)
    {
        if(character.movement != null) {
            character.movement.handleMovement();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {

            for(Door door:dungeonMap.currentRoom.dungeonRoom.map_layout.getDoors()){
                if(character.isWithinDistance(20,door)){
                    dungeonMap.doorEntered(door.getDirection(),character);
                    break;
                }
            }
        }


        // Reset on 'R'  key
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            Reset();
        }

        seeker.Seek();

        dungeonMap.getCurrentRoom().dungeonRoom.update(dt,character);
    }
            //check if interactible nearby
            //if interactible is door
            //call map.DoorEntered(door.getProperty("direction"))


    public Rectangle convertMapObjectToRectangle(MapObject obj) {
        MapProperties props = obj.getProperties();
        return new Rectangle( (float)props.get("x"), (float)props.get("y"), ((float)props.get("width")), ((float)props.get("height")));
    }

    private void Reset() {
        character.setPosition(500, 500);
        enemy.setPosition(300, 300);
    }


}