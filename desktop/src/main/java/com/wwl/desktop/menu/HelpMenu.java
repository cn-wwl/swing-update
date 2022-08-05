package com.wwl.desktop.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wwl
 * @date 2022/8/5 17:35
 * @desc 文件菜单
 */
public class HelpMenu implements IMenu {

    private JMenu fileMenu = new JMenu("帮助");

    @Override
    public void initMenu(JMenuBar menuBar){

        this.addMenuItem("检查更新",this::checkClick);
        this.addMenuItem("关于",this::aboutClick);
        
        menuBar.add(fileMenu);
    }

    private void addMenuItem(String name, ActionListener actionListener){
        fileMenu.add(generateMenuItem(name,actionListener));
    }

    private void checkClick(ActionEvent event){
        System.out.println(event.getActionCommand());
    }

    private void aboutClick(ActionEvent event){
        System.out.println(event.getActionCommand());
    }

}
