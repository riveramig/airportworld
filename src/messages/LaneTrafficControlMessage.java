package messages;

import BESA.Kernel.Agent.Event.DataBESA;
import ltc.LaneTrafficControlMessageType;

public class LaneTrafficControlMessage extends DataBESA {
    private LaneTrafficControlMessageType type;
    private String planeId;
    private String runaway;
    private String gate;
    private String metaInfo;

    public LaneTrafficControlMessage(LaneTrafficControlMessageType type, String planeId) {
        this.type = type;
        this.planeId = planeId;
    }

    public LaneTrafficControlMessage(LaneTrafficControlMessageType type) {
        this.type = type;
    }

    public String getRunaway() {
        return runaway;
    }

    public void setRunaway(String runaway) {
        this.runaway = runaway;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public LaneTrafficControlMessageType getType() {
        return type;
    }

    public void setType(LaneTrafficControlMessageType type) {
        this.type = type;
    }

    public String getPlaneId() {
        return planeId;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }
}
