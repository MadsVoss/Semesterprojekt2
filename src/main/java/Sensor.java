/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
import data.EKGDTO;
import jssc.*;
import java.sql.Timestamp;
import java.util.LinkedList;

public class Sensor {
    private SerialPort serialPort = null;
    private String result = null;

    //Access the sensor (Arduino etc). We also set our parameters for baudRate and so on.
    public Sensor(int portnummer) {
        String[] portNames = SerialPortList.getPortNames();
        try {
            serialPort = new SerialPort(portNames[portnummer]);
            serialPort.openPort();
            serialPort.setParams(38400, 8, 1, 0);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.setDTR(true);
            serialPort.setRTS(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //We get the data from the sensor and catch any exceptions if necessary
    public LinkedList<EKGDTO> getData() {
        try {
            if (serialPort.getInputBufferBytesCount() > 0) {
                result = serialPort.readString();
                String[] rawValues;
                if (result != null) {
                    result = result.substring(0, result.length() - 1);
                    rawValues = result.split(" ");
                    LinkedList<EKGDTO> data = new LinkedList<>();
                    for (int i = 0; i < rawValues.length; i++) {
                        EKGDTO ekgDTO = new EKGDTO();
                        try {
                            if (!rawValues[i].equals("")) {
                                try {
                                    ekgDTO.setEkg(Double.parseDouble(rawValues[i]));
                                    if (ekgDTO.getEkg() > 100) {
                                        ekgDTO.setTimestamp(new Timestamp(System.currentTimeMillis()));
                                        data.add(ekgDTO);
                                        Thread.sleep(1);
                                    }

                                }
                                catch (NumberFormatException e){
                                    System.out.println("Discarding data");
                                }

                            }


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return data;
                }
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return null;
    }
}