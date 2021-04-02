package misc;

import atc.AirTrafficControlAgent;
import ltc.LaneTrafficControlAgent;

import java.util.Arrays;

public class Airport {

    private String name;
    private AirTrafficControlAgent atcAgent;
    private LaneTrafficControlAgent ltcAgent;
    private Runway[] runaways;
    private Gate[] gates;


    public Airport(String name, AirTrafficControlAgent atcAgent, LaneTrafficControlAgent ltcAgent, Runway[] runways, Gate[] gates) {
        this.name = name;
        this.atcAgent = atcAgent;
        this.ltcAgent = ltcAgent;
        this.runaways = runways;
        this.gates = gates;
    }

    public Airport(String name, AirTrafficControlAgent atcAgent, LaneTrafficControlAgent ltcAgent) {
        this.name = name;
        this.atcAgent = atcAgent;
        this.ltcAgent = ltcAgent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AirTrafficControlAgent getAtcAgent() {
        return atcAgent;
    }

    public Runway[] getRunaways() {
        return runaways;
    }

    public void setRunaways(Runway[] runaways) {
        this.runaways = runaways;
    }

    public Gate[] getGates() {
        return gates;
    }

    public void setGates(Gate[] gates) {
        this.gates = gates;
    }

    public void setAtcAgent(AirTrafficControlAgent atcAgent) {
        this.atcAgent = atcAgent;
    }

    public LaneTrafficControlAgent getLtcAgent() {
        return ltcAgent;
    }

    public void setLtcAgent(LaneTrafficControlAgent ltcAgent) {
        this.ltcAgent = ltcAgent;
    }

    public Runway findRunwayById(String runwayId) {
        return Arrays.stream(this.runaways).filter(runway -> runway.getRunawayId().equalsIgnoreCase(runwayId)).findAny().orElse(null);
    }
    public Gate findGateByName(String gateId) {
        return Arrays.stream(this.gates).filter(gate -> gate.getGateId().equalsIgnoreCase(gateId)).findAny().orElse(null);
    }
}
