package com.wzj.snake;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    int length;  //蛇长
    int[] snakeX = new int[600]; //蛇身的x y坐标
    int[] snakeY = new int[600];
    String fx;   //蛇头方向

    boolean isStart;  //游戏是否启动
    boolean isFail;   //游戏是否失败

    Timer timer = new Timer(50, this);

    //定义一个食物
    int foodx, foody;
    Random random = new Random();

    //定义一个积分
    int score;

    public GamePanel() {
        init();
        //获取键盘的监听事件
        this.setFocusable(true);
        this.addKeyListener(this);
        createFood();
        score = 0;
    }

    //初始化
    public void init() {
        length = 3;
        snakeX[0] = 100;snakeY[0] = 100;
        snakeX[1] = 75;snakeY[1] = 100;
        snakeX[2] = 50;snakeY[2] = 100;
        fx = "R";
        isStart = false;
        isFail = false;
        score = 0;
    }

    //生成食物
    public void createFood() {
        foodx = 25 + 25*random.nextInt(34);
        foody = 75 + 25 * random.nextInt(24);
    }

    //绘画
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
        g.fillRect(25, 75, 850, 600);

        //控制蛇头图片
        if(fx.equals("R")) {
            Data.right.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if(fx.equals("L")) {
            Data.left.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if(fx.equals("U")) {
            Data.up.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if(fx.equals("D")) {
            Data.down.paintIcon(this, g, snakeX[0], snakeY[0]);
        }
        //控制身体
        for(int i = 1; i < length; ++i) {
            Data.body.paintIcon(this, g, snakeX[i], snakeY[i]);
        }

        //画积分
        g.setColor(Color.white); //设置画笔颜色
        g.setFont(new Font("微软雅黑", Font.BOLD, 35)); //设置字体
        g.drawString("长度："+length, 110, 50);
        g.drawString("分数："+score, 600, 50);

        //控制食物
        Data.food.paintIcon(this, g, foodx, foody);

        //控制游戏开始
        if(!isStart) {
            g.setColor(Color.white); //设置画笔颜色
            g.setFont(new Font("微软雅黑", Font.BOLD, 40)); //设置字体
            g.drawString("按下空格开始游戏", 300, 300);
        }

        //控制游戏失败
        if(isFail) {
            g.setColor(Color.orange); //设置画笔颜色
            g.setFont(new Font("微软雅黑", Font.BOLD, 40)); //设置字体
            g.drawString("游戏失败，按下空格重新开始", 200, 300);
            timer.stop();
            isStart = false;
        }
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //按下键盘的哪个键
        int keyCode = e.getKeyCode();
        //空格控制开始暂停游戏
        if(keyCode == KeyEvent.VK_SPACE) {
            if(!isFail) {
                isStart = !isStart;
            } else {
                init();
            }
            repaint(); //刷新页面
        }
        //键盘控制走向
        if(!fx.equals("R") && keyCode == KeyEvent.VK_LEFT) {
            fx = "L";
        } else if(!fx.equals("L") && keyCode == KeyEvent.VK_RIGHT) {
            fx = "R";
        } else if(!fx.equals("D") && keyCode == KeyEvent.VK_UP) {
            fx = "U";
        } else if(!fx.equals("U") && keyCode == KeyEvent.VK_DOWN) {
            fx = "D";
        }
    }


    //定时器，监听时间流动，游戏控制主体
    @Override
    public void actionPerformed(ActionEvent e) {
        //如果游戏处于开始状态并且游戏没有失败
        if(isStart && !isFail) {
            for(int i = length-1; i > 0; --i) {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }
            //移动方向处理
            if(fx.equals("R")) {
                snakeX[0] += 25;
                if(snakeX[0] > 850) snakeX[0] = 25;
            } else if(fx.equals("L")) {
                snakeX[0] -= 25;
                if(snakeX[0] < 25) snakeX[0] = 850;
            } else if(fx.equals("U")) {
                snakeY[0] -= 25;
                if(snakeY[0] < 75) snakeY[0] = 650;
            } else if(fx.equals("D")) {
                snakeY[0] += 25;
                if(snakeY[0] > 650) snakeY[0] = 75;
            }
            //判断是否吃到食物
            if(snakeX[0] == foodx && snakeY[0] == foody) {
                length++;
                snakeX[length-1] = snakeX[length-2];
                snakeY[length-1] = snakeY[length-2];
                createFood();
                score += 10;
                //积分越高，速度越快
                if(score > 200 && score < 400) {
                    timer.setDelay(90);
                } else if(score >= 400 && score < 600) {
                    timer.setDelay(75);
                } else if(score >= 600) {
                    timer.setDelay(50);
                }
            }
            //判断是否游戏失败
            for(int i = 1; i < length; ++i) {
                if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                    isFail = true;
                }
            }
            //刷新界面
            repaint();
        }
        timer.start();
    }
} 
