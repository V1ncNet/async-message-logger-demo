package de.vinado.ba;

import de.vinado.ba.sender.MessageSender;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main view of the application, showing input and ountput elements needed to interact with the {@link
 * MessageSender}.
 *
 * @author Vincent Nadoll
 */
public class MainActivity extends JFrame implements Observer {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private JTextArea logArea;

    public MainActivity(AtomicInteger model, MessageSender sender, Observable observable) throws HeadlessException {
        super("Asynchronous Message Logger");

        createInputPanel(model, sender);
        createOutputPanel();

        observable.addObserver(this);
        setVisible(true);
        pack();
    }

    private void createInputPanel(AtomicInteger model, MessageSender sender) {
        JPanel inputPanel = new JPanel();

        JTextField amount = new JTextField("30", 10);
        inputPanel.add(amount);

        Controller controller = new Controller(model, amount, sender);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(controller);
        inputPanel.add(sendButton);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void createOutputPanel() {
        JPanel logPanel = new JPanel();

        logArea = new JTextArea(24, 64);
        logArea.setEditable(false);
        logArea.setAutoscrolls(true);
        logPanel.add(logArea);

        add(logPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void frameInit() {
        super.frameInit();

        setLocationRelativeTo(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof AppendingMessageSender) {
            String payload = String.valueOf(arg);
            logArea.setText(payload);
        }
    }
}
