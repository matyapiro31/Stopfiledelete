package net.surlinter.akira.stopfiledelete;

import java.io.IOException;
import java.util.Arrays;

public class AsyncShell extends Shell {

    public static Process execShell (String[] cmdarray) {
        Process process = null;
        String output;
        try {
            process = new ProcessBuilder(Arrays.asList(cmdarray)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        java.util.Date date= new java.util.Date();
        exec_log += "shell command `" + cmdarray[0] + "` was executed. (" + date.toString() + ")\n";
        exec_log += "arguments are";
        exec_log += " \'";
        for (String arg:cmdarray
                ) {
            exec_log += arg.equals(cmdarray[0])?"":" " + arg;
        } exec_log += "\'\n";
        exec_log += "process is " + process.toString() +"\n\n";

        return process;
    }
}
