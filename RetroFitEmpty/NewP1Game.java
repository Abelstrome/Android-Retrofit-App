/*
//-----------------------------------online.abelstrome.retrofitempty.NewP1Game.java-----------------------------------

package online.abelstrome.retrofitempty;

import java.util.List;
import java.util.Stack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewP1Game {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("GameRef")
    @Expose
    private String gameRef;
    @SerializedName("Player1Name")
    @Expose
    private String player1Name;
    @SerializedName("Player2Name")
    @Expose
    private String player2Name;
    @SerializedName("TurnCount")
    @Expose
    private String turnCount;
    @SerializedName("LastPlayer")
    @Expose
    private String lastPlayer;
    @SerializedName("AreaSize")
    @Expose
    private String areaSize;
    @SerializedName("AreaWidth")
    @Expose
    private String areaWidth;
    @SerializedName("Winner")
    @Expose
    private String winner;
    @SerializedName("Stacks")
    @Expose
    private List<GamesStacks> stacks = null;

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

    public String getGameRef() {
        return gameRef;
    }

    public void setGameRef(String gameRef) {
        this.gameRef = gameRef;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(String turnCount) {
        this.turnCount = turnCount;
    }

    public String getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(String lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(String areaSize) {
        this.areaSize = areaSize;
    }

    public String getAreaWidth() {
        return areaWidth;
    }

    public void setAreaWidth(String areaWidth) {
        this.areaWidth = areaWidth;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<GamesStacks> getStacks() {
        return stacks;
    }

    public void setStacks(List<GamesStacks> stacks) {
        this.stacks = stacks;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
*/
