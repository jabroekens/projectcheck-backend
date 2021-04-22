package nl.han.oose.buizerd.projectcheck_backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomFactory {

    private static final Map<String, Room> rooms = new HashMap<>();




    public static Room getRoom(String roomCode) {
        return rooms.get(roomCode);
    }

}
