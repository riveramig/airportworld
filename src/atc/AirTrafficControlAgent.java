package atc;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import ltc.LaneTrafficControlAgent;

public class AirTrafficControlAgent extends AgentBESA {

    private LaneTrafficControlAgent ltcAgent;

    public AirTrafficControlAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernellAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
        AirTrafficControlState atcState = (AirTrafficControlState) this.getState();
        atcState.setLtc(this.ltcAgent);
        atcState.setAlias(this.getAlias());
    }

    @Override
    public void shutdownAgent() {

    }

    public void setLtcAgent(LaneTrafficControlAgent ltcAgent) {
        this.ltcAgent = ltcAgent;
    }

}
