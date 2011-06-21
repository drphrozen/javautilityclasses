package dk.znz.znk.terminal;

import gnu.io.DataBits;
import gnu.io.FlowControl;
import gnu.io.Parity;
import gnu.io.SerialPortInfo;
import gnu.io.StopBits;
import gnu.io.Utils;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class Settings extends JPanel {

  private static final long serialVersionUID = 3493415047628642546L;

  private final JComboBox mPortComboBox;
  private final JComboBox mSpeedComboBox;

  /**
   * Create the panel.
   */
  public Settings() {
    
    JComboBox mDataBitsComboBox = new JComboBox();
    mDataBitsComboBox.setModel(new DefaultComboBoxModel(DataBits.values()));
    
    JComboBox mStopBitsComboBox = new JComboBox();
    mStopBitsComboBox.setModel(new DefaultComboBoxModel(StopBits.values()));
    
    JComboBox mParityComboBox = new JComboBox();
    mParityComboBox.setModel(new DefaultComboBoxModel(Parity.values()));
    
    JComboBox mFlowControlComboBox = new JComboBox();
    mFlowControlComboBox.setModel(new DefaultComboBoxModel(FlowControl.values()));
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    
    JButton button = new JButton("");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DefaultComboBoxModel model = (DefaultComboBoxModel)mPortComboBox.getModel();
        model.removeAllElements();
        for (SerialPortInfo serialPortInfo : Utils.getSerialPorts()) {
          model.addElement(serialPortInfo);
        }
      }
    });
    button.setIcon(new ImageIcon(Settings.class.getResource("/dk/znz/znk/terminal/images/arrow_refresh.png")));
    add(button);
    
    mPortComboBox = new JComboBox(new DefaultComboBoxModel(Utils.getSerialPorts()));
    
    mSpeedComboBox = new JComboBox(new Vector<String>(Utils.DefaultSpeeds));
    mSpeedComboBox.setEditable(true);
    add(mSpeedComboBox);
    
    add(mPortComboBox);
    add(mDataBitsComboBox);
    add(mStopBitsComboBox);
    add(mParityComboBox);
    add(mFlowControlComboBox);
  }
}
