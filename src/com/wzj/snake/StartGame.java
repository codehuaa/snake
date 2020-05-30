package com.wzj.snake;

import javax.swing.*;

public class StartGame {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("wzh-贪吃蛇");
        jFrame.setBounds(10, 10, 900, 720);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.add(new GamePanel());

        jFrame.setVisible(true);
    }
}
