package com.dungeonsanddishes.game;

import java.util.Random;

public class RandomWalker implements IDungeonGenerator {

    private Random random = new Random();
    private int [][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    //private RoomRepo roomRepo_;

    //public RandomWalker(RoomRepo roomRepo){
        //roomRepo_ = roomRepo;
    //}

    /**
     *
     * @param numberOfDungeons --.
     * @param numberOfDungeonTunnels --.
     * @param tunnelLength --.
     * @param roomNames --.
     */
    public DungeonRoomMeta[][] createDungeonMap(int numberOfDungeons,
                                                int numberOfDungeonTunnels,
                                                int tunnelLength,
                                                String[] roomNames){
        int dimensions = 20;
        int startRow = 9;
        int startColumn = 9;

        // create dimensions x dimensions 2D-array
        DungeonRoomMeta[][] dungeonMap = create2DArray(dimensions);

        for(int i = 0; i < numberOfDungeons; i++)
        {
            dungeonMap = createDungeonTunnel(dungeonMap, roomNames[i], dimensions, directions[i], numberOfDungeonTunnels, tunnelLength, startRow, startColumn);
        }

        return dungeonMap;
    }

    /**
     *
     * @param dungeonMap --.
     * @param roomType --.
     * @param dimensions --.
     * @param startDirection --.
     * @param numberOfTunnels --.
     * @param tunnelLength --.
     * @param startRow --.
     * @param startColumn --.
     */

    private DungeonRoomMeta[][] createDungeonTunnel(DungeonRoomMeta [][] dungeonMap,
                                                    String roomType,
                                                    int dimensions,
                                                    int[] startDirection,
                                                    int numberOfTunnels,
                                                    int tunnelLength,
                                                    int startRow,
                                                    int startColumn)
    {
        int tunnelCounter = 0;
        // place the random walker at a random cell
        int currentRow = startRow;
        int currentColumn = startColumn;

        // set start room, if not set
        if(dungeonMap[currentRow][currentColumn] == null)
        {
            dungeonMap[currentRow][currentColumn] = new DungeonRoomMeta(false, new DungeonRoom("S"));
        }

        int [] lastDirection = {0, 0};
        int [] randomDirection = {0, 0};

        while(tunnelCounter < numberOfTunnels)
        {
            // set a direction for random walker
            // direction should be perpendicular to last direction
            // unless last direction is only possible direction

            if(tunnelCounter == 0)
            {
                randomDirection = startDirection;
            }
            else
            {
                randomDirection = getNextDirection(directions, lastDirection);
                if(randomDirection == null) return dungeonMap; // null -> no possible directions
            }

            // set tunnel length counter
            int tunnelLengthCount = 0;

            // let random walker walk the random length
            while(tunnelLengthCount < tunnelLength)
            {
                // stop if random walker is going beyond array
                if((currentRow == 0 && randomDirection[0] == -1) ||
                        (currentColumn == 0 && randomDirection[1] == -1) ||
                        (currentRow == dimensions - 1 && randomDirection[0] == 1) ||
                        (currentColumn == dimensions - 1 && randomDirection[1] == 1))
                {
                    break;
                }

                // stop if random walker is going into another tunnel/room
                else if(dungeonMap[currentRow + randomDirection[0]][currentColumn + randomDirection[1]] != null)
                {
                    break;
                }

                // set cell where random walker is to true
                // and prepare random walker to walk to next cell
                else
                {
                    // first room is already set
                    if(dungeonMap[currentRow][currentColumn] == null)
                    {
                        dungeonMap[currentRow][currentColumn] = new DungeonRoomMeta(false, new DungeonRoom(roomType));
                        tunnelLengthCount++;
                    }

                    currentRow += randomDirection[0];
                    currentColumn += randomDirection[1];
                }
            }

            // don't update variables
            if(tunnelLengthCount > 0)
            {
                lastDirection = randomDirection;
                tunnelCounter++;
            }
        }
        return dungeonMap;

        /*
        - = false
        T = true;

        dungeonMap = [[-,-,-,-,-,-,-,-,-,-,-,-,-],
                      [-,-,-,-,-,-,-,T,-,-,-,-,-],
                      [-,-,-,-,-,-,-,T,-,-,-,-,-],
                      [-,-,-,-,-,-,-,T,T,-,-,-,-],
                      [-,-,-,-,-,-,-,-,T,T,T,-,-],
                      [-,-,-,-,T,T,T,-,-,-,T,-,-],
                      [-,-,-,-,T,T,T,-,T,T,T,-,-],
                      [-,-,-,-,T,-,-,-,T,-,-,-,-],
                      [-,-,-,-,T,-,-,-,T,T,-,-,-],
                      [-,-,-,-,T,T,T,T,T,T,-,-,-],
                      [-,-,-,-,-,-,-,-,-,-,-,-,-]]
        * */
    }

    public int getNumberOfRooms(DungeonRoomMeta [][] dungeonMap, String roomName){
        int numberOfRooms = 0;
        for(int i = 0; i < dungeonMap.length; i++)
        {
            for(int j = 0; j < dungeonMap.length; j++)
            {
                if(dungeonMap[i][j] != null
                        && dungeonMap[i][j].dungeonRoom.roomName == roomName){
                    numberOfRooms++;
                }
            }
        }
        return numberOfRooms;
    }

    private int[] getNextDirection(int[][] directions, int [] lastDirection){
        int[] randomDirection;
        do{
            randomDirection = directions[random.nextInt(directions.length)];
        } while((randomDirection[0] == -lastDirection[0] && randomDirection[1] == -lastDirection[1])
                || (randomDirection[0] == lastDirection[0] && randomDirection[1] == lastDirection[1]));

        return randomDirection;
    }

    private DungeonRoomMeta[][] create2DArray(int dimensions){
        DungeonRoomMeta [][] arr = new DungeonRoomMeta[dimensions][dimensions];
        for (int i = 0; i < dimensions; i++)
        {
            for (int j = 0; j < dimensions; j++)
            {
                arr[i][j] = null;
            }
        }
        return arr;
    }
}