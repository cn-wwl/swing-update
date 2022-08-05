package com.wwl.desktop.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author wwl
 * @date 2022/8/5 17:41
 * @desc TODO
 */
public interface IMenu {

    /**
     * 加载菜单
     * @param menuBar
     */
    void initMenu(JMenuBar menuBar);

    /**
     * 生成菜单
     * @param name 菜单名
     * @param actionListener 事件
     * @return 菜单
     */
    default JMenuItem generateMenuItem(String name, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setName(name);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

}
