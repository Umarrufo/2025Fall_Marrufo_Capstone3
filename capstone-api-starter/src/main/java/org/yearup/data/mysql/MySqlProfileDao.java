package org.yearup.data.mysql;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.yearup.models.Category;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (User_Id, First_Name, Last_Name, Phone, Email, Address, City, State, Zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getById(int profileId)
    {
        String sqlQuery = "Select * From Profiles Where User_Id = ?";

        try(Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);

            ps.setInt(1, profileId);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                int dbProfileId = rs.getInt("User_Id");
                String firstName = rs.getString("First_Name");
                String lastName = rs.getString("Last_Name");
                String phone = rs.getString("Phone");
                String email = rs.getString("Email");
                String address = rs.getString("Address");
                String city = rs.getString("City");
                String state = rs.getString("State");
                String zip = rs.getString("Zip");

                Profile profile = new Profile(dbProfileId, firstName, lastName, phone, email, address, city, state, zip);
                return profile;
            }
        }
        catch(SQLException ex)
        {
            System.out.println("\nThere was a problem with the database");
        }
        return null;
    }

    @Override
    public void update(int profileId, Profile profile)
    {
        // update profile
        String sqlQuery = "Update Profiles Set First_Name = ?, Last_Name = ?," +
                "Phone = ?, Email = ?, Address = ?, City = ?, State = ?," +
                "Zip = ? Where User_Id = ?";
        try(Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);

            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getPhone());
            ps.setString(4, profile.getEmail());
            ps.setString(5, profile.getAddress());
            ps.setString(6, profile.getCity());
            ps.setString(7, profile.getState());
            ps.setString(8, profile.getZip());
            ps.setInt(9, profileId);

            ps.executeUpdate();

        }
        catch(SQLException ex)
        {
            System.out.println("\nSomething went wrong with the db");
            ex.printStackTrace();
        }
    }
}
