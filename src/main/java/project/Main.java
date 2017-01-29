package project;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deft on 29.01.2017.
 */
public class Main {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/javastudy";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    static final int N = 1000000;

    public static String sqlForCountFields = "SELECT COUNT(*) FROM TEST";
    public static String sqlForSelectAll = "SELECT * FROM TEST";
    public static String sqlForDeleteAll = "TRUNCATE TABLE TEST";
    public static String sqlForInsert = "INSERT INTO TEST (FIELD) VALUE (?)";

    public static String pathToFirstFile = "C:\\Golitsyn\\1.xml";
    public static String pathToSecondFile = "C:\\Golitsyn\\2.xml";


    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ArrayList<BeanObject> rezList = new ArrayList();
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sqlForCountFields);
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            if (count > 0) {
                statement.execute(sqlForDeleteAll);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO TEST (FIELD) VALUES ");
            int temp = 0;
            for (int i = 0; i < N; ) {
                i++;
                sb.append("(")
                        .append(i)
                        .append("), ");
                temp++;
                if (temp == 100000) {
                    PreparedStatement ps = connection.prepareStatement(sb.toString().substring(0, sb.length() - 2));
                    ps.execute();
                    temp = 0;
                    sb = new StringBuilder();
                    sb.append("INSERT INTO TEST (FIELD) VALUES ");
                }
            }
            PreparedStatement ps = connection.prepareStatement(sb.toString().substring(0, sb.length() - 2));

            rs = statement.executeQuery(sqlForSelectAll);
            while (rs.next()) {
                BeanObject bo = new BeanObject();
                bo.setField(rs.getInt(1));
                rezList.add(bo);
            }
            rs.close();

            Entries bt = new Entries();
            bt.setEntry(rezList);
            JAXBContext context = JAXBContext.newInstance(Entries.class);
            context.createMarshaller().marshal(bt, new File(pathToFirstFile));

            Unmarshaller unmarshaller = context.createUnmarshaller();
            Entries entry = (Entries) unmarshaller.unmarshal(new File(pathToFirstFile));
            List<ModifyBeanObject> mbo = new ArrayList<ModifyBeanObject>();
            ModifyEntry me = new ModifyEntry();
            for (BeanObject bo : entry.getEntry()) {
                ModifyBeanObject mo = new ModifyBeanObject();
                mo.setField(bo.getField());
                mbo.add(mo);
            }
            me.setEntry(mbo);
            context = JAXBContext.newInstance(ModifyEntry.class);
            context.createMarshaller().marshal(me, new File(pathToSecondFile));
            System.out.println("successed");

            unmarshaller = context.createUnmarshaller();
            ModifyEntry modifyEntry = (ModifyEntry) unmarshaller.unmarshal(new File(pathToSecondFile));
            BigDecimal summ = new BigDecimal(0);
            for (ModifyBeanObject mb : modifyEntry.getEntry()) {
                summ = summ.add(new BigDecimal(mb.getField()));
            }
            System.out.println(summ);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private static void close(Connection connection, Statement statement) throws SQLException {
        if (statement != null)
            statement.close();
        if (connection != null)
            connection.close();
    }
}
