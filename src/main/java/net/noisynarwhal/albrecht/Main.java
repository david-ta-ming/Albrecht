/*
 * To change this license error, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht;

import net.noisynarwhal.albrecht.square.Population;
import net.noisynarwhal.albrecht.square.Magic;
import net.noisynarwhal.albrecht.square.Matrices;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import net.noisynarwhal.albrecht.square.Population.PopulationManager;

/**
 *
 * @author lioudt
 */
public class Main {

    private static final String VERSION = "1.2.0";
    private static final int NUM_THREADS = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);

    /**
     *
     * @param args
     */
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {

        final Options options = new Options();
        options.addOption(new Option("h", "help", false, "Display help"));
        options.addOption(new Option("o", "order", true, "Magic square order"));
        options.addOption(new Option("d", "dir", true, "Output directory"));
        options.addOption(new Option("p", "pop", true, "Size of population"));
        options.addOption(new Option("r", "report", true, "Report frequency in seconds"));

        try {

            final CommandLine line = new DefaultParser().parse(options, args);

            if (line.hasOption("help")) {
                new HelpFormatter().printHelp(Main.class.getName(), options);
                System.exit(0);
            }

            final int order = line.hasOption("order") ? Integer.parseInt(line.getOptionValue("order")) : 15;
            final File saveDir = line.hasOption("dir") ? new File(line.getOptionValue("dir")) : new File(System.getProperty("user.dir"));
            final int populationSize = line.hasOption("pop") ? Integer.parseInt(line.getOptionValue("pop")) : 25;
            final int reportSecs = line.hasOption("report") ? Integer.parseInt(line.getOptionValue("report")) : -1;

            if (!saveDir.isDirectory()) {
                throw new RuntimeException("Invalid directory path: '" + saveDir.getAbsolutePath() + '\'');
            }

            Main.run(order, saveDir, populationSize, reportSecs);

        } catch (Throwable th) {

            final String error = th.getMessage();
            new HelpFormatter().printHelp(Main.class.getName(), "", options, error);
            System.exit(1);
        }

        System.exit(0);
    }

    private static Magic run(int order, File saveDir, int populationSize, int reportSecs) throws Exception {

        Magic magic = null;

        System.out.println("\n\n");
        System.out.println("Version " + VERSION);
        System.out.println("Searching...");
        System.out.println(new Date());
        System.out.println("Searching...");
        System.out.println("Order: " + Integer.toString(order));
        System.out.println("Population Size: " + Integer.toString(populationSize));
        System.out.println("Number of threads: " + Integer.toString(NUM_THREADS));

        final long start = System.nanoTime();

        final List<Future<Magic>> tasks = new ArrayList<>();

        final ExecutorService threadService = Executors.newFixedThreadPool(NUM_THREADS);
        try {
            for (int i = 0; i < NUM_THREADS; i++) {
                final Callable<Magic> thread = new EvolutionManager(order, populationSize, reportSecs);
                final Future<Magic> task = threadService.submit(thread);
                tasks.add(task);
            }
        } finally {
            threadService.shutdown();
        }

        for (final Future<Magic> task : tasks) {
            final Magic m = task.get();
            if (m.isMagic()) {
                magic = m;
                break;
            }
        }

        final long elapsed = System.nanoTime() - start;

        if (magic != null) {

            final int[][] standardValues = Matrices.standardize(magic.getValues());

            Main.printResults(standardValues, elapsed, order);

            final File saveFile = Main.saveResults(standardValues, order, saveDir);

            System.out.println("File: " + saveFile.getAbsolutePath());
        } else {

            System.out.println("");
            System.out.println("*** Error: null result ***");

        }

        return magic;
    }

    private static File saveResults(final int[][] standardValues, int order, File saveDir) throws IOException {

        final File saveFile;

        final StringBuilder filename = new StringBuilder();

        final Hasher valuesHash = Hashing.farmHashFingerprint64().newHasher();
        for (final int[] row : standardValues) {
            for (final int v : row) {
                valuesHash.putInt(v);
            }
        }
        final int[] row0 = standardValues[0];
        filename.append(Integer.toString(order));
        filename.append('_');
        filename.append(Integer.toString(row0[0]));
        filename.append('-');
        filename.append(Integer.toString(row0[1]));
        filename.append('-');
        filename.append(Integer.toString(row0[2]));
        filename.append('_');
        filename.append(valuesHash.hash().toString());
        filename.append(".txt");

        saveFile = new File(saveDir, filename.toString());
        try (final Writer writer = new FileWriter(saveFile)) {
            Matrices.print(standardValues, writer);
        }

        return saveFile;

    }

    private static void printResults(final int[][] standardValues, final long elapsed, int order) throws IOException {

        System.out.println("");
        System.out.println(Matrices.print(standardValues));

        System.out.println("");
        System.out.println(new Date());

        System.out.println("");
        System.out.println("*** Complete ***");

        System.out.println("");
        System.out.println("Order: " + Integer.toString(order));

        System.out.println("");
        System.out.println("Elapsed: " + Main.timeMessage(elapsed, TimeUnit.NANOSECONDS));
    }

    private static class EvolutionManager implements Callable<Magic>, PopulationManager {

        private static final AtomicBoolean MAGIC_FOUND = new AtomicBoolean(false);
        private static final AtomicInteger HIGH_SCORE = new AtomicInteger(Integer.MIN_VALUE);
        private static final long STARTED = System.nanoTime();
        private static final AtomicLong LAST_REPORT = new AtomicLong(System.nanoTime());

        private final int order;
        private final int populationSize;
        private final int reportSecs;

        private EvolutionManager(int order, int populationSize, int reportSecs) {
            this.order = order;
            this.populationSize = populationSize;
            this.reportSecs = reportSecs;
        }

        @Override
        public Magic call() {
            try {
                return Population.evolve(this.order, this.populationSize, this);
            } catch (Throwable th) {
                System.err.print(th);
                throw th;
            }
        }

        @Override
        public void onStart(SortedSet<Magic> pop) {
        }

        @Override
        public boolean isFinished() {
            return MAGIC_FOUND.get();
        }

        @Override
        public void report(SortedSet<Magic> pop) {

            if (this.reportSecs > 0
                    && ThreadLocalRandom.current().nextDouble() < 0.0001
                    && TimeUnit.SECONDS.convert(System.nanoTime() - LAST_REPORT.get(), TimeUnit.NANOSECONDS) >= this.reportSecs) {

                LAST_REPORT.set(System.nanoTime());

                final Magic first = pop.first();
                HIGH_SCORE.set(Math.max(HIGH_SCORE.get(), first.getScore()));
                final long elapsed = System.nanoTime() - STARTED;

                final StringBuilder sb = new StringBuilder();

                sb.append(Integer.toString(this.order));
                sb.append(": ");
                sb.append(Main.timeMessage(elapsed, TimeUnit.NANOSECONDS));
                sb.append(": ");
                sb.append(Integer.toString(HIGH_SCORE.get()));
                sb.append('/').append(Integer.toString(first.getMaxScore()));

                System.out.println(sb.toString());

            }
        }

        @Override
        public void onFinish(SortedSet<Magic> pop) {

            MAGIC_FOUND.compareAndSet(false, true);
        }

    }

    private static String timeMessage(long elapsed, TimeUnit timeUnit) {

        final StringBuilder sb = new StringBuilder();

        final long hoursElapsed = TimeUnit.HOURS.convert(elapsed, timeUnit);
        final long minsElapsed = TimeUnit.MINUTES.convert(elapsed, timeUnit);
        final long secsElapsed = TimeUnit.SECONDS.convert(elapsed, timeUnit);

        if (hoursElapsed > 0) {

            final long minsRemainder = minsElapsed - (hoursElapsed * 60);

            sb.append(Long.toString(hoursElapsed));
            sb.append(" hours");
            sb.append(" + ");
            sb.append(Long.toString(minsRemainder));
            sb.append(" mins");

        } else if (minsElapsed > 0) {

            final long secsRemainder = secsElapsed - (minsElapsed * 60);

            sb.append(Long.toString(minsElapsed));
            sb.append(" mins");
            sb.append(" + ");
            sb.append(Long.toString(secsRemainder));
            sb.append(" secs");

        } else {

            sb.append(Long.toString(secsElapsed));
            sb.append(" secs");

        }

        return sb.toString();
    }

}
