/*
-----------------------------------online.abelstrome.retrofitempty.Post.java-----------------------------------
for dummy/players/<player name> API
*/
package online.abelstrome.retrofitempty;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Players {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("PlayerName")
    @Expose
    private String playerName;
    @SerializedName("Games")
    @Expose
    private List<PlayersGames> games = null;

    @SerializedName("ErrorCode")
    @Expose
    private String errorCode;

    @SerializedName("ErrorMsg")
    @Expose
    private String errorMsg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<PlayersGames> getPlayersGames() {
        return games;
    }

    public void setPlayersGames(List<PlayersGames> games) {
        this.games = games;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
