package de.vinado.ba.sender;

import java.util.concurrent.ExecutorService;

/**
 * Factory implementation for decorating any {@link MessageSender}.
 *
 * @author Vincent Nadoll
 */
public class AsyncMessageSenderFactory implements MessageSenderDecoratorFactory {

    private final ExecutorService threadPool;
    private final AsyncMessageSenderProperties properties;

    public AsyncMessageSenderFactory(ExecutorService threadPool, AsyncMessageSenderProperties properties) {
        this.threadPool = threadPool;
        this.properties = properties;
    }

    /**
     * Decorates the given {@link MessageSender} with a new instance of {@link AsyncMessageSender}.
     *
     * @param delegate the sender to be decorated
     * @return a new {@link AsyncMessageSender} instance
     */
    @Override
    public AsyncMessageSender decorate(MessageSender delegate) {
        return new AsyncMessageSender(threadPool, properties, delegate);
    }
}
