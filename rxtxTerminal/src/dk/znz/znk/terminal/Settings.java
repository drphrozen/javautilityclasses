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
  private JButton mRefreshButton;

  /**
   * Create the panel.
   */
  public Settings() {
    
    JComboBox mDataBitsComboBox = new JComboBox();
    mDataBitsComboBox.setToolTipText("Data bits");
    mDataBitsComboBox.setModel(new DefaultComboBoxModel(DataBits.values()));
    
    JComboBox mStopBitsComboBox = new JComboBox();
    mStopBitsComboBox.setToolTipText("Stop bits");
    mStopBitsComboBox.setModel(new DefaultComboBoxModel(StopBits.values()));
    
    JComboBox mParityComboBox = new JComboBox();
    mParityComboBox.setToolTipText("Parity");
    mParityComboBox.setModel(new DefaultComboBoxModel(Parity.values()));
    
    JComboBox mFlowControlComboBox = new JComboBox();
    mFlowControlComboBox.setToolTipText("Flow control");
    mFlowControlComboBox.setModel(new DefaultComboBoxModel(FlowControl.values()));
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    
    mRefreshButton = new JButton("");
    mRefreshButton.setToolTipText("Refresh ports");
    mRefreshButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DefaultComboBoxModel model = (DefaultComboBoxModel)mPortComboBox.getModel();
        model.removeAllElements();
        for (SerialPortInfo serialPortInfo : Utils.getSerialPorts()) {
          model.addElement(serialPortInfo);
        }
      }
    });
    mRefreshButton.setIcon(new ImageIcon(Settings.class.getResource("/dk/znz/znk/terminal/images/arrow_refresh.png")));
    add(mRefreshButton);
    
    mPortComboBox = new JComboBox(new DefaultComboBoxModel(Utils.getSerialPorts()));
    mPortComboBox.setToolTipText("Port");
    mPortComboBox.setEditable(true);
    System.out.println(mPortComboBox.getEditor());
//    mPortComboBox.setEditor(new BasicComboBoxEditor() {
//      @Override public Component getEditorComponent() {
//        Component c =super.getEditorComponent();
//        System.out.println(c.getClass().getName() + ": " + c.toString());
//        return c;
//      }
//    });
//    mPortComboBox.setRenderer(new ListCellRenderer() {
//      @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//        System.out.println("[" + index + "]" + value.getClass().getName() + ": " + value.toString() + " selected: " + isSelected + " cellHasFocus: " + cellHasFocus);
//        
//        return new JLabel(value.toString(), JLabel.RIGHT);
//      }
//    });
    add(mPortComboBox);
    
    mSpeedComboBox = new JComboBox(new Vector<String>(Utils.DefaultSpeeds));
    mSpeedComboBox.setToolTipText("Speed");
    mSpeedComboBox.setEditable(true);
    
    add(mSpeedComboBox);
    add(mDataBitsComboBox);
    add(mParityComboBox);
    add(mStopBitsComboBox);
    add(mFlowControlComboBox);
  }
}
