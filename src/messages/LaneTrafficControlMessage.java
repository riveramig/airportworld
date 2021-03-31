package messages;

import BESA.Kernel.Agent.Event.DataBESA;
import ltc.LaneTrafficControlMessageType;

public class LaneTrafficControlMessage extends DataBESA {
    private LaneTrafficControlMessageType type;
    private String content;

    public LaneTrafficControlMessage(LaneTrafficControlMessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public LaneTrafficControlMessage(LaneTrafficControlMessageType type) {
        this.type = type;
    }

    public LaneTrafficControlMessageType getType() {
        return type;
    }

    public void setType(LaneTrafficControlMessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
