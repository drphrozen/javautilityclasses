package dk.znz.znk.terminal;

import gnu.io.DataBits;
import gnu.io.FlowControl;
import gnu.io.Parity;
import gnu.io.SerialPortInfo;
import gnu.io.Speed;
import gnu.io.StopBits;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Settings extends JPanel {

  private static final long serialVersionUID = 3493415047628642546L;
  
  private JTextField mPortTextField;
  private JTextField mSpeedTextField;
  private JComboBox mPortComboBox;

  /**
   * Create the panel.
   */
  public Settings() {
    
    mPortTextField = new JTextField();
    mPortTextField.setColumns(10);
    
    JComboBox mDataBitsComboBox = new JComboBox();
    mDataBitsComboBox.setModel(new DefaultComboBoxModel(DataBits.values()));
    
    JComboBox mStopBitsComboBox = new JComboBox();
    mStopBitsComboBox.setModel(new DefaultComboBoxModel(StopBits.values()));
    
    JComboBox mParityComboBox = new JComboBox();
    mParityComboBox.setModel(new DefaultComboBoxModel(Parity.values()));
    
    JComboBox mFlowControlComboBox = new JComboBox();
    mFlowControlComboBox.setModel(new DefaultComboBoxModel(FlowControl.values()));
    
    JComboBox mSpeedComboBox = new JComboBox();
    mSpeedComboBox.setModel(new DefaultComboBoxModel(Speed.values()));
    mSpeedComboBox.setSelectedIndex(11);
    setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    
    JButton button = new JButton("");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DefaultComboBoxModel model = (DefaultComboBoxModel)mPortComboBox.getModel();
        model.removeAllElements();
        for (SerialPortInfo serialPortInfo : SerialPortInfo.GetSerialPorts()) {
          model.addElement(serialPortInfo);
        }
      }
    });
    button.setIcon(new ImageIcon(Settings.class.getResource("/dk/znz/znk/terminal/images/arrow_refresh.png")));
    add(button);
    
    mPortComboBox = new JComboBox();
    add(mPortComboBox);
    add(mPortTextField);
    add(mSpeedComboBox);

    
    mSpeedTextField = new JTextField();
    add(mSpeedTextField);
    mSpeedTextField.setColumns(10);
    add(mDataBitsComboBox);
    add(mStopBitsComboBox);
    add(mParityComboBox);
    add(mFlowControlComboBox);
  }
}
