package dk.znz.znk.terminal;

import gnu.io.CustomComboBox;
import gnu.io.DataBits;
import gnu.io.FlowControl;
import gnu.io.Parity;
import gnu.io.SerialPortInfo;
import gnu.io.Speed;
import gnu.io.StopBits;
import gnu.io.Utils;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Settings extends JPanel {

  private static final long serialVersionUID = 3493415047628642546L;
  
  private final JTextField mPortTextField;
  private final JTextField mSpeedTextField = new JTextField();
  private final JComboBox mPortComboBox;
  private final CustomComboBox mCustomComboBox;

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
    
    mCustomComboBox = new CustomComboBox();
    mCustomComboBox.setCustomObject(Speed.SPEED_CUSTOM);
    mCustomComboBox.getComboBox().setModel(new DefaultComboBoxModel(Speed.values()));
    
    FlowLayout flowLayout = (FlowLayout) mCustomComboBox.getLayout();
    flowLayout.setVgap(0);
    flowLayout.setHgap(0);
    
    add(mPortComboBox);
    add(mPortTextField);
    add(mSpeedComboBox);

    
    add(mSpeedTextField);
    mSpeedTextField.setColumns(10);
    add(mDataBitsComboBox);
    add(mStopBitsComboBox);
    add(mParityComboBox);
    add(mFlowControlComboBox);
    add(mCustomComboBox);

    mSpeedComboBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() != ItemEvent.SELECTED)
          return;
        Speed speed = (Speed)e.getItem();
        if(speed != null)
          mSpeedTextField.setVisible(speed == Speed.SPEED_CUSTOM);
      }
    });
  }
}
