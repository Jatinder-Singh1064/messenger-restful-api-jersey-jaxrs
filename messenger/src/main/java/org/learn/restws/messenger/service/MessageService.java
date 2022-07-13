package org.learn.restws.messenger.service;

import java.util.*;
import org.learn.restws.messenger.database.DatabaseClass;
import org.learn.restws.messenger.exception.DataNotFoundException;
import org.learn.restws.messenger.model.Message;

public class MessageService {

	private Map<Long, Message> messages = DatabaseClass.getMessages();

	public MessageService() {
		messages.put(1L, new Message(1, "Hello World!!", "Jatinder"));
		messages.put(2L, new Message(2, "JAX-RS Jersey!!", "Jatinder"));
	}

	public List<Message> getAllMessages() {
		return new ArrayList<Message>(messages.values());
	}

	public List<Message> getAllMessagesForYear(int year) {
		List<Message> messagesForYear = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		
		for(Message msg : messages.values()) {
			cal.setTime(msg.getCreated());
			if(cal.get(Calendar.YEAR) == year) {
				messagesForYear.add(msg);
			}
		}
		return messagesForYear;
	}

	public List<Message> getAllMessagesPaginated(int start, int size) {
		ArrayList<Message> list = new ArrayList<Message>(messages.values());
		
		if((start + size) > list.size()) return new ArrayList<Message>();
		
		return list.subList(start, start + size);
	}

	public Message getMessage(long id) {
		Message message = messages.get(id);
		if(message == null) {
			throw new DataNotFoundException("Message with id " + id + " not found");
		}
		return message;
	}

	public Message addMessage(Message message) {
		message.setId(messages.size() + 1);
		messages.put(message.getId(), message);
		return message;
	}

	public Message updateMessage(Message message) {
		if (message.getId() <= 0) {
			return null;
		}
		messages.put(message.getId(), message);
		return message;
	}

	public Message removeMessage(long id) {
		return messages.remove(id);
	}
}
