/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
package data;

import sun.management.Sensor;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EKGDAOSQLImpl implements EKGDAO {


    @Override
    public void saveEkg(LinkedList<EKGDTO> ekgdtobatch) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = SQLConnector.getConnection();

                try {
                    PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO ekgData(cpr, data, time) VALUES (?, ?, ?)");
                    for (EKGDTO ekgDTO: ekgdtobatch) {
                        preparedStatement.setString(1, ekgDTO.getCpr());
                        preparedStatement.setDouble(2, ekgDTO.getEkg());
                        preparedStatement.setTimestamp(3, ekgDTO.getTimestamp());
                        preparedStatement.execute();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public LinkedList<EKGDTO> loadEKG(String id) {
        LinkedList<EKGDTO> data = new LinkedList<>();
        Connection connection = SQLConnector.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ekgData WHERE cpr = ? ");
            preparedStatement.setString(1, "");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EKGDTO ekgDTO = new EKGDTO();
                ekgDTO.setId(Integer.parseInt(resultSet.getString("id")));
                ekgDTO.setCpr(id);
                ekgDTO.setEkg(resultSet.getDouble("data"));
                ekgDTO.setTimestamp(resultSet.getTimestamp("time"));
                data.add(ekgDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}

/*    */