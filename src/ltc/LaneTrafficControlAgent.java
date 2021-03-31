package ltc;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import atc.AirTrafficControlAgent;

public class LaneTrafficControlAgent extends AgentBESA {

    private AirTrafficControlAgent atcAgent;

    public LaneTrafficControlAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernellAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
        LaneTrafficControlState ltcState = (LaneTrafficControlState) this.getState();
        ltcState.setAtc(this.atcAgent);
        ltcState.setAlias(this.getAlias());
    }

    @Override
    public void shutdownAgent() {

    }

    public void setAtcAgent(AirTrafficControlAgent atcAgent) {
        this.atcAgent = atcAgent;
    }
}
