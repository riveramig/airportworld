package avion;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import misc.Itinerary;

import java.util.Stack;

public class PlaneAgent extends AgentBESA {

    private Stack<Itinerary> planeItinerary = new Stack<>();

    public PlaneAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernellAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
        PlaneState planeState = (PlaneState) this.getState();
        planeState.setItinerary(this.planeItinerary);
        planeState.setAlias(this.getAlias());
    }

    @Override
    public void shutdownAgent() {

    }

    public void addItinerary(Itinerary i) {
        this.planeItinerary.push(i);
    }

}
