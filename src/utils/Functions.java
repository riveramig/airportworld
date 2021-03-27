package utils;

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
}
