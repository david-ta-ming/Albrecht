/*
 * To change this license error, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht;

import net.noisynarwhal.albrecht.square.Magic;
import net.noisynarwhal.albrecht.square.Matrices;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import net.noisynarwhal.albrecht.square.Evolutions;
import net.noisynarwhal.albrecht.square.Evolutions.DefaultEvolutionMonitor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 *
 * @author lioudt
 */
public class Main {

    private static final String VERSION = "2.0.0";
    private static final int NUM_THREADS = Math.max(2, Runtime.getRuntime().availableProcessors() / 3);

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

        try {

            final CommandLine line = new DefaultParser().parse(options, args);

            if (line.hasOption("help")) {
                new HelpFormatter().printHelp(Main.class.getName(), options);
                System.exit(0);
            }

            final int order = line.hasOption("order") ? Integer.parseInt(line.getOptionValue("order")) : 30;
            final File saveDir = line.hasOption("dir") ? new File(line.getOptionValue("dir")) : new File(System.getProperty("user.dir"));

            if (!saveDir.isDirectory()) {
                throw new RuntimeException("Invalid directory path: '" + saveDir.getAbsolutePath() + '\'');
            }

            Main.run(order, saveDir);

        } catch (Throwable th) {

            final String error = th.getMessage();
            new HelpFormatter().printHelp(Main.class.getName(), "", options, error);
            System.exit(1);
        }

        System.exit(0);
    }

    private static Magic run(final int order, final File saveDir) throws Exception {

        final Magic magic;

        System.out.println("\n\n");
        System.out.println("Version " + VERSION);
        System.out.println("Searching...");
        System.out.println(new Date());
        System.out.println("Searching...");
        System.out.println("Order: " + Integer.toString(order));

        final long start = System.nanoTime();

        magic = Evolutions.evolve(order, NUM_THREADS, new DefaultEvolutionMonitor() {
            private final long start = System.nanoTime();
            private final AtomicLong lastReport = new AtomicLong(System.nanoTime());
            private final AtomicInteger highScore = new AtomicInteger(0);

            @Override
            public void report(Magic magic) {
                if (ThreadLocalRandom.current().nextDouble() < 0.00001
                        && TimeUnit.SECONDS.convert(System.nanoTime() - this.lastReport.get(), TimeUnit.NANOSECONDS) >= 15) {

                    final long elapsed = System.nanoTime() - this.start;

                    final long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
                    this.highScore.set(Math.max(this.highScore.get(), magic.getScore()));

                    System.out.println(Integer.toString(order) + ": " + Long.toString(elapsedSecs) + "s: " + Integer.toString(this.highScore.get()) + '/' + Integer.toString(magic.getMaxScore()));

                    this.lastReport.set(System.nanoTime());
                }
            }

        });

        final long elapsed = System.nanoTime() - start;

        if (magic != null) {

            final int[][] values = magic.getValues();

            Main.printResults(values, elapsed);

            final File saveFile = Main.saveResults(values, saveDir);

            System.out.println("File: " + saveFile.getAbsolutePath());
        } else {

            System.out.println("");
            System.out.println("*** Error: null result ***");

        }

        return magic;

    }

    /**
     * Standardize the results to Frénicle form then save to file. The filename
     * format is:
     * <order>_<number1>-<number2>-<number3>_<file hash>.txt. The file hash is a
     * FarmHash Fingerprint64 as implemented in the Guava library.
     *
     * @see
     * https://guava.dev/releases/snapshot/api/docs/com/google/common/hash/Hashing.html#farmHashFingerprint64()
     * @param values
     * @param saveDir
     * @return the file to which the results have been saved
     * @throws IOException
     */
    private static File saveResults(int[][] values, File saveDir) throws IOException {

        final File saveFile;

        values = Matrices.standardize(values);

        final StringBuilder filename = new StringBuilder();

        final Hasher valuesHash = Hashing.farmHashFingerprint64().newHasher();
        for (final int[] row : values) {
            for (final int v : row) {
                valuesHash.putInt(v);
            }
        }
        final int[] row0 = values[0];

        final int order = row0.length;

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
            Matrices.print(values, writer);
        }

        return saveFile;

    }

    /**
     * Standardize the results to Frénicle form then print details to standard
     * out.
     *
     * @param values
     * @param elapsed
     * @throws IOException
     */
    private static void printResults(int[][] values, final long elapsed) throws IOException {

        values = Matrices.standardize(values);

        final int order = values.length;

        System.out.println("");
        System.out.println(Matrices.print(values));

        System.out.println("");
        System.out.println(new Date());

        System.out.println("");
        System.out.println("*** Complete ***");

        System.out.println("");
        System.out.println("Order: " + Integer.toString(order));

        System.out.println("");
        System.out.println("Elapsed: " + Main.printTimeMessage(elapsed, TimeUnit.NANOSECONDS));
    }

    private static String printTimeMessage(long elapsed, TimeUnit timeUnit) {

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
