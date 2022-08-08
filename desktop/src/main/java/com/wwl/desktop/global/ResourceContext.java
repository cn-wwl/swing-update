package com.wwl.desktop.global;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Objects;

public class ResourceContext {

    private static ResourceContext instance;
    private ImageIcon logoIcon;


    private ResourceContext() {
    }

    public static ResourceContext getInstance(){
        if (instance==null){
            instance = new ResourceContext();
        }
        return instance;
    }

    public ImageIcon getLogoIcon() {
        if (logoIcon == null){
            logoIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("icons/logo.png")));
        }

        return logoIcon;
    }
}
