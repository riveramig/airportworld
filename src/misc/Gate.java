package misc;

import avion.PlaneAgent;

import java.util.UUID;

public class Gate {
    private boolean isOccupied;
    private String gateId;
    private PlaneAgent planeOnGate;
    private boolean isUsable;

    public Gate(String gateId){
        this.gateId = gateId;
        this.isUsable = true;
    }

    public Gate() {this.gateId = UUID.randomUUID().toString();}

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public PlaneAgent getPlaneOnGate() {
        return planeOnGate;
    }

    public void setPlaneOnGate(PlaneAgent planeOnGate) {
        this.planeOnGate = planeOnGate;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }
}
