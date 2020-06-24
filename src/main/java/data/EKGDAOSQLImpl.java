/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
package data;

import java.sql.*;
import java.util.LinkedList;

public class EKGDAOSQLImpl implements EKGDAO {

    //The cpr, data, and timestamp gets saved on the database into the table ekgData in MySQL
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
    //Loads data from the database back into java, here it selects everything from the table ekgData where the cpr is equal to what you write in the GUI.
//It has a limit of 150, this is because it would otherwise look cluttered. In reality it would have been nice to have the statement also select a given data and time to show some more specific data.
    @Override
    public LinkedList<EKGDTO> loadEKG(String id) {
        LinkedList<EKGDTO> data = new LinkedList<>();
        Connection connection = SQLConnector.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ekgData WHERE cpr = ? limit 150 ");
            preparedStatement.setString(1, id);
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