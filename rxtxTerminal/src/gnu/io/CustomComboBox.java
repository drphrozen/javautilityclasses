package gnu.io;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class CustomComboBox extends JPanel {
  private static final long serialVersionUID = -514522084337155860L;

  private final JTextField mTextField;
  private final JComboBox mComboBox;
  private boolean mIsCustom = false;

  public CustomComboBox() {
    FlowLayout flowLayout = (FlowLayout) getLayout();
    flowLayout.setHgap(0);
    flowLayout.setVgap(0);
    flowLayout.setAlignment(FlowLayout.LEFT);
    
    mComboBox = new JComboBox();
    add(mComboBox);
    
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
}
