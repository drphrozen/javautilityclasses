package dk.iha.and;

import dk.iha.and.message.Message;

public interface OnMessageListener {
  boolean onMessage(Message message);
}