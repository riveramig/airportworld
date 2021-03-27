package messages;

import BESA.Kernel.Agent.Event.DataBESA;

public class MessageGeneric extends DataBESA {

    private String content;

    public MessageGeneric(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
