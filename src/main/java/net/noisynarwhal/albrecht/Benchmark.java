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
import net.noisynarwhal.albrecht.square.Evolutions;
import net.noisynarwhal.albrecht.square.Magic;
import net.noisynarwhal.albrecht.square.Matrices;

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
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        final int order = 9;
        final int numTrials = 500;

        System.gc();

        final List<Long> times = new ArrayList<>();

        for (int i = 0; i < numTrials; i++) {

            final Magic magic;
            final long start = System.nanoTime();

            magic = Evolutions.evolve(order);

            final long elapsed = System.nanoTime() - start;

            if (!Matrices.isMagic(magic.getValues())) {
                System.out.println("Not magic: ");
                System.out.println(Matrices.print(magic.getValues()));
                break;
            }

            times.add(elapsed);

            final StringBuilder sb = new StringBuilder();

            final long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);

            if (elapsedSecs > 1) {
                
                sb.append(Long.toString(elapsedSecs));
                sb.append(" secs");
                
            } else {

                final long elapsedMs = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
                
                final long stars = (elapsedMs / 50) + 1;
                
                for (int j = 1; j <= stars; j++) {
                    if (j % 3 == 0) {
                        sb.append(Integer.toString(j));
                    } else {
                        sb.append('*');
                    }
                }

            }
            System.out.println(Integer.toString(i) + ": " + sb.toString());

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
        final double inverse = 1.0 / average;
        System.out.println("Completions/sec (adj avg): " + average);
        System.out.println("Secs/completion (adj avg): " + inverse);
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
