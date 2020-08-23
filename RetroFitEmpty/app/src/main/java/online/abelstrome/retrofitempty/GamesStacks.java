//-----------------------------------online.abelstrome.retrofitempty.Stack.java-----------------------------------

package online.abelstrome.retrofitempty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GamesStacks {

    @SerializedName("Stack")
    @Expose
    private Integer stack;
    @SerializedName("Height")
    @Expose
    private String height;
    @SerializedName("Top")
    @Expose
    private String top;

    public Integer getStack() {
        return stack;
    }

    public void setStack(Integer stack) {
        this.stack = stack;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

}
