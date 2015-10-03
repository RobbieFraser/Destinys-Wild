package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class MultiKeyListener implements KeyListener{
	 // Set of currently pressed keys
    private final Set<Character> pressed = new HashSet<Character>();
    private GameInterface parent;
    
    public MultiKeyListener(GameInterface parent) {
    	this.parent = parent;
    }
    
    @Override
    public synchronized void keyPressed(KeyEvent e) {    	
        pressed.add(e.getKeyChar());   
        parent.handleKeyPress(e, pressed);
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyChar());
        parent.handleKeyRelease(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {/* Not used */ }
    
}
