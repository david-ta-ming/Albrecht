/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.noisynarwhal.albrecht.Population;

/**
 *
 * @author lioudt
 */
public class Benchmark {

    public static void main(String[] args) {

        final int order = 9;
        final int populationSize = 200;
        final int numTrials = 200;

        final List<Long> times = new ArrayList<>();

        for (int i = 0; i < numTrials; i++) {

            final long start = System.nanoTime();

            Population.evolve(order, populationSize);

            final long elapsed = System.nanoTime() - start;

            times.add(elapsed);

            System.out.println(Integer.toString(i) + ": " + Math.round(elapsed / (1000.0 * 1000.0)));
        }

        Collections.sort(times);

        final int trim = Math.round(times.size() / 20.0F);
        for (int i = 0; i < trim && !times.isEmpty(); i++) {
            times.remove(0);
            times.remove(times.size() - 1);
        }

        long sum = 0;
        for (final double time : times) {
            sum += time;
        }
        final long secs = TimeUnit.SECONDS.convert(sum, TimeUnit.NANOSECONDS);

        final double average = ((double) times.size()) / ((double) secs);
        System.out.println("Adjusted average: " + average);
    }

}
