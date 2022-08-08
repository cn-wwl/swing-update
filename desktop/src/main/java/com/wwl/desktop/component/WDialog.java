package com.wwl.desktop.component;

import com.wwl.core.utils.SpringContextUtils;
import com.wwl.desktop.global.FrameContext;
import com.wwl.desktop.global.ResourceContext;
import com.wwl.desktop.utils.UiConst;

import javax.swing.*;
import java.awt.*;

public class WDialog extends JDialog {

    protected JPanel dialogContent;

    public WDialog(int width,int height){
        this(width,height,false);
    }

    public WDialog(int width,int height,boolean modal){
        this(width,height,null,modal);
    }
    public WDialog(int width,int height,String title){
        this(width,height,title,false);
    }

    public WDialog(int width,int height,String title,boolean modal){
        super(FrameContext.getApplicationFrame(),modal);
        this.setSize(width, height);
        this.setTitle(title);
        this.setIconImage(ResourceContext.getInstance().getLogoIcon().getImage());
        this.setResizable(false);
        this.setSize(width,height);
        this.setLocationRelativeTo(FrameContext.getApplicationFrame());
        SpringContextUtils.injectService(this);
        this.initUi();
    }


    protected void initUi(){
        this.dialogContent = new JPanel();
        this.dialogContent.setBorder(BorderFactory.createEmptyBorder(UiConst.MINI_UI_SPACE,UiConst.MINI_UI_SPACE,UiConst.MINI_UI_SPACE,UiConst.MINI_UI_SPACE));
        this.add( this.dialogContent, BorderLayout.CENTER);
    }


}
