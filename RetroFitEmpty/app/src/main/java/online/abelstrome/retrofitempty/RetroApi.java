package online.abelstrome.retrofitempty;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetroApi {

    @GET("games/{gameRef}")
    Call<Games> getGames(@Path("gameRef") String gameRef);

    @GET("players/{playerName}")
    Call<Players> getPlayers(@Path("playerName") String playerName);

    @POST("games")
    Call<Games> createNewP1Game(@Body Games newP1Game);

    @PATCH("games/{gameRef}")
    Call<GamesAddP2> addNewP2ToGame(@Path("gameRef") String gameRef, @Body GamesAddP2 addP2Game);

    @PATCH("games/{gameRef}/players/{playerName}")
    Call<GamesMakeMove> makeMove(@Path("gameRef") String gameRef, @Path ("playerName") String playerName, @Body GamesMakeMove moveGame);
    //Call<Games> makeMove(@Path("gameRef") String gameRef, @Path ("playerName") String playerName, @Body Move moveGame);
}
