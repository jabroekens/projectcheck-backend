package nl.han.oose.buizerd.projectcheck_backend;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/room")
public class RoomEndPoint {



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{roomcode}/join/{username}")
    public Response joinRoom(@PathParam("roomcode") String roomCode,@PathParam("username") String username){
      Room roomToJoin =  RoomFactory.getRoom(roomCode);
        roomToJoin.joinRoom(username);
        return Response.ok().build();






    }





}
