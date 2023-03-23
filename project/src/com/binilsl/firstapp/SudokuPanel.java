package com.binilsl.firstapp;

import javax.swing.*;
import java.awt.*;

public class SudokuPanel extends JPanel {

    private SudokuPuzzle puzzle;
    public SudokuPanel() {
        this.setPreferredSize(new Dimension(540, 450));
        this.puzzle = SudokuGenerator.generateRandomSudoku(SudokuPuzzleType.NINEBYNINE);
        System.out.println(this.puzzle);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 255, 255)); //set colour as white

        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2d.setColor(new Color(0, 0, 0));
        int slotWidth = this.getWidth() / 9;
        int slotHeight = this.getHeight() /9;

        for(int x = 0; x <= getWidth(); x += slotWidth){
            if ((x / slotWidth) % 3 == 0){

                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x, 0, x, getHeight());
            }
            else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y <= this.getHeight(); y += slotHeight){

                if((y / slotHeight) % 3 == 0){
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(0, y, this.getWidth(), y);

                }
                else {
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawLine(0, y, this.getWidth(), y);
                }
            }
        }
    }
}
