package com.wwl.desktop.dialog;

import com.alibaba.fastjson.JSONObject;
import com.wwl.desktop.component.WDialog;
import com.wwl.desktop.config.SystemConfig;
import com.wwl.desktop.config.SystemConfigProperties;
import com.wwl.desktop.config.UpdateConfig;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

public class CheckDialog extends WDialog {

    @Autowired
    private SystemConfig systemConfig;

    public CheckDialog(){
        super(400,300,"版本记录");
        this.setVisible(true);
    }

    @Override
    protected void initUi() {
        super.initUi();

        JLabel labTitle = new JLabel(BorderLayout.CENTER);
        labTitle.setFont(new Font("宋体",Font.BOLD,18));
        labTitle.setText(systemConfig.updateConfig().getProject());


        JPanel panel = new JPanel();
        ScrollPane scrollPaneLayout = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        this.setUpdateContent(panel);

        scrollPaneLayout.add(panel);
        this.add(labTitle,BorderLayout.NORTH);
        this.add(scrollPaneLayout,BorderLayout.CENTER);

    }

    private void setUpdateContent(JPanel content){
        int row =systemConfig.updateConfig().getVersions().size();
        content.setLayout(new GridLayout(row,1));

        for (UpdateConfig.Version version : systemConfig.updateConfig().getVersions()) {

            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridLayout(2,1));

            JLabel labVersion = new JLabel();
            labVersion.setVerticalAlignment(SwingConstants.CENTER);
            labVersion.setFont(new Font("宋体",Font.BOLD,14));
            labVersion.setText(version.getVersion());

            JTextPane paneContent = new JTextPane();
            paneContent.setEditable(false);
            paneContent.setText(version.getContent());

            jPanel.add(labVersion);
            jPanel.add(paneContent);


            content.add(jPanel);
        }
    }

}
