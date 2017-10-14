package Data.Repository;

import Data.DataContext;

public class DatabaseRepository {

    public void KeepAlive()
    {
        try(DataContext context = new DataContext())
        {
            context.getSession()
                    .createSQLQuery("SELECT 'keep-alive'")
                    .list();
        }
    }
}
