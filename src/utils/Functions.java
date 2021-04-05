package utils;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import messages.WatcherMessage;
import watcher.WatcherGuard;

import java.util.Random;

public class Functions {

    public static synchronized long getRandomCabin(CabinEnum cabin){
        Random r = new Random();
        long start = 0;
        long end = 0;
        switch (cabin) {
            case LARGE:
                start = 1000;
                end = 2000;
                return start  + ((long)(Math.random() * (end - start)) + start);
            case SMALL:
                start = 0;
                end = 1000;
                return start  + ((long)(Math.random() * (end - start)) + start);
            default:
                return 0L;
        }
    }

    public static synchronized void addDelay(long delay){
        AdmBESA adm = AdmBESA.getInstance();
        try {
            AgHandlerBESA ah = adm.getHandlerByAlias("watcher");
            WatcherMessage message = new WatcherMessage(delay);
            EventBESA event = new EventBESA(WatcherGuard.class.getName(),message);
            ah.sendEvent(event);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }
}
