package com.dungeonsanddishes.game;

import static Framework.TilemapActor.convertMapObjectToRectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.dungeonsanddishes.game.StartRoomLib.Oven;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Framework.BaseScreen;
import Framework.RoomTilemap;

public class LevelScreen extends BaseScreen
{
    Character character;
    RoomTilemap map;
    DungeonMap dungeonMap;
    CustomGame game;
    ArrayList<Rectangle> collisionRectangles;
    private Music music;

    public LevelScreen(CustomGame game){
        super();
        this.game=game;
    }
    public boolean scrolled(float a, float b){
        return true;
    }

    /**
     *
     * setup the structure of the dungeon and initialize start room and character
     */
    public void initialize() 
    {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("sounds/level-music.ogg"));
        dungeonMap = new DungeonMap(new RandomWalker(new DungeonRoomRepository(1, 7)),mainStage );
        dungeonMap.createDungeon();
        DungeonRoomMeta room = dungeonMap.getCurrentRoom();
        map=(RoomTilemap) room.dungeonRoom.map_layout;
        //map = new RoomTilemap("rooms/start_room.tmx");
        //map.setRoom(mainStage);

        character = new Character(0,0, mainStage,6);
        character.displayHealth(uiStage,30,Gdx.graphics.getHeight() - 50);
        character.displayRecipe(uiStage, 150, Gdx.graphics.getHeight() - 50);
        ArrayList<MapObject> spawn_point = map.getRectangleList("spawn_point");
        character.centerAtPosition((float)spawn_point.get(0).getProperties().get("x"),(float)spawn_point.get(0).getProperties().get("y"));
        character.setWorldBounds(Gdx.graphics.getWidth() - 350, Gdx.graphics.getHeight() - 200); // Hardcoded since they never change.
        character.setMovementStragety(new BasicMovement(character));
        music.setVolume(0.05f);
        music.setLooping(true);
        music.play();
        character.setMainItem(new Item(0,0,mainStage));
    }

    public void update(float dt)
    {
        if(!character.isDead()){

            for (MapObject obj:map.getCustomRectangleList("Collidable")){
                if ((boolean)obj.getProperties().get("Collidable")) {
                    character.preventOverlapWithObject( convertMapObjectToRectangle(obj));
                }
            }

            if(character.movement != null){
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
            character.mainItem.centerAtActorMainItem(character);

            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                character.incrementChili();
                character.incrementRice();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { swingKnife(); }

            dungeonMap.getCurrentRoom().dungeonRoom.update(dt,character);

        if (character.bossSlain()) {
                game.setScreen(new WinnerStoryScreen(new VictoryScreen(this.game)));
        }
        } else {
            //game over
            Logger.getGlobal().log(Level.WARNING,"GAME OVER!!!!");
            this.dispose();
            music.stop();
            game.setScreen( new GameOverScreen(this.game));
        }
        dungeonMap.getCurrentRoom().dungeonRoom.update(dt,character);
    }

    public Rectangle convertMapObjectToRectangle(MapObject obj) {
        MapProperties props = obj.getProperties();
        return new Rectangle( (float)props.get("x"), (float)props.get("y"), ((float)props.get("width")), ((float)props.get("height")));
    }

    public void swingKnife()
    {
        character.setSpeed(0);
        float facingAngle = character.CharAngle;
        float knifeArc = 180;
        if (facingAngle == 0)
            character.mainItem.addAction( Actions.sequence(
                    Actions.rotateBy(-knifeArc, 0.15f),
                    Actions.rotateBy(knifeArc, 0.15f))
            );
        else if (facingAngle == 90)
            character.mainItem.addAction( Actions.sequence(
                    Actions.rotateBy(-knifeArc, 0.15f),
                    Actions.rotateBy(knifeArc, 0.15f))
            );
        else if (facingAngle == 180)
            character.mainItem.addAction( Actions.sequence(
                    Actions.rotateBy(knifeArc, 0.15f),
                    Actions.rotateBy(-knifeArc, 0.15f))
            );
        else // facingAngle == 270
            character.mainItem.addAction( Actions.sequence(
                    Actions.rotateBy(knifeArc, 0.15f),
                    Actions.rotateBy(-knifeArc, 0.15f))
            );
    }
}