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
import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import net.noisynarwhal.albrecht.square.Evolutions;
import net.noisynarwhal.albrecht.square.Evolutions.DefaultEvolutionMonitor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;

/**
 *
 * @author lioudt
 */
public class Main {

    public static final Properties PROJECT = new Properties();
    private static final int NUM_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(Main.class.getName());

    static {

        try (final InputStream in = Main.class.getResourceAsStream("/project.properties")) {

            PROJECT.load(in);

        } catch (IOException ex) {
            LOGGER.error(ex, ex);
        }
    }

    /**
     *
     * @param args
     */
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {

        final MainOptions options = new MainOptions();

        try {

            final CommandLine line = new DefaultParser().parse(options, args);

            if (line.hasOption("help")) {
                options.printHelp("");
                System.exit(0);
            }

            final int order = line.hasOption("order") ? Integer.parseInt(line.getOptionValue("order")) : 30;
            final File saveDir = line.hasOption("dir") ? new File(line.getOptionValue("dir")) : new File(System.getProperty("user.dir"));
            final int reportFreq = line.hasOption("report") ? Integer.parseInt(line.getOptionValue("report")) : -1;

            if (!saveDir.isDirectory()) {
                throw new RuntimeException("Invalid directory path: '" + saveDir.getAbsolutePath() + '\'');
            }

            final Magic magicStart = Magic.build(order);
            Main.run(magicStart, saveDir, reportFreq);

        } catch (Throwable th) {

            final String error = th.getMessage();
            options.printHelp(error);
            System.exit(1);
        }

        System.exit(0);
    }

    private static Magic run(final Magic magicStart, final File saveDir, final int reportFreq) throws Exception {

        final Magic magic;

        final int order = magicStart.getOrder();

        System.out.println("\n\n");
        System.out.println("Version " + PROJECT.getProperty("version"));
        System.out.println("Num threads " + Integer.toString(NUM_THREADS));
        System.out.println("Searching...");
        System.out.println(new Date());
        System.out.println("Searching...");
        System.out.println("Order: " + Integer.toString(order));

        final long start = System.nanoTime();

        magic = Evolutions.evolve(magicStart, NUM_THREADS, new DefaultEvolutionMonitor() {
            private final long start = System.nanoTime();
            private final AtomicLong lastReport = new AtomicLong(System.nanoTime());
            private final AtomicInteger highScore = new AtomicInteger(0);

            @Override
            public void report(Magic magic) {

                if (reportFreq > 0 && ThreadLocalRandom.current().nextDouble() < 0.00001D
                        && TimeUnit.SECONDS.convert(System.nanoTime() - this.lastReport.get(), TimeUnit.NANOSECONDS) >= reportFreq) {

                    final long elapsed = System.nanoTime() - this.start;

                    this.highScore.set(Math.max(this.highScore.get(), magic.getScore()));

                    System.out.println(Integer.toString(order) + ": " + Main.printTimeMessage(elapsed, TimeUnit.NANOSECONDS) + ' ' + Integer.toString(this.highScore.get()) + '/' + Integer.toString(magic.getMaxScore()));

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
    private static void printResults(int[][] values, final long elapsed) {

        values = Matrices.standardize(values);

        final int order = values.length;

        System.out.println("");
        System.out.println(Matrices.print(values));

        System.out.println("");
        System.out.println(new Date());

        System.out.println("");
        if (Matrices.isMagic(values)) {
            System.out.println("*** Complete ***");
        } else {
            System.out.println("*** Not magic ***");
        }

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
