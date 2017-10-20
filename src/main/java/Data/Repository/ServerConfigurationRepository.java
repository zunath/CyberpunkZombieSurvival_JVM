package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.ServerConfigurationEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

public class ServerConfigurationRepository {

    public static ServerConfigurationEntity GetServerConfiguration()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("ServerConfiguration/GetServerConfiguration", ServerConfigurationEntity.class);
        }
    }

}
