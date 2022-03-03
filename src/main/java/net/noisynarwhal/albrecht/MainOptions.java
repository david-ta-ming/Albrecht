/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht;

import static net.noisynarwhal.albrecht.Main.PROJECT;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 *
 * @author lioudt
 */
public class MainOptions extends Options {
    
    public MainOptions() {
        super.addOption(new Option("h", "help", false, "Display help"));
        super.addOption(new Option("o", "order", true, "Magic square order"));
        super.addOption(new Option("d", "dir", true, "Output directory"));
        super.addOption(new Option("r", "report", true, "Report frequency in secs"));
        
    }
    
    public void printHelp(String footer) {
        final String cmd = "java -jar " + PROJECT.getProperty("artifactId") + ".jar";
        new HelpFormatter().printHelp(cmd, "version " + PROJECT.getProperty("version"), this, footer);
    }
    
}
