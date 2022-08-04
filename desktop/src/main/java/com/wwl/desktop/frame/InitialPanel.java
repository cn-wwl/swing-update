package com.wwl.desktop.frame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author wwl
 * @date 2022/7/26 15:55
 * @desc 加载 面板
 */
public class InitialPanel extends JPanel {

    private JLabel labTitle;
    private JLabel labDesc;
    private JProgressBar progressBar;


    public InitialPanel() {

        labTitle = new JLabel();
        labTitle.setFont(new Font("宋体", Font.BOLD, 16));
        labTitle.setForeground(Color.WHITE);

        labDesc = new JLabel();
        labDesc.setFont(new Font("宋体", Font.PLAIN, 12));
        labDesc.setForeground(Color.WHITE);

        progressBar = new JProgressBar();
        progressBar.setValue(60);

        this.setLayout(null);
        labTitle.setBounds(200, 100, 180, 30);
        labDesc.setBounds(185, 195, 205, 20);
        progressBar.setBounds(180, 220, 210, 10);

        this.add(labTitle);
        this.add(progressBar);
        this.add(labDesc);
    }

    public void setTitle(String title){
        this.labTitle.setText(title);
    }

    public void setDescription(String description){
        this.labDesc.setText(description);
    }

    public void addSpeed(int value){
        this.progressBar.setValue( this.progressBar.getValue() + value);
    }

    public void setSpeed(int value){
        this.progressBar.setValue(value);
    }


    @Override
    protected void paintComponent(Graphics g) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("icons/loading.jpg")));
        g.drawImage(icon.getImage(),-580,-410,icon.getIconWidth(),icon.getIconHeight(),icon.getImageObserver());
    }
}
