package ManyStream2;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        String[] routs = new String[1000];

        for (int i = 0; i < routs.length; i++) {
            routs[i] = generateRoute("RLRFR", 100);
        }

        List<Thread> threads = new ArrayList<>();

        for (String rout : routs) {
            Runnable logic = () -> {
                Integer amountR = 0;
                for (int i = 0; i < rout.length(); i++) {
                    if (rout.charAt(i) == 'R') {
                        amountR++;
                    }
                }
                System.out.println(rout.substring(0, 100) + " -> " + amountR);

                synchronized (amountR) {
                    if (sizeToFreq.containsKey(amountR)) {
                        sizeToFreq.put(amountR, sizeToFreq.get(amountR) + 1);
                    } else {
                        sizeToFreq.put(amountR, 1);
                    }
                }
            };
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        }

        int maxValue = 0;
        Integer maxKey = 0;
        for (Integer key : sizeToFreq.keySet()) {
            if (sizeToFreq.get(key) > maxValue) {
                maxValue = sizeToFreq.get(key);
                maxKey = key;
            }
        }
        System.out.println();
        System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + maxValue + " раз)");
        sizeToFreq.remove(maxKey);

        System.out.println("Другие размеры:");
        for (Integer key : sizeToFreq.keySet()) {
            int value = sizeToFreq.get(key);
            System.out.println("- " + key + " (" + value + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
