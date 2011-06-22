package dk.znz.znk.terminal;

public class SerialSettings {
  private int mSpeed;
  private int mDataBits;
  private int mStopBits;
  private int mParity;
  private int mFlowControl;

  public SerialSettings() {}

  public SerialSettings(int speed, int dataBits, int stopBits, int parity, int flowControl) {
    mSpeed = speed;
    mDataBits = dataBits;
    mStopBits = stopBits;
    mParity = parity;
    mFlowControl = flowControl;
  }

  public SerialSettings(SerialSettings settings) {
    mSpeed = settings.mSpeed;
    mDataBits = settings.mDataBits;
    mStopBits = settings.mStopBits;
    mParity = settings.mParity;
    mFlowControl = settings.mFlowControl;
  }

  public int getSpeed() {
    return mSpeed;
  }

  public void setSpeed(int speed) {
    mSpeed = speed;
  }

  public int getDataBits() {
    return mDataBits;
  }

  public void setDataBits(int dataBits) {
    mDataBits = dataBits;
  }

  public int getStopBits() {
    return mStopBits;
  }

  public void setStopBits(int stopBits) {
    mStopBits = stopBits;
  }

  public int getParity() {
    return mParity;
  }

  public void setParity(int parity) {
    mParity = parity;
  }

  public int getFlowControl() {
    return mFlowControl;
  }

  public void setFlowControl(int flowControl) {
    mFlowControl = flowControl;
  }
}
