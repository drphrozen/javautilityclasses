package dk.au.perpos.tailing.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import com.google.protobuf.GeneratedMessage;

@SuppressWarnings("unchecked")
public class MessagePublisher {

	interface MessageSubscriber<T extends GeneratedMessage> {
		void OnSignal(final T value);
	}

	public static final MessagePublisher instance = new MessagePublisher();
	private final Logger log = Logger.getLogger(MessagePublisher.class.getName());
	private final BlockingQueue<GeneratedMessage> queue = new ArrayBlockingQueue<GeneratedMessage>(20);
	private final HashMap<Class<? extends GeneratedMessage>, List<MessageSubscriber>> signalListenersMap = new HashMap<Class<? extends GeneratedMessage>, List<MessageSubscriber>>();
	
	private final Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					GeneratedMessage message = queue.take();
					log.info(message.getClass().getName());
					synchronized (signalListenersMap) {
						Class<? extends GeneratedMessage> key = message.getClass();
						if (!signalListenersMap.containsKey(key))
							continue;
						List<MessageSubscriber> Tsignals = signalListenersMap.get(message.getClass());
						synchronized (Tsignals) {
							for (MessageSubscriber signalListener : Tsignals) {
								signalListener.OnSignal(message);
							}
						}
					}
				} catch (InterruptedException e) {
					signalListenersMap.clear();
					return;
				}
			}
		}
	});

	private MessagePublisher() {
	}

	public void start() {
		thread.start();
	}

	public void stop() {
		thread.interrupt();
	}

	public <T extends GeneratedMessage> void addMessageSubscriber(MessageSubscriber<T> listener, Class<T> c) {
		synchronized (signalListenersMap) {
			if (signalListenersMap.containsKey(c)) {
				signalListenersMap.get(c).add(listener);
			} else {
				List<MessageSubscriber> list = Collections.synchronizedList(new LinkedList<MessageSubscriber>());
				list.add(listener);
				signalListenersMap.put(c, list);
			}
		}
	}

	public <T extends GeneratedMessage> void removeMessageSubscriber(MessageSubscriber<T> listener, Class<T> c) {
		synchronized (signalListenersMap) {
			if (signalListenersMap.containsKey(c)) {
				signalListenersMap.get(c).remove(listener);
			}
		}
	}

	public <T extends GeneratedMessage> boolean Publish(final T value) {
		log.info(value.getClass().getName());
		return queue.add(value);
	}
}
