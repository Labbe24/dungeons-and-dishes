package com.dungeonsanddishes.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DungeonRoomRepository {
    private int tunnel_room_type_count=0;
    private int end_room_type_count=4;

    public DungeonRoomRepository(int level,int seed) {
        _end_rooms = new ArrayList<DungeonRoom>();
        _tunnel_rooms = new ArrayList<DungeonRoom>();

        switch (seed % tunnel_room_type_count) {
            case 1:
                //create room type 1
            default:
                //throw?
        }
        _end_rooms.add(new KitchenRoom());//with parameters for room?
        _end_rooms.add(new MainIngredientRoom(seed));
        Collections.shuffle(_end_rooms, new Random(seed));
        Collections.shuffle(_tunnel_rooms, new Random(seed));

    }
    public DungeonRoom getTunnelRoom(){
        if(_tunnel_rooms.isEmpty()){
            //throw
        }
        DungeonRoom ret= _tunnel_rooms.get(0);
        _tunnel_rooms.remove(0);
        return ret;
    }
    public DungeonRoom getEndRoom(){
        if(_end_rooms.isEmpty()){
            //throw
        }
        DungeonRoom ret= _end_rooms.get(0);
        _end_rooms.remove(0);
        return ret;
    }
    private ArrayList<DungeonRoom> _end_rooms;
    private ArrayList<DungeonRoom> _tunnel_rooms;
}