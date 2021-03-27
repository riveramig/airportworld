package atc;

import BESA.Kernel.Agent.StateBESA;
import ltc.LaneTrafficControlAgent;
import misc.Runway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirTrafficControlState extends StateBESA {

    private List<Runway> runaways = new ArrayList<>();
    private LaneTrafficControlAgent ltc;

    public AirTrafficControlState(LaneTrafficControlAgent ltc, Runway... runwaysFill) {
        this.ltc = ltc;
        Arrays.stream(runwaysFill).forEach(runaway -> this.runaways.add(runaway));
    }

    public AirTrafficControlState(Runway... runwaysFill) {
        Arrays.stream(runwaysFill).forEach(runaway -> this.runaways.add(runaway));
    }

    public void addRunway (Runway runway) {
        this.runaways.add(runway);
    }

    public List<Runway> getRunaways() {
        return runaways;
    }

    public LaneTrafficControlAgent getLtc() {
        return ltc;
    }

    public void setLtc(LaneTrafficControlAgent ltc) {
        this.ltc = ltc;
    }

    @Override
    public String toString() {
        return "AirTrafficControlState{" +
                "runaways=" + runaways +
                ", ltc=" + ltc +
                '}';
    }
}
