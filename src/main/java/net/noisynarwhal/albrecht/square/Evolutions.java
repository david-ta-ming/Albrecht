/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht.square;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author lioudt
 */
public class Evolutions {

    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(Evolutions.class.getName());

    /**
     *
     * @param order
     * @return
     */
    public static Magic evolve(int order) {
        return Evolutions.evolve(order, new DefaultEvolutionManager(), new DefaultEvolutionManager());
    }

    /**
     *
     * @param order
     * @param monitor
     * @return
     */
    public static Magic evolve(int order, EvolutionMonitor monitor) {
        return Evolutions.evolve(order, monitor, new DefaultEvolutionManager());
    }

    private static Magic evolve(int order, EvolutionMonitor monitor, EvolutionManager manager) {

        Magic magic = Magic.build(order);

        manager.onStart(magic);
        monitor.onStart(magic);

        while (!(magic.isMagic() || manager.isFinished())) {

            final Magic child = magic.newChild();

            if (child.getScore() >= magic.getScore()) {
                magic = child;
            }

            manager.report(magic);
            monitor.report(magic);
        }

        manager.onFinish(magic);
        monitor.onFinish(magic);

        return magic;
    }

    /**
     *
     * @param order
     * @param numThreads
     * @param monitor
     * @return
     */
    public static Magic evolve(int order, int numThreads, EvolutionMonitor monitor) {

        final List<Future<Magic>> promises = new ArrayList<>();

        final ExecutorService threadService = Executors.newFixedThreadPool(numThreads);

        try {

            for (int i = 0; i < numThreads; i++) {

                final Callable<Magic> thread = new Evolution(order, monitor);

                final Future<Magic> promise = threadService.submit(thread);

                promises.add(promise);
            }

        } finally {
            threadService.shutdown();
        }

        Magic magic = null;

        for (final Future<Magic> promise : promises) {
            try {
                final Magic m = promise.get();
                if (m.isMagic()) {
                    magic = m;
                }
            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error(ex.getMessage());
            }
        }

        return magic;
    }

    private static class Evolution implements Callable<Magic>, EvolutionManager {

        private final int order;
        private final EvolutionMonitor monitor;

        private static final AtomicBoolean FINISHED = new AtomicBoolean(false);

        public Evolution(int order, EvolutionMonitor monitor) {
            this.order = order;
            this.monitor = monitor;
        }

        @Override
        public Magic call() {

            try {

                Magic magic = Evolutions.evolve(this.order, this.monitor, this);

                return magic;

            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                throw ex;
            }
        }

        @Override
        public void onStart(Magic magic) {
        }

        @Override
        public void report(Magic magic) {
        }

        @Override
        public void onFinish(Magic magic) {
            FINISHED.compareAndSet(false, true);
        }

        @Override
        public boolean isFinished() {
            return FINISHED.get();
        }
    }

    /**
     *
     */
    public static interface EvolutionMonitor {

        /**
         *
         * @param magic
         */
        public void onStart(Magic magic);

        /**
         *
         * @param magic
         */
        public void report(Magic magic);

        /**
         *
         * @param magic
         */
        public void onFinish(Magic magic);
    }

    /**
     *
     */
    public static class DefaultEvolutionMonitor implements EvolutionMonitor {

        /**
         *
         * @param magic
         */
        @Override
        public void onStart(Magic magic) {
        }

        /**
         *
         * @param magic
         */
        @Override
        public void report(Magic magic) {
        }

        /**
         *
         * @param magic
         */
        @Override
        public void onFinish(Magic magic) {
        }

    }

    private static interface EvolutionManager extends EvolutionMonitor {

        public boolean isFinished();
    }

    private static class DefaultEvolutionManager implements EvolutionManager {

        @Override
        public void onStart(Magic magic) {
        }

        @Override
        public void report(Magic magic) {
        }

        @Override
        public void onFinish(Magic magic) {
        }

        @Override
        public boolean isFinished() {
            return false;
        }

    }

}
