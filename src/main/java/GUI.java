// google api imports
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
//import com.google.api.services.sheets.v4.SheetsScopes;
//import com.google.api.services.sheets.v4.model.ValueRange;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;


import java.util.ArrayList;

// awt/swing imports
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GUI extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final Timer timer;
    private final int delay = 8;
    private ArrayList<Button> buttons = new ArrayList<>();
    protected Sheets sheetService;

    public GUI(Sheets sheetService) {
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        this.sheetService = sheetService;
        this.timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {

    }

    private void checkButtonPresses(int xClick, int yClick) {
        for(Button button : buttons) {
            if(button.checkPressed(xClick, yClick)) {
                executeButtonCommand(button);
            }
        }
    }
    private void releaseButtons() {
        for(Button button : buttons) {
            button.release();
        }
    }
    private void executeButtonCommand(Button button) {
        switch(button.getIdentifier()) {
            default:
                break;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent press) {

    }

    @Override
    public void keyReleased(KeyEvent release) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent press) {
        checkButtonPresses(press.getX(), press.getY());
    }

    @Override
    public void mouseReleased(MouseEvent release) {
        releaseButtons();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
