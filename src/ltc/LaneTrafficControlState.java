package ltc;

import BESA.Kernel.Agent.StateBESA;
import atc.AirTrafficControlAgent;
import misc.Gate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LaneTrafficControlState extends StateBESA {

    private AirTrafficControlAgent atc;
    private String alias;

    private List<Gate> gates = new ArrayList<>();

    public LaneTrafficControlState(AirTrafficControlAgent atc, Gate... gatesFill) {
        Arrays.stream(gatesFill).forEach(gate -> this.gates.add(gate));
        this.atc = atc;
    }

    public LaneTrafficControlState (Gate... gatesFill) {
        Arrays.stream(gatesFill).forEach(gate -> this.gates.add(gate));
    }

    public AirTrafficControlAgent getAtc() {
        return atc;
    }

    public void setAtc(AirTrafficControlAgent atc) {
        this.atc = atc;
    }

    public void addGate(Gate gate) {
        this.gates.add(gate);
    }

    public List<Gate> getGates() {
        return gates;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "LaneTrafficControlState{" +
                "atc=" + atc +
                ", alias='" + alias + '\'' +
                ", gates=" + gates +
                '}';
    }
}
