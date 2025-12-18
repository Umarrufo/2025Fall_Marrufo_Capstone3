package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List<Category> categories = new ArrayList<>();
        String sqlQuery = "Select * From Categories";

        try(Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int categoryId = rs.getInt("Category_Id");
                String name = rs.getString("Name");
                String description = rs.getString("Description");

                Category category = new Category(categoryId, name, description);
                categories.add(category);
            }
        }
        catch(SQLException ex)
        {
            System.out.println("\nThere was a problem with the database");
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        String sqlQuery = "Select * From Categories Where Category_Id = ?";

        try(Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);

            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                int dbCategoryId = rs.getInt("Category_Id");
                String name = rs.getString("Name");
                String description = rs.getString("Description");

                Category category = new Category(dbCategoryId, name, description);

                return category;
            }
        }
        catch(SQLException ex)
        {
            System.out.println("\nThere was a problem with the database");
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String sqlQuery ="Insert Into Categories(name, description)" +
                "Values(?, ?)";
        try(Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            ps.executeUpdate();

                try(ResultSet generatedKeys = ps.getGeneratedKeys())
                {
                    if(generatedKeys.next())
                    {
                        category.setCategoryId(generatedKeys.getInt(1));
                    }
                }
        }
        catch(SQLException ex)
        {
            System.out.println("\nSomething went wrong with the db");
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        String sqlQuery = "Update Categories Set Name = ?, Description = ? Where Category_Id = ?";
        try(Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, categoryId);

            ps.executeUpdate();

        }
        catch(SQLException ex)
        {
            System.out.println("\nSomething went wrong with the db");
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        String sqlQuery= "Delete From Categories Where Category_Id = ?";

        try(Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);

            ps.setInt(1, categoryId);

            ps.executeUpdate();
        }
        catch(SQLException ex){
            System.out.println("\nThere was a problem with the database");
            ex.printStackTrace();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
