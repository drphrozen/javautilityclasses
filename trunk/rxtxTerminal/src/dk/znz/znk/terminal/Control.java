package dk.znz.znk.terminal;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Control extends JPanel {

  private static final long serialVersionUID = -7436676470776887404L;

  /**
   * Create the panel.
   */
  public Control() {
    setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    
    JRadioButton cdRadioButton = new JRadioButton("CD");
    cdRadioButton.setEnabled(false);
    add(cdRadioButton);
    
    JRadioButton ctsRadioButton = new JRadioButton("CTS");
    ctsRadioButton.setEnabled(false);
    add(ctsRadioButton);
    
    JRadioButton dsrRadioButton = new JRadioButton("DSR");
    dsrRadioButton.setEnabled(false);
    add(dsrRadioButton);
    
    JRadioButton riRadioButton = new JRadioButton("RI");
    riRadioButton.setEnabled(false);
    add(riRadioButton);
    
    JCheckBox dtrCheckBox = new JCheckBox("DTR");
    add(dtrCheckBox);
    
    JCheckBox rtsCheckBox = new JCheckBox("RTS");
    add(rtsCheckBox);
  }

}
