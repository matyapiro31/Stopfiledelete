package net.surlinter.akira.stopfiledelete;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shell {
    public static String exec_log="";
    public static Process execShell(String[] cmdarray) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        String output;
        try {
            process = runtime.exec(cmdarray);
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

    public static String getProcessStdOut(java.lang.Process pr) {
        String str_stdout="";
        try {
            String line;
            BufferedReader read_stdout = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            while ((line=read_stdout.readLine())!=null) {
                str_stdout +=line + "\n";
            }
            read_stdout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str_stdout;
    }

    public static String getProcessStdErr(java.lang.Process pr) {
        String str_stderr="";
        try {
            String line;
            BufferedReader read_stderr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            while ((line=read_stderr.readLine())!=null) {
                str_stderr +=line + "\n";
            }
            read_stderr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str_stderr;
    }

    public static boolean setProcessStdIn(java.lang.Process pr,String[] text) {
        try {
            DataOutputStream d_out = new DataOutputStream(pr.getOutputStream());
            for (String word:text
                    ) {
                d_out.writeBytes(word+"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        java.util.Date date= new java.util.Date();
        exec_log += "Standard input for " + pr.toString() + " received text. (" + date.toString() + ")\n";
        for (String word:text
                ) {
            exec_log += word + "\n";
        }

        return true;
    }

    public static int exitProcess(java.lang.Process pr) {
        int ret;
        try {
            pr.waitFor();
            ret = pr.exitValue();
            if (ret == -1) {
                ret = 0x10;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            pr.destroy();
            ret = -1;
        }
        exec_log += "process " + pr.toString() + " is exited. (" + new java.util.Date().toString() + ")\n\n";
        return ret;
    }
}
