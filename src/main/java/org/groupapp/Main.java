package org.groupapp;

import java.awt.Font;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        UIManager.setLookAndFeel(new FlatLightLaf()); // 可換成 FlatDarkLaf 等
        UIManager.put("defaultFont", new Font("微軟正黑體", Font.PLAIN, 14));


        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame().setVisible(true);
    }
}
