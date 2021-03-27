package avion;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import messages.MessageGeneric;

public class PlaneGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        MessageGeneric mensaje = (MessageGeneric) eventBESA.getData();
        switch(mensaje.getContent()){
            case "Hola":
                PlaneState estado = (PlaneState) this.agent.getState();
                ReportBESA.info("Hola soy "+estado.getAlias());
                break;
            default:
                ReportBESA.warn("No te entiendo");
        }
    }
}
