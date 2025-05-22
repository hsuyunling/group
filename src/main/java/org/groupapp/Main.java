package org.groupapp;

import java.util.prefs.Preferences;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new HomePage();
        Preferences prefs = Preferences.userRoot().node("org.groupapp");
        prefs.put("userId", "113306005");  // 模擬許筠靈登入
        frame.setVisible(true);
    }
}