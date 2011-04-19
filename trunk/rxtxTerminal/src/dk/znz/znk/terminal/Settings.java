package dk.znz.znk.terminal;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import gnu.io.DataBits;
import gnu.io.StopBits;
import gnu.io.Parity;
import gnu.io.FlowControl;
import gnu.io.Speed;

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
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addComponent(mPortTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(mSpeedComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(mDataBitsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(mStopBitsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(mParityComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(mFlowControlComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addContainerGap(116, Short.MAX_VALUE))
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(mPortTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(mSpeedComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(mDataBitsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(mStopBitsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(mParityComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(mFlowControlComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addContainerGap(280, Short.MAX_VALUE))
    );
    setLayout(groupLayout);

  }
}
