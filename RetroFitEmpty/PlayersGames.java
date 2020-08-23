/*
-----------------------------------online.abelstrome.retrofitempty.PlayersGames.java-----------------------------------
for dummy/players/<player name> API
*/

package online.abelstrome.retrofitempty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayersGames {

    @SerializedName("GameRef")
    @Expose
    private String gameRef;
    @SerializedName("AreaSize")
    @Expose
    private String areaSize;
    @SerializedName("GameState")
    @Expose
    private String gameState;

    public String getGameRef() {
        return gameRef;
    }

    public void setGameRef(String gameRef) {
        this.gameRef = gameRef;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(String areaSize) {
        this.areaSize = areaSize;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

}
