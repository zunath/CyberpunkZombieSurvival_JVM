package Data;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.io.*;
import java.net.URL;
import java.util.List;

public class DataContext implements AutoCloseable {

    Transaction tran;

    public DataContext()
    {
        tran = DataAccess.getSession().beginTransaction();
    }

    public Session getSession()
    {
        return DataAccess.getSession();
    }

    private String readSQL(String sqlFilePath)
    {
        URL url = getClass().getResource("/Data/SQL/" + sqlFilePath + ".sql");
        String sql;
        try
        {
            sql = Resources.toString(url, Charsets.UTF_8);
        }
        catch (IOException ex)
        {
            System.out.println("Unable to read file executeSQLFileList: " + sqlFilePath + " " + ex.getMessage());
            return null;
        }

        return sql;
    }

    private <T> NativeQuery<T> buildQuery(String sqlFilePath, Class c, SqlParameter... params)
    {
        String sql = readSQL(sqlFilePath);
        NativeQuery<T> query = getSession()
                .createNativeQuery(sql, c);

        for(SqlParameter p : params)
        {
            query.setParameter(p.getName(), p.getValue());
        }

        return query;
    }

    public <T> List<T> executeSQLList(String sqlFilePath, Class c, SqlParameter... params)
    {
        NativeQuery<T> query = buildQuery(sqlFilePath, c, params);
        return query.getResultList();
    }

    public <T> T executeSQLSingle(String sqlFilePath, Class c, SqlParameter... params)
    {
        NativeQuery<T> query = buildQuery(sqlFilePath, c, params);
        return query.getSingleResult();
    }

    @Override
    public void close()  {
        tran.commit();
    }
}
