/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.noisynarwhal.albrecht.square.Population;

/**
 * Benchmark evolution times
 *
 * @author lioudt
 */
public class Benchmark {

    /**
     * Run a number of evolutions and calculate average number of evolution
     * times in units of successes per second.
     *
     * @param args
     */
    public static void main(String[] args) {

        final int order = 9;
        final int populationSize = 200;
        final int numTrials = 500;

        System.gc();

        final List<Long> times = new ArrayList<>();

        for (int i = 0; i < numTrials; i++) {

            final long start = System.nanoTime();

            Population.evolve(order, populationSize);

            final long elapsed = System.nanoTime() - start;

            times.add(elapsed);

            final long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
            if (elapsedSecs > 0) {
                System.out.println(Integer.toString(i) + ": " + Long.toString(elapsedSecs) + " secs");
            } else {
                final long elapsedMs = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
                System.out.println(Integer.toString(i) + ": " + Long.toString(elapsedMs) + " ms");
            }

        }

        Collections.sort(times);

        final double mean = Benchmark.mean(times);
        final double sd = Benchmark.sd(times);

        /**
         * Trim the results to remove outliers
         */
        final double headCutOff = mean - (2 * sd);
        final double tailCutOff = mean + (2 * sd);

        while (times.get(0) < headCutOff) {
            times.remove(0);
        }
        while (times.get(times.size() - 1) > tailCutOff) {
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

    /**
     * Calculate standard deviation
     *
     * @param values
     * @return
     */
    private static double sd(Collection<Long> values) {

        final int len = values.size();

        double sum = 0.0;
        for (final double v : values) {
            sum += v;
        }

        final double mean = sum / len;

        double sd = 0.0;
        for (final double v : values) {
            sd += Math.pow(v - mean, 2);
        }

        return Math.sqrt(sd / len);
    }

    /**
     * Return the average
     *
     * @param values
     * @return
     */
    private static double mean(Collection<Long> values) {

        double sum = 0.0;

        for (final double v : values) {
            sum += v;
        }

        return sum / values.size();
    }

}
