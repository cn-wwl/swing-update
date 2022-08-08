package com.wwl.desktop.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;

@Component
public class MenuFactory {

    @Autowired
    private List<BaseMenu> baseMenuList;

    public void initMenu(JMenuBar menuBar){
        baseMenuList.sort(Comparator.comparing(BaseMenu::getMenuOrder));
        baseMenuList.forEach(f->f.initMenu(menuBar));
    }
}
