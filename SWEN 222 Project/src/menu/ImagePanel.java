package menu;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImagePanel extends JComponent{
	private Image image;

	public ImagePanel(String fileName) {
		this.image = MenuInterface.loadImage(fileName);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}
