package free.videodownloader.whatsapp.status.statusdownloader.story.saver.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Edge implements Serializable {

    @SerializedName("node")
    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
