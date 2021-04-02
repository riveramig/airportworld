package misc;

import avion.PlaneAgent;

import java.util.UUID;

public class Runway {
    private boolean isOccupied;
    private String runawayId;
    private PlaneAgent agentOnRunaway;
    private boolean isUsable;

    public Runway() {
        this.runawayId = UUID.randomUUID().toString();
        this.isUsable =true;
        this.isOccupied =false;
    }

    public Runway(String id) {
        this.runawayId = id;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getRunawayId() {
        return runawayId;
    }

    public void setRunawayId(String runawayId) {
        this.runawayId = runawayId;
    }

    public PlaneAgent getAgentOnRunaway() {
        return agentOnRunaway;
    }

    public void setAgentOnRunaway(PlaneAgent agentOnRunaway) {
        this.agentOnRunaway = agentOnRunaway;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }

    @Override
    public String toString() {
        return "Runway{" +
                "isOccupied=" + isOccupied +
                ", runawayId='" + runawayId + '\'' +
                ", agentOnRunaway=" + agentOnRunaway +
                ", isUsable=" + isUsable +
                '}';
    }
}
