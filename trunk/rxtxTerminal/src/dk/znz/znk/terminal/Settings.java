package dk.znz.znk.terminal;

import gnu.io.DataBits;
import gnu.io.FlowControl;
import gnu.io.Parity;
import gnu.io.Speed;
import gnu.io.StopBits;

import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Settings extends JPanel {

  private static final long serialVersionUID = 3493415047628642546L;
  
  private JTextField mPortTextField;

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
    add(mPortTextField);
    add(mSpeedComboBox);
    add(mDataBitsComboBox);
    add(mStopBitsComboBox);
    add(mParityComboBox);
    add(mFlowControlComboBox);

  }
}
