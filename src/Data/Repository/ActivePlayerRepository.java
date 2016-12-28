package Data.Repository;

import Data.DataContext;
import Entities.ActivePlayerEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;

import java.util.List;

public class ActivePlayerRepository {

    public List<ActivePlayerEntity> GetAll()
    {
        List<ActivePlayerEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ActivePlayerEntity.class);

            entities = criteria.list();
        }

        return entities;
    }

    public void Save(List<ActivePlayerEntity> entities)
    {
        try(DataContext context = new DataContext())
        {
            Query query = context.getSession().createQuery("DELETE FROM ActivePlayerEntity");
            query.executeUpdate();

            for(ActivePlayerEntity entity : entities)
            {
                context.getSession().saveOrUpdate(entity);
            }
        }
    }

}
