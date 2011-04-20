package dk.iha.nonin;

public class Format8 {
   public final byte[] data = new byte[4];
   
   public short getHeartRate() {
     return (short)(((data[0] << 7) & data[1]) & 0x01FF);
   }
   
   public byte getSpO2() {
     return data[2];
   }
   
   /**
    * An absence of signal, meaning the sensor is disconnected
    */
   public boolean isSensorDisconnected() {
     return (data[0] & 0x40) == 0x40;
   }
   
   /**
    * An absence of consecutive good pulse signals
    */
   public boolean isOutOfTrack() {
     return (data[0] & 0x20) == 0x20;
   }
   
   /**
    * Amplitude representation of low signal quality (holds for entire duration)
    */
   public boolean isLowPerfusion() {
     return (data[0] & 0x10) == 0x10;
   }
   
   /**
    * Amplitude representation of medium signal quality (holds for entire duration)
    */
   public boolean isMarginalPerfusion() {
     return (data[0] & 0x08) == 0x08;
   }
  
   /**
    * Indicates an artifact condition
    */
   public boolean isArtifact() {
     return (data[0] & 0x04) == 0x04;
   }
   
   /**
    * Sensor is providing unusable data for analysis
    */
   public boolean isSensorFaulty() {
     return (data[3] & 0x08) == 0x08;
   }
   
   /**
    * Is the mode Spot Check?
    * @return true if mode is "Spot Check", otherwise mode is "Sensor"
    */
   public boolean isModeSpotCheck() {
     return (data[3] & 0x04) == 0x04;
   }
   /**
    * Critical Battery condition - device will turn-off after sending this status
    */
   public boolean isBatteryCritical() {
     return (data[3] & 0x02) == 0x02;
   }
   
   /**
    * Low Battery condition, less than ½ hour run-time remains
    */
   public boolean isBatteryLow() {
     return (data[3] & 0x01) == 0x01;
   }
}
