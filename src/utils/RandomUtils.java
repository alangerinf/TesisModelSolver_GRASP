package utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils { 
    
    public static long nextLong(long topNumber) {
        long nextLong = ThreadLocalRandom.current().nextLong(topNumber);
        return nextLong;
    }
}
