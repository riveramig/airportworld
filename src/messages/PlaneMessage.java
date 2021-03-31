package messages;

import BESA.Kernel.Agent.Event.DataBESA;
import avion.PlaneMessageType;

public class PlaneMessage extends DataBESA {

    private PlaneMessageType type;
    private String content;

    public PlaneMessage(PlaneMessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public PlaneMessage(PlaneMessageType type) {
        this.type = type;
    }

    public void setType(PlaneMessageType type) {
        this.type = type;
    }

    public PlaneMessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
