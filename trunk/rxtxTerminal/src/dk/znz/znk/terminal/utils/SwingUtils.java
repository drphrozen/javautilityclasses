package dk.znz.znk.terminal.utils;

import java.awt.Container;

public class SwingUtils {
  public static Container updateLayout(Container container) {
    container.setLayout(container.getLayout());
    return container;
  }
}
