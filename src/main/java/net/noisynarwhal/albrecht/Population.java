/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author lioudt
 */
public class Population {

    /**
     *
     * @param order
     * @param populationSize
     * @return
     */
    public static Magic evolve(int order, int populationSize) {
        return Population.evolve(order, populationSize, new DefaultPopulationMonitor());
    }

    /**
     *
     * @param order
     * @param populationSize
     * @param manager
     * @return
     */
    public static Magic evolve(int order, int populationSize, PopulationManager manager) {

        final SortedSet<Magic> population = new TreeSet<>();

        while (population.size() < populationSize) {
            population.add(Magic.generate(order));
        }
        
        manager.onStart(population);

        while (!(population.first().isMagic() || manager.isFinished())) {

            final Iterator<Magic> it = new ArrayList<>(population).iterator();
            while (it.hasNext()) {

                final Magic parent = it.next();
                final Magic child = parent.newChild();

                final Magic last = population.last();

                if (child.compareTo(last) < 0 && population.add(child)) {
                    population.remove(last);
                }

            }

            manager.report(population);

        }

        manager.onFinish(population);

        return population.first();
    }

    /**
     *
     */
    public static interface PopulationManager {

        /**
         *
         * @return
         */
        public boolean isFinished();

        /**
         *
         * @param pop
         */
        public void onStart(SortedSet<Magic> pop);

        /**
         *
         * @param pop
         */
        public void report(SortedSet<Magic> pop);

        /**
         *
         * @param pop
         */
        public void onFinish(SortedSet<Magic> pop);
    }

    private static class DefaultPopulationMonitor implements PopulationManager {

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public void onStart(SortedSet<Magic> pop) {
        }

        @Override
        public void report(SortedSet<Magic> pop) {
        }

        @Override
        public void onFinish(SortedSet<Magic> pop) {
        }

    }
}
