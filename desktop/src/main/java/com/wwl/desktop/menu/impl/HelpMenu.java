package com.wwl.desktop.menu.impl;

import com.wwl.desktop.dialog.CheckDialog;
import com.wwl.desktop.global.FrameContext;
import com.wwl.desktop.menu.BaseMenu;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wwl
 * @date 2022/8/5 17:35
 * @desc 文件菜单
 */
@Component("HelpMenu")
public class HelpMenu extends BaseMenu {

    private JMenu fileMenu = new JMenu("帮助");
    public HelpMenu() {
        super(9);
    }

    @Override
    public void initMenu(JMenuBar menuBar){

        this.addMenuItem("版本记录",this::checkClick);
        this.addMenuItem("关于",this::aboutClick);
        
        menuBar.add(fileMenu);
    }

    private void addMenuItem(String name, ActionListener actionListener){
        fileMenu.add(generateMenuItem(name,actionListener));
    }

    private void checkClick(ActionEvent event){
        new CheckDialog();
    }

    private void aboutClick(ActionEvent event){
        System.out.println(event.getActionCommand());
    }

}
