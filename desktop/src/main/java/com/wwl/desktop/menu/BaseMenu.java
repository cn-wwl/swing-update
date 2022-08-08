package com.wwl.desktop.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author wwl
 * @date 2022/8/5 17:41
 * @desc 菜单接口
 */
public abstract class BaseMenu {

    private int menuOrder;

    public BaseMenu(int order){
        menuOrder = order;
    }

    /**
     * 加载菜单
     * @param menuBar
     */
    public abstract void initMenu(JMenuBar menuBar);

    /**
     * 生成菜单
     * @param name 菜单名
     * @param actionListener 事件
     * @return 菜单
     */
    protected JMenuItem generateMenuItem(String name, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    public int getMenuOrder() {
        return menuOrder;
    }
}
