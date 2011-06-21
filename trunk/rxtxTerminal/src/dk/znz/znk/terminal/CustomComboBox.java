package dk.znz.znk.terminal;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dk.znz.znk.terminal.utils.SwingUtils;

public class CustomComboBox extends JPanel {
  
  private static final long serialVersionUID = -7948010382375058636L;
  
  private final JTextField mTextField;
  private final JComboBox  mComboBox;
  private boolean          mSelected     = false;
  private Object           mCustomObject = null;

  public CustomComboBox() {
    mComboBox = new InternalComboBox();
    mTextField = new JTextField();
    mTextField.setColumns(10);
    add(mComboBox);
  }

  public CustomComboBox(ComboBoxModel aModel) {
    mComboBox = new InternalComboBox(aModel);
    mTextField = new JTextField();
    mTextField.setColumns(10);
    add(mComboBox);
  }

  public CustomComboBox(Object[] items) {
    mComboBox = new InternalComboBox(items);
    mTextField = new JTextField();
    mTextField.setColumns(10);
    add(mComboBox);
  }

  public CustomComboBox(Vector<?> items) {
    mComboBox = new InternalComboBox(items);
    mTextField = new JTextField();
    mTextField.setColumns(10);
    add(mComboBox);
  }

  public void setCustomObject(Object customObject) {
    mCustomObject = customObject;
  }

  public JTextField getTextField() {
    return mTextField;
  }

  public JComboBox getComboBox() {
    return mComboBox;
  }

  public boolean isSelected() {
    return mSelected;
  }

  private class InternalComboBox extends JComboBox {
    private static final long serialVersionUID = 1L;

    public InternalComboBox() {
      super();
    }

    public InternalComboBox(ComboBoxModel aModel) {
      super(aModel);
    }

    public InternalComboBox(Object[] items) {
      super(items);
    }

    public InternalComboBox(Vector<?> items) {
      super(items);
    }

    @Override protected void fireItemStateChanged(ItemEvent e) {
      if (mCustomObject != null && e.getItem() == mCustomObject) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          if (isSelected() == false) {
            add(mTextField);
            Container me = CustomComboBox.this.getParent();
            Container parent = CustomComboBox.this.getParent();
            me.setLayout(me.getLayout());
            me.validate();
            parent.setLayout(parent.getLayout());
            parent.validate();
            
            mSelected = true;
            System.out.println(mSelected);
          }
        } else {
          if (isSelected() == true) {
            remove(mTextField);
            SwingUtils.updateLayout(CustomComboBox.this).invalidate();
            SwingUtils.updateLayout(CustomComboBox.this.getParent()).validate();
            mSelected = false;
            System.out.println(mSelected);
          }
        }
      }
      super.fireItemStateChanged(e);
    }
    
  }
}
