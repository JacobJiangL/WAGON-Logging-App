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
//import com.google.api.services.sheets.v4.SheetsScopes;
//import com.google.api.services.sheets.v4.model.ValueRange;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;


// awt/swing imports
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

        import javax.swing.JPanel;
import javax.swing.Timer;

public class GUI extends JPanel implements ActionListener, MouseListener {

    protected enum PageType {
        REQUEST_LIST,
        REQUEST_DETAILS
    }

    private final Timer timer;
    private final int delay = 20;

    protected PageContainer currentPage;
    protected Processor processor;

    public GUI(Processor processor) {
        this.processor = processor;
        this.currentPage = new PageContainer(this, PageType.REQUEST_LIST);

        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        this.timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {currentPage.draw((Graphics2D) g);}

    private void checkButtonPresses(int xClick, int yClick) {
        for(ButtonComponent button : currentPage.getButtons()) {
            if(button.checkPressed(xClick, yClick)) {
                button.executeCommand();
            }
        }
        for(ListComponent list : currentPage.lists) {
            if(list.scrollBar.checkPressed(xClick, yClick)) {
                list.scrollBar.scroll(yClick);
            }
        }
    }

    private void releaseButtons() {
        for(ButtonComponent button : currentPage.getButtons()) {
            button.release();
        }
        for(ListComponent list : currentPage.lists) {
            list.scrollBar.release();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(ListComponent list : currentPage.lists) {
            if(list.scrollBar.pressed) {
                try {
                    list.scrollBar.scroll(getMousePosition().y);
                } catch(NullPointerException ignore) { }
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent press) {
        checkButtonPresses(press.getX(), press.getY());
    }

    @Override
    public void mouseReleased(MouseEvent release) {
        releaseButtons();
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
