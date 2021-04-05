package messages;

import BESA.Kernel.Agent.Event.DataBESA;
import atc.AirTrafficControlType;

public class AirTrafficControlMessage extends DataBESA {

    private AirTrafficControlType type;
    private String planeId;
    private String runwayId;
    private String gateId;

    public AirTrafficControlMessage(AirTrafficControlType type, String planeId, String runwayId) {
        this.type = type;
        this.planeId = planeId;
        this.runwayId = runwayId;
    }

    public AirTrafficControlMessage(AirTrafficControlType type) {
        this.type = type;
    }

    public AirTrafficControlType getType() {
        return type;
    }

    public void setType(AirTrafficControlType type) {
        this.type = type;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getRunwayId() {
        return runwayId;
    }

    public void setRunwayId(String runwayId) {
        this.runwayId = runwayId;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    @Override
    public String toString() {
        return "AirTrafficControlMessage{" +
                "type=" + type +
                ", planeId='" + planeId + '\'' +
                ", runwayId='" + runwayId + '\'' +
                '}';
    }
}
