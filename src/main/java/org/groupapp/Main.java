package org.groupapp;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        UIManager.setLookAndFeel(new FlatLightLaf()); // 可換成 FlatDarkLaf 等

        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame().setVisible(true);
    }
}
