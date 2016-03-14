package net.surlinter.akira.stopfiledelete;

import java.util.HashMap;

public class Translate {
    public Translate(final String[] reses, final String[] tags) {
        if (reses.length != tags.length) {
            throw new IllegalArgumentException();
        } else {
            translation = new HashMap<String,String>();
            for (int i=0;i<tags.length;i++) {
                translation.put(tags[i],reses[i]);
            }
        }
    }
    public HashMap<String,String> translation;
}
