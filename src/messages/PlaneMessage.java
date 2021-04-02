package messages;

import BESA.Kernel.Agent.Event.DataBESA;
import avion.PlaneMessageType;

public class PlaneMessage extends DataBESA {

    private PlaneMessageType type;
    private String metaContent;
    private String gateId;
    private String runwayId;

    public PlaneMessage(PlaneMessageType type, String metaContent) {
        this.type = type;
        this.metaContent = metaContent;
    }

    public PlaneMessage(PlaneMessageType type) {
        this.type = type;
    }

    public PlaneMessage(){}

    public void setType(PlaneMessageType type) {
        this.type = type;
    }

    public PlaneMessageType getType() {
        return type;
    }

    public String getMetaContent() {
        return metaContent;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getRunwayId() {
        return runwayId;
    }

    public void setRunwayId(String runwayId) {
        this.runwayId = runwayId;
    }

    public void setMetaContent(String metaContent) {
        this.metaContent = metaContent;
    }
}
