package net.surlinter.akira.stopfiledelete;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by akira on 16/03/11.
 */
public class Shell {
    public static Process execShell(String[] cmdarray) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        String output;
        try {
            process = runtime.exec(cmdarray);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return ret;
    }
}
