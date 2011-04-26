package gnu.io;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class CustomComboBox extends JPanel {
  private static final long serialVersionUID = -514522084337155860L;

  private final JTextField mTextField;
  private final JComboBox mComboBox;
  private Object mCustomObject;
  private boolean mIsCustom = false;

  public CustomComboBox() {
    FlowLayout flowLayout = (FlowLayout) getLayout();
    flowLayout.setHgap(0);
    flowLayout.setVgap(0);
    flowLayout.setAlignment(FlowLayout.LEFT);
    
    mComboBox = new JComboBox();
    getComboBox().addItemListener(new ItemListener() {
      @Override public void itemStateChanged(ItemEvent e) {
        if(e.getItem() == getCustomObject()) {
          switch(e.getStateChange()) {
          case ItemEvent.SELECTED:
            setCustom(true);
            break;
          case ItemEvent.DESELECTED:
            setCustom(false);
            break;
          }
        }
      }
    });
    add(getComboBox());
    
    mTextField = new JTextField();
    mTextField.setColumns(10);
  }

  public JTextField getTextField() {
    return mTextField;
  }

  public JComboBox getComboBox() {
    return mComboBox;
  }
  

  public boolean isCustom() {
    return mIsCustom;
  }

  public void setCustom(boolean custom) {
    if(mIsCustom == custom) return;
    mIsCustom = custom;
    if(mIsCustom) {
      add(mTextField);
      revalidate();
    } else {
      remove(mTextField);
      revalidate();
    }
  }

  public void setCustomObject(Object customObject) {
    mCustomObject = customObject;
  }

  public Object getCustomObject() {
    return mCustomObject;
  }
}
