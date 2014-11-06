/*
 =============
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package recommender;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBManager
{
    private String username;
    private String password;
    private String databaseUrl;
    private String driverClassName;

    public enum Table
    {
        USER_RATINGS("suggestUsers.dat", "SELECT iduser, fromUser, rating, date FROM userRatings"),
        CONTENT_RATINGS("suggestContent.dat", "SELECT idcontent, fromUser, rating, date FROM contentRatings");
        
        private final String temporalFile;
        private final String query;

        Table(final String temporalFile, String query)
        {
            this.temporalFile = temporalFile;
            this.query = query;
        }

        public String getQuery()
        {
            return query;
        }

        public String getTemporalFile()
        {
            return temporalFile;
        }
    }

    /* 
     * DBManager
     */
    public DBManager()
    {
        Properties prop = new Properties();

        try
        {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");

            prop.load(inputStream);

            username = prop.getProperty("jdbc.username");
            password = prop.getProperty("jdbc.password");
            databaseUrl = prop.getProperty("jdbc.databaseUrl");
            driverClassName = prop.getProperty("jdbc.driverClassName");
        }
        catch (IOException e)
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public ResultSet doQuery(String query)
    {
        ResultSet res = null;

        try
        {
            Class.forName(driverClassName);

            Connection con = DriverManager.getConnection(databaseUrl, username, password);

            res = con.createStatement().executeQuery(query);
        }
        catch (ClassNotFoundException | SQLException ex)
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public void createDbFile(Table table)
    {
        try
        {
            try (FileWriter fileWriter = new FileWriter(table.getTemporalFile()))
            {
                PrintWriter pw = new PrintWriter(fileWriter);

                ResultSet res = doQuery(table.getQuery());

                while (res.next())
                {
                    String colum1 = res.getString(1);
                    String colum2 = res.getString(2);
                    String colum3 = res.getString(3);
                    String colum4 = res.getString(4);

                    if (table == Table.USER_RATINGS)
                        pw.println(colum1 + "\t" + colum2 + "\t" + colum3 + "\t" + colum4);
                    else
                        pw.println(colum2 + "\t" + colum1.hashCode() + "\t" + colum3);
                }

                res.close();
            }
        }
        catch (SQLException | IOException ex)
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
