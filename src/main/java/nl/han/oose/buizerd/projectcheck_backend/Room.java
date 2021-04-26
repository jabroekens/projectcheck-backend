package nl.han.oose.buizerd.projectcheck_backend;

import nl.han.oose.buizerd.projectcheck_backend.exceptions.InvalidUserException;

public class Room {
   private String roomcode;



   public Participant addPaticipant(String username){
    if(isUsernameValid(username)){
        Participant participantToJoin = new Participant(this);
        return participantToJoin;


    }
    else {
        throw new InvalidUserException();


    }



   }

   public boolean isUsernameValid(String username){

       return true;



   }

    public String getRoomcode() {
        return roomcode;
    }

    public void setRoomcode(String roomcode) {
        this.roomcode = roomcode;
    }
}
