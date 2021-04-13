import org.jsoup.Jsoup;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class VeryBasicBrowser extends JPanel implements HyperlinkListener {
    public static void main(String[] args)  {
        SwingUtilities.invokeLater(VeryBasicBrowser::startGui);
    }

    static void startGui() {
        JFrame frame = new JFrame("Very Basic Browser");
        frame.add(new VeryBasicBrowser());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    JTextField urlField, statusField;
    JEditorPane editorPane;

    VeryBasicBrowser() {
        super(new BorderLayout());
        urlField = new JTextField(80);
        urlField.addActionListener(e -> urlUpdate(e.getActionCommand()));
        urlField.setFocusAccelerator('L');
        add(urlField, BorderLayout.PAGE_START);
        HTMLEditorKit kit = new HTMLEditorKit();
        kit.setAutoFormSubmission(false);
        editorPane = new JEditorPane("text/html", "");
        editorPane.addHyperlinkListener(this);
        editorPane.setEditable(false);
        editorPane.setEditorKit(kit);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);
        statusField = new JTextField();
        statusField.setEditable(false);
        add(statusField, BorderLayout.PAGE_END);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        String url = event.getURL().toString();
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            urlField.setText(url);
            urlUpdate(url);
        }
        statusField.setText((event.getEventType() == HyperlinkEvent.EventType.EXITED) ? "" : url);
    }

    void urlUpdate(String url) {
        final String newUrl;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            newUrl = "http://" + url;
            urlField.setText(newUrl);
        } else {
            newUrl = url;
        }
        SwingWorker<String, Void> updater = new SwingWorker<>() {
            @Override
            public String doInBackground() {
                String result = "";
                try {
                    org.jsoup.nodes.Document soupDoc = Jsoup.connect(newUrl).get();
                    result = new Cleaner(Whitelist.relaxed()).clean(soupDoc).html();
                } catch (IOException e) {
                    statusField.setText(String.format("Malformed url: |%s|.", newUrl));
                }
                return result;
            }
            @Override
            public void done() {
                try {
                    String text = get();
                    if (text.isEmpty()) {
                        statusField.setText(String.format("Nothing returned for the url |%s|.", newUrl));
                    }
                    editorPane.setText(text);
                    editorPane.setCaretPosition(0);
                } catch (InterruptedException e) {
                    statusField.setText(String.format("Something interrupted getting the url |%s|.\n", url));
                } catch (ExecutionException e) {
                    statusField.setText(String.format("An execution error occurred when getting the url |%s|.\n", url));
                }
            }
        };
        updater.execute();
    }
}