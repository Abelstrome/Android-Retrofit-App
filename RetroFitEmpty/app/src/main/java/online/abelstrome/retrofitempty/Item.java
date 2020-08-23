//-----------------------------------online.abelstrome.retrofitempty.Item.java-----------------------------------

package online.abelstrome.retrofitempty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {

    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("dataString")
    @Expose
    private String dataString;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

}
