package Data.Repository;

import Data.DataContext;
import Entities.ServerConfigurationEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

public class ServerConfigurationRepository {

    public static ServerConfigurationEntity GetServerConfiguration()
    {
        ServerConfigurationEntity entity;

        try(DataContext context = new DataContext())
        {
            DetachedCriteria maxID = DetachedCriteria.forClass(ServerConfigurationEntity.class)
                    .setProjection(Projections.max("serverConfigurationID"));

            Criteria criteria = context.getSession()
                    .createCriteria(ServerConfigurationEntity.class)
                    .add(Property.forName("serverConfigurationID").eq(maxID));

            entity = (ServerConfigurationEntity)criteria.uniqueResult();
        }

        return entity;
    }

}
