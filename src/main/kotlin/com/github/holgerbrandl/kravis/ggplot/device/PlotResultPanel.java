package com.github.holgerbrandl.kravis.ggplot.device;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Holger Brandl
 */
public class PlotResultPanel {
    private JButton lastButton;
    private JButton nextButton;
    private JButton clearHistoryButton;
    private JButton exportButton;
    private JButton copyButton;
    private JPanel contentPanel;
    private JPanel mainPanel;


    public ImagePanel imagePanel;


    public PlotResultPanel() {
        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked next");
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked next");
            }
        });

        imagePanel = new ImagePanel();
        contentPanel.add(imagePanel);
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
