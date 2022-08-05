package com.wwl.desktop.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wwl
 * @date 2022/8/5 17:35
 * @desc 文件菜单
 */
public class FileMenu implements IMenu {

    private JMenu fileMenu = new JMenu("文件");

    @Override
    public void initMenu(JMenuBar menuBar){

        this.addMenuItem("打开",this::openClick);
        this.addMenuItem("保存",this::saveClick);

        menuBar.add(fileMenu);
    }

    private void addMenuItem(String name, ActionListener actionListener){
        fileMenu.add(generateMenuItem(name,actionListener));
    }

    private void openClick(ActionEvent event){
        System.out.println(event.getActionCommand());
    }

    private void saveClick(ActionEvent event){
        System.out.println(event.getActionCommand());
    }

}
