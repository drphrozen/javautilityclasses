import java.io.IOException;
import java.text.ParseException;

import dk.iha.and.MessageService;
import dk.iha.and.OnMessageListener;
import dk.iha.and.message.BloodPressure;
import dk.iha.and.message.Data;
import dk.iha.and.message.Message;
import dk.iha.and.message.Weight;

public class Program {
  public static void main(String[] args) throws IOException {
    final Weight weight = new Weight();
    final BloodPressure bloodPressure = new BloodPressure();
    MessageService messageService = new MessageService(new OnMessageListener() {
      @Override public boolean onMessage(Message message) {
        Data data = null;
        switch (message.getDeviceType()) {
        case UA767PBT:
          data = bloodPressure;
          break;
        case UC321PBT:
          data = weight;
          break;
        default:
          System.out.println("Error, device type not supported!");
          return false;
        }
        try {
          data.setData(message.getPayload());
        } catch (ParseException e) {
          e.printStackTrace();
          return false;
        }
        
        System.out.println(data);
        System.out.println("Battery:     " + message.getBatteryStatus());
        System.out.println("Serial #:    " + message.getSerialNumber());
        System.out.println("Measured:    " + message.getMeasurementDate());
        System.out.println("Transmitted: " + message.getTransmissionDate());
        return true;
      }
    });
    messageService.start();
    System.in.read();
    messageService.stop();
  }
}
