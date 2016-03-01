package net.surlinter.akira.stopfiledelete;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Runtime;
import java.lang.Process;

public class OpenFileDialog {
    public OpenFileDialog() {
        this.init("/");
        path = "/";
    }
    public OpenFileDialog(String openDir) {
        this.init(openDir);
        path = openDir;
    }
    public void init(String dirname) {
        java.io.File file= new java.io.File(dirname);
        items = file.list();
    }

    public interface OpenFileAction {
        java.io.File write(java.io.File file);
        java.io.File append(java.io.File file);
        void read(java.io.File file);
        String toString();
    }

    public enum OpenMode {
        Write,Append,Read
    }
    public OpenFileAction openFileAction;
    private String path;
    private String[] items;

    public android.app.AlertDialog.Builder createOpenFileDialog(android.content.Context context,final OpenFileDialog.OpenMode mode)
            throws java.io.IOException {
        final java.util.ArrayList<Integer> checkedItems = new java.util.ArrayList<>();
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.open))
                .setMultiChoiceItems(items, null, new android.content.DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) checkedItems.add(which);
                        else checkedItems.remove(which);
                    }
                })
                .setPositiveButton(context.getString(R.string.open), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {

                        for (Integer i : checkedItems) {
                            // item_i checked
                            // note: i is undefined when not checked. use switch to do work.

                            // insert / as double / is allowed and no / is error.
                            java.io.File file = new java.io.File(path + "/" + items[i]);
                            switch (mode) {
                                case Write:
                                    openFileAction.write(file);
                                    break;
                                case Append:
                                    openFileAction.append(file);
                                    break;
                                case Read:
                                    openFileAction.read(file);
                                    break;
                                default:
                                    android.util.Log.d("FileState", openFileAction.toString());
                            }
                        }
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), null);
        return builder;
    }
}
