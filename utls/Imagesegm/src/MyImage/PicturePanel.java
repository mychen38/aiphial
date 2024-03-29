/*
 *
 * This file is part of Aiphial.
 *
 * Copyright (c) 2010 Nicolay Mitropolsky <NicolayMitropolsky@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 * PicturePanel.java
 *
 * Created on 27 Декабрь 2007 г., 22:22
 */
package MyImage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author  Nickl
 */
public class PicturePanel extends javax.swing.JPanel
{
    // Храним 2 изображения: оригинальное и текущее.
    // Оригинальное используется для получения текущего в зависимости от размеров панели.
    // Текущее непосредственно прорисовывается на панели.
    private BufferedImage originalImage = null;
    private Image image = null;

    public PicturePanel()
    {
        initComponents();
        setLayout(null);
        addComponentListener(new java.awt.event.ComponentAdapter()
        {

            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                formComponentResized(evt);
            }
        });

    }

    // Реакция на изменение размеров панели - изменение размера изображения.
    private void formComponentResized(java.awt.event.ComponentEvent evt)
    {
        int w = this.getWidth();
        int h = this.getHeight();
        if ((originalImage != null) && (w > 0) && (h > 0))
        {
            image = originalImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            this.repaint();
        }
    }

    // Берем прорисовку в свои руки.
    public void paint(Graphics g)
    {
        // Рисуем картинку
        if (image != null)
        {
            g.drawImage(image, 0, 0, null);
        }

        // Рисуем подкомпоненты.
        super.paintChildren(g);
        // Рисуем рамку        
        super.paintBorder(g);
    }

    // Методы для настройки картинки.
    public BufferedImage getImage()
    {
        return originalImage;
    }

    public void setImage(BufferedImage image)
    {
        this.originalImage = image;
        
        formComponentResized(null);
        repaint();
    }

    public void setImageFile(File imageFile)
    {
        try
        {
            if (imageFile == null)
            {
                originalImage = null;
            }
            BufferedImage bi = ImageIO.read(imageFile);
            originalImage = bi;
        } catch (IOException ex)
        {
            System.err.println("Неудалось загрузить картинку!");
            ex.printStackTrace();
        }

        formComponentResized(null);

        repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        setName("Form"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
