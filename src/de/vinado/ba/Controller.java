package de.vinado.ba;

import de.vinado.ba.sender.MessageSender;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * An {@link ActionListener} implementation acting as the controller for the {@link MainActivity}'s input text field.
 *
 * @author Vincent Nadoll
 */
public class Controller implements ActionListener {

    private final AtomicInteger model;
    private final JTextField amountTf;
    private final MessageSender sender;

    public Controller(AtomicInteger model, JTextField amount, MessageSender sender) {
        this.model = model;
        this.amountTf = amount;
        this.sender = sender;
    }

    /**
     * Extracts the user input, adds the extracted value to the model and extracts each integer between the previous
     * amount. The result gets delegated to the {@link MessageSender} afterwards.
     * <p>
     * An error dialog will be shown in case the user input is not an integer.
     *
     * @param event the emitted event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            int amount = extractInput();
            String[] messages = IntStream.range(model.get(), model.addAndGet(amount))
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new);
            sender.send(messages);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Text field input must be an integer",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int extractInput() {
        String rawInput = amountTf.getText();
        return Integer.parseInt(rawInput);
    }
}
