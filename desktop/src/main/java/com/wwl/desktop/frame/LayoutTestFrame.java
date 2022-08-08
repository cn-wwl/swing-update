package com.wwl.desktop.frame;

import com.wwl.core.utils.SpringContextUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LayoutTestFrame extends JFrame {

    public static void main(String[] args) {
        new LayoutTestFrame();
    }

    public LayoutTestFrame(){
        super();
        SpringContextUtils.injectService(this);
        this.setTitle("LayoutTest");
        this.setSize(800,600);
        this.setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("icons/logo.png"))).getImage());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.initUi();
    }


    private void initUi(){

        JPanel panel = new JPanel();
        // this.boxLayout(panel);
        // this.gridLayout(panel);
        // this.gridBagLayout(panel);
        this.groupLayout(panel);

        this.add(panel);
    }

    public void boxLayout(Container pane) {
        JPanel xPanel = new JPanel();
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));
        addButtons(xPanel);
        JPanel yPanel = new JPanel();
        yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.Y_AXIS));
        addButtons(yPanel);

        pane.add(yPanel, BorderLayout.PAGE_START);
        pane.add(xPanel, BorderLayout.PAGE_END);
    }

    private void addAButton(String text, Container container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(button);
    }

    private void addButtons(Container container) {
        addAButton("Button 1", container);
        addAButton("Button 2", container);
        addAButton("Button 3", container);
        addAButton("Long-Named Button 4", container);
        addAButton("5", container);
    }


    private void gridLayout(Container pane){
        JButton[] buttons = new JButton[9];
        pane.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(i + "");
            pane.add(buttons[i]);
        }
    }

    /**
     * GridBagConstraints可以从这11个方面来进行控制和操纵。这些内容是：
     * 1、gridx—组件的横向坐标；
     * 2、girdy—组件的纵向坐标；
     * 3、gridwidth—组件的横向宽度，也就是指组件占用的列数；
     * 4、gridheight—组件的纵向长度，也就是指组件占用的行数；
     * 5、weightx—指行的权重，告诉布局管理器如何分配额外的水平空间；
     * 6、weighty—指列的权重，告诉布局管理器如何分配额外的垂直空间；
     * 7、anchor—当组件小于其显示区域时使用此字段；
     * 8、fill—如果显示区域比组件的区域大的时候，可以用来控制组件的行为。控制组件是垂直填充，还是水平填充，或者两个方向一起填充；
     * 9、insets—指组件与表格空间四周边缘的空白区域的大小；
     * 10、ipadx— 组件间的横向间距，组件的宽度就是这个组件的最小宽度加上ipadx值；
     * 11、ipady— 组件间的纵向间距，组件的高度就是这个组件的最小高度加上ipady值。
     * 说明：
     * 1、gridx，gridy：其实就是组件行列的设置，注意都是从0开始的，比如 gridx=0，gridy=1时放在0行1列；
     * 2、gridwidth，gridheight：默认值为1；GridBagConstraints.REMAINDER常量，代表此组件为此行或此列的最后一个组件，会占据所有剩余的空间；
     * 3、weightx，weighty：当窗口变大时，设置各组件跟着变大的比例。比如组件A的weightx=0.5，组件B的weightx=1，那么窗口X轴变大时剩余的空间就会以1：2的比例分配给组件A和B；
     * 4、anchor：当组件空间大于组件本身时，要将组件置于何处。 有CENTER（默认值）、NORTH、NORTHEAST、EAST、SOUTHEAST、WEST、NORTHWEST选择。
     * 5、insets：设置组件之间彼此的间距。它有四个参数，分别是上，左，下，右，默认为（0，0，0，0）。
     * @param pane
     */
    public void gridBagLayout(Container pane) {
        JButton button;
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        button = new JButton("Button 1");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Button 2");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Button 3");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Long-Named Button 4");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(button, c);

        button = new JButton("5");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0; // reset to default
        c.weighty = 1.0; // request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; // bottom of space
        c.insets = new Insets(10, 0, 0, 0); // top padding
        c.gridx = 1; // aligned with button 2
        c.gridwidth = 2; // 2 columns wide
        c.gridy = 2; // third row
        pane.add(button, c);
    }


    private void groupLayout(Container pane){

        JPanel panel = new JPanel();
        // panel.setBackground(Color.darkGray);
        panel.setSize(200,200);
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JButton btn1 = new JButton("Button 1");
        JButton btn2 = new JButton("Button 2");
        JButton btn3 = new JButton("Button 3");

        layout.setHorizontalGroup( layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(
                                        GroupLayout.Alignment.LEADING)
                                .addComponent(btn1)
                                .addComponent(btn2)
                                .addComponent(btn3)
                        )
        );


        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(btn1)
                .addComponent(btn2)
                .addComponent(btn3)
        );
        panel.setLayout(layout);
        pane.add(panel);
    }

}
