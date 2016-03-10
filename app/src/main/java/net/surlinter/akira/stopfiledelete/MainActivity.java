package net.surlinter.akira.stopfiledelete;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    protected static final int log = 3;
    public String exec_log="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.content.Context context = this;
        final TextView TV1= (TextView)findViewById(R.id.tv1);
        TV1.setText("");
        TV1.setMovementMethod(ScrollingMovementMethod.getInstance());
        //mode = help; //mode of show help message.

        mode = usual;
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
                    mode = usual;
                    TV1.scrollTo(0, 0);
                    TV1.setText("");
                    SetFlag(context);
                }
            }
        });

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (mode == help) {
                    Snackbar.make(view,getString(R.string.showlog_message), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if (mode == usual) {
                        TV1.scrollTo(0,0);
                    }
                    mode = log;
                    TV1.setText(exec_log);
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
            if (mode == help) {
                Snackbar.make(getWindow().getDecorView(),getString(R.string.setting_message), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                android.content.Intent intent1 = new android.content.Intent(this, AkiraPreferenceActivity.class);
                startActivity(intent1);
            }
            return true;
        } else if (id == R.id.action_save_log) {
            final TextView TV1= (TextView)findViewById(R.id.tv1);
            if (mode == usual || mode == log) {
                try {
                    FileOutputStream f_os = openFileOutput("exec_log", MODE_APPEND|MODE_PRIVATE);
                    f_os.write(exec_log.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Snackbar.make(getWindow().getDecorView(),getString(R.string.saved), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (mode == help) {
                Snackbar.make(getWindow().getDecorView(),getString(R.string.savelog_message), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            return true;
        } else if (id == R.id.action_help) {
            mode = mode == help?usual:help;
            item.setChecked(!item.isChecked());
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
    public void onResume() {
        super.onResume();

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

    public void SetFlag(android.content.Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String default_root = preferences.getString("default_path","/");
        OpenFileDialog openFileDialog = new OpenFileDialog(default_root);
        openFileDialog.openFileAction = new OpenFileDialog.OpenFileAction() {
            @Override
            public File write(File file) {
                Shell.execShell(new String[] {"su", "-c", "chattr +i " + file.getAbsolutePath()});
                return file;
            }

            @Override
            public File append(File file) {
                Shell.execShell(new String[] {"su", "-c", "chattr -i " + file.getAbsolutePath()});
                return file;
            }

            @Override
            public void read(File file) {

            }
        };
        try {
            openFileDialog.createOpenFileDialog(context, OpenFileDialog.OpenMode.Write).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//TODO:add double click action to move.
//TODO:implement SuperUserOpenFileDialog.
//TODO:add ListView over the TextView and show file info.