package de.vinado.ba;

import de.vinado.ba.sender.AsyncMessageSenderProperties;
import de.vinado.ba.sender.AsyncMessageSenderFactory;
import de.vinado.ba.sender.MessageSender;

import javax.swing.SwingUtilities;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The entrypoint into the application.
 *
 * @author Vincent Nadoll
 */
public class SenderApplication {

    public static void main(String[] args) {
        AtomicInteger model = new AtomicInteger();
        AppendingMessageSender observable = new AppendingMessageSender();
        MessageSender sender = decorate(observable);

        SwingUtilities.invokeLater(() -> new MainActivity(model, sender, observable));
    }

    private static MessageSender decorate(MessageSender delegate) {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        AsyncMessageSenderProperties properties = new AsyncMessageSenderProperties();
        applyProperties(properties);

        AsyncMessageSenderFactory factory = new AsyncMessageSenderFactory(threadPool, properties);
        return factory.decorate(delegate);
    }

    private static void applyProperties(AsyncMessageSenderProperties properties) {
        properties.setChunkSize(20);
        properties.setCooldownMillis(2 * 1000); // 2 seconds
    }
}
