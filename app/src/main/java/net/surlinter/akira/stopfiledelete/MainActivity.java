package net.surlinter.akira.stopfiledelete;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Runtime;
import java.lang.Process;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //mode of the app.
    protected int mode=0;
    protected static final int usual = 1;
    protected static final int help = 2;
    public String exec_log="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Test
        String test = "/storage/emulated/legacy/test";
        addAttr(test);
        final TextView TV1= (TextView)findViewById(R.id.tv1);
        TV1.setText(test);

        //set fab events
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == help) {
                    Snackbar.make(view,getString(R.string.search_message), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    java.util.Date date= new java.util.Date();
                    exec_log += "Search fab was clicked. (" + date.toString() +")\n\n";
                    Process pr_ls = execShell(new String[] {"su","-c","lsattr /storage/emulated/legacy/"});
                    String output = getProcessStdOut(pr_ls);
                    TV1.setText(output);
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; t
        // his adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean addAttr(String file) {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String[] command = {
                "su","-c","chattr +i "+file
        };

        try {
            process = runtime.exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        java.util.Date date= new java.util.Date();
        exec_log += "i flag was added to " + file + ". (" + date.toString() + ")\n\n";

        return true;
    }

    public boolean removeAttr(String file) {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String[] command = {
                "su","-c","chattr","-i",file
        };

        try {
            process = runtime.exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        java.util.Date date= new java.util.Date();
        exec_log += "i flag was removed to " + file +". (" + date.toString() + ")\n\n";

        return true;
    }

    public Process execShell(String[] cmdarray) {
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
            exec_log += arg==cmdarray[0]?"":" " + arg;
        } exec_log += "\'\n";
        exec_log += "process is " + process.toString() +"\n\n";

        return process;
    }

    public String getProcessStdOut(java.lang.Process pr) {
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

    public String getProcessStdErr(java.lang.Process pr) {
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

    public boolean setProcessStdIn(java.lang.Process pr,String[] text) {
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

    public int exitProcess(java.lang.Process pr) {
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://android.surlinter.net/api"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://net.surlinter.akira.stopfiledelete/http/android.surlinter.net/api")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://android.surlinter.net/api"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://net.surlinter.akira.stopfiledelete/http/android.surlinter.net/api")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
