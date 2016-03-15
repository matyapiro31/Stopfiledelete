package net.surlinter.akira.stopfiledelete;

public class SuperUserOpenFileDialog extends OpenFileDialog {
    public SuperUserOpenFileDialog () {
    }

    public SuperUserOpenFileDialog (String openDir) {
        super(openDir);
    }
    @Override
    public void init (String dirname) throws java.io.IOException {
        if (dirname.equals("")) {
            items = new String[] {"/"};
            return;
        }
        library_path = "/data/data/net.surlinter.akira.stopfiledelete/files";
        java.lang.Process pr =AsyncShell.execShell(new String[] {"su", "-c", "cd " + library_path + "&&" + "source .mkshrc &&" + "list_files " + dirname});
        String output = AsyncShell.getProcessStdOut(pr);
        items = output.split("\n");
    }
    public String library_path;//Path for shell functions.

    @Override
    public int move(android.content.Context context,OpenMode mode, String path) {
        SuperUserOpenFileDialog childopenfiledialog = new SuperUserOpenFileDialog(path);
        childopenfiledialog.openFileAction = openFileAction;
        childopenfiledialog.MenuWords = MenuWords;
        android.util.Log.d("Path", path);
        try {
            childopenfiledialog.createOpenFileDialog(context, mode).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
