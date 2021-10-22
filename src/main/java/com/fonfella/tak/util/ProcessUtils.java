package com.fonfella.tak.util;

import org.springframework.stereotype.Component;

@Component
public class ProcessUtils {

    public boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public String[] prepender(String[] orig, String... prefixes) {

        String[] newcli = new String[orig.length + prefixes.length];

        System.arraycopy(prefixes, 0, newcli, 0, prefixes.length);
        System.arraycopy(orig, 0, newcli, prefixes.length, orig.length);

        return newcli;
    }

    public String[] shellPrepender(String[] cli) {

        String[] shcli;

        if (isWindows) {

            shcli = prepender(cli, "cmd.exe", "/C");

        } else {

            shcli = prepender(cli, "sh", "-c");
        }

        return shcli;
    }
}
