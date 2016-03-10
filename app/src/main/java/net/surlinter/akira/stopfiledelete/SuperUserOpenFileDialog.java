package net.surlinter.akira.stopfiledelete;

public class SuperUserOpenFileDialog extends OpenFileDialog {
    public SuperUserOpenFileDialog () {
        try {
            this.init("/");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        path = "/";
    }
    public SuperUserOpenFileDialog (String openDir) {
        try {
            this.init(openDir);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        if (openDir.startsWith("//")) {
            path = openDir.substring(1);
        } else {
            path = openDir;
        }
    }
    @Override
    public void init (String dirname) throws java.io.IOException {

    }
    private String path;
    private String[] items;
}
