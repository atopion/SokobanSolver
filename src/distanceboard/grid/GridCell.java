package distanceboard.grid;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class GridCell extends JPanel implements MouseListener
{
    private CellClickedListener listener;
    private int x;
    private int y;

    // 0 - normal, 1 - wall, 2 - box, 3 - goal, 4 - path vertical, 5 - path horizontal, 6 - path cross
    private int type = 0;

    private JLabel label;

    private String boxString     = /*"data:image/png;base64,*/"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";
    private String targetString  = /*"data:image/png;base64,*/"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";
    private String path_tdString = /*"data:image/png;base64,*/"iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAB3SURBVGhD7c9BCYBAAAXRBRsYQzCHzUxgB8EWYhpT6Pcm4llnYR5MgCmSftGmPR2PplSVPj0nrrZUFUdoHKFxhMYRGkdoHKFxhMYRGkdoHKFxhMYRGkdoHKFxhMYRGkdoHKFxhKZLbyNrqkqTxrTcmtOQJH2ulBNIovW0LUFpjwAAAABJRU5ErkJggg==";
    private String path_lrString = /*"data:image/png;base64,*/"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";
    private String path_crString = /*"data:image/png;base64,*/"iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAEC0lEQVR4Xu2dMYoVURREaxBjY5dirrgETQZz9yCCSzAxMBDExB24AlMXYS4aCoLS6fjnU1D31/RrjnH9++6cV54x8Hdfaf0/DyV9kPRY0r0L/Di/JL2V9FrS3wvMv9ORV3d6+szhXyQ9nRl1dsoLSR8L51SPOEIB/lzob/7Ni/gk6bp6O4XDjlCAlpY/S3peuJPqERTAx00BfFbVJAYIcGMAHx4G8FlVkxggwI0BfHgYwGdVTWKAADcG8OFhAJ9VNYkBAtwYwIeHAXxW1SQGCHBjAB8eBvBZVZMYIMCNAXx4GMBnVU1igAA3BvDhYQCfVTWJAQLcGMCHhwF8VtUkBghwYwAfHgbwWVWTGCDAjQF8eBjAZ1VNYoAANwbw4WEAn1U1iQEC3BjAh4cBfFbVJAYIcGMAHx4G8FlVkxggwI0BfHgYwGdVTWKAADcG8OFhAJ9VNYkBAtwYwIeHAXxW1SQGCHBjAB8eBvBZVZMYIMCNAXx4GMBnVU1igAA3BvDhYQCfVTWJAQLcGMCHhwF8VtUkBghwYwAfHgbwWVWTGCDAvRng0s/bD9bjowMEzr7vYCtA63n7Az8LIwICJ993sBWg9bz9YHc+OkDg5PsOtgK0focO/AyMCAic/EcsBQiILvZRCrDYhU2vSwGmiS42jwIsdmHT61KAaaKLzaMAi13Y9LoUYJroYvMowGIXNr0uBZgmutg8CrDYhU2vSwGmiS42jwIsdmHT61KAaaKLzaMAi13Y9LoUYJroYvMowGIXNr0uBZgmutg8CrDYhU2vSwGmiS42jwIsdmHT695agJ+SHkyfxrzdEXgn6eXNrbb/FPpG0qvdrctCkwR+S3ok6dupAmwluJb0RNL9yVNLs56Vzvku6WvprMljfkh6f+ryt0P4cqiPmi+H+qyqydYXWyhA9Vr9wyiAz+q/JL8CfHgYwGdVTWKAADcG8OFhAJ9VNYkBAtwYwIeHAXxW1SQGCHBjAB8eBvBZVZMYIMCNAXx4GMBnVU1igAA3BvDhYQCfVTWJAQLcGMCHhwF8VtUkBghwYwAfHgbwWVWTGCDAjQF8eBjAZ1VNYoAANwbw4WEAn1U1iQEC3BjAh4cBfFbVJAYIcGMAHx4G8FlVkxggwI0BfHgYwGdVTWKAADcG8OFhAJ9VNYkBAtwYwIeHAXxW1SQGCHBjAB8eBvBZVZMYIMCNAXx4GMBnVU1igAA3BvDhYQCfVTWJAQLcGMCHhwF8VtUkBghwYwAfHgbwWVWTGCDAjQF8eBjAZ1VNYoAA9xEM0Hrfwcnn7Qfsd/HRIxSg8b6DW5+3v4tbDJY4QgEu/b6Ds8/bD9jv4qNHKMAuQK66BAVY9eaG9qYAQyBXHUMBVr25ob0pwBDIVcf8A7KzAJDPJYpnAAAAAElFTkSuQmCC";

    private BufferedImage box, target, path_td, path_lr, path_cr;
    private ImageIcon boxIcon, targetIcon, path_td_Icon, path_lr_Icon, path_cr_Icon;

    public static interface CellClickedListener
    {
        public void clicked(GridCell cell);
    }

    public GridCell(CellClickedListener listener, int x, int y)
    {
        super();
        this.listener = listener;
        this.addMouseListener(this);
        this.x = x;
        this.y = y;

        this.setLayout(new GridBagLayout());
        label = new JLabel();
        this.add(label);

        new Thread(() -> {
                byte[] arr1 = Base64.getMimeDecoder().decode(boxString);
                try { box = ImageIO.read(new ByteArrayInputStream(arr1)); } catch (Exception e) { e.printStackTrace(); }
                byte[] arr2 = Base64.getMimeDecoder().decode(targetString);
                try { target = ImageIO.read(new ByteArrayInputStream(arr2)); } catch (Exception e) { e.printStackTrace(); }
                byte[] arr3 = Base64.getMimeDecoder().decode(path_tdString);
                try { path_td = ImageIO.read(new ByteArrayInputStream(arr3)); } catch (Exception e) { e.printStackTrace(); }
                byte[] arr4 = Base64.getMimeDecoder().decode(path_lrString);
                try { path_lr = ImageIO.read(new ByteArrayInputStream(arr4)); } catch (Exception e) { e.printStackTrace(); }
                byte[] arr5 = Base64.getMimeDecoder().decode(path_crString);
                try { path_cr = ImageIO.read(new ByteArrayInputStream(arr5)); } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(50, 50);
    }

    public Point getPosition()
    {
        return new Point(x, y);
    }

    public void setColor(Color color)
    {
        this.setBackground(color);
    }

    public void reset()
    {
        this.type = 0;
        this.setBackground(new Color(210, 210, 210));
        this.label.setText("");
        this.label.setIcon(null);
    }

    public boolean isNormal()
    {
        return this.type == 0;
    }

    public void setWall()
    {
        this.type = 1;
        this.setBackground(Color.darkGray);
    }

    public boolean isWall()
    {
        return this.type == 1;
    }

    public void setBox()
    {
        this.type = 2;
        this.label.setIcon(boxIcon);
    }

    public boolean isBox()
    {
        return this.type == 2;
    }

    public void setTarget()
    {
        this.type = 3;
        this.label.setIcon(targetIcon);
    }

    public boolean isTarget()
    {
        return this.type == 3;
    }

    public void setPath_vert()
    {
        this.resizeIcons();
        if(this.isPath_horiz())
            this.setPath_cross();
        else {
            this.type = 4;
            this.label.setIcon(path_td_Icon);
        }
    }

    public boolean isPath_vert() { return this.type == 4; }

    public void setPath_horiz()
    {
        this.resizeIcons();
        if(this.isPath_vert())
            this.setPath_cross();
        else {
            this.type = 5;
            this.label.setIcon(path_lr_Icon);
        }

    }

    public boolean isPath_horiz() { return this.type == 5; }

    public void setPath_cross()
    {
        this.resizeIcons();
        this.type = 6;
        this.label.setIcon(path_cr_Icon);
    }

    public boolean isPath_cross() { return this.type == 6; }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        this.resizeIcons();
        this.listener.clicked(this);
    }

    private void resizeIcons()
    {
        boxIcon      = new ImageIcon(box.getScaledInstance((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.8), Image.SCALE_SMOOTH));
        targetIcon   = new ImageIcon(target.getScaledInstance((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.8), Image.SCALE_SMOOTH));
        path_cr_Icon = new ImageIcon(path_cr.getScaledInstance((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.8), Image.SCALE_SMOOTH));
        path_lr_Icon = new ImageIcon(path_lr.getScaledInstance((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.8), Image.SCALE_SMOOTH));
        path_td_Icon = new ImageIcon(path_td.getScaledInstance((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.8), Image.SCALE_SMOOTH));
    }
}
