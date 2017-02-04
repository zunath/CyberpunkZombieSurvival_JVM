package Data.Repository;

import Data.DataContext;
import Entities.SpawnTableEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class SpawnTableRepository {

    public SpawnTableEntity GetBySpawnTableID(int spawnTableID)
    {
        SpawnTableEntity entity;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(SpawnTableEntity.class)
                    .add(Restrictions.eq("spawnTableID", spawnTableID));

            entity = (SpawnTableEntity)criteria.uniqueResult();
        }

        return entity;
    }

}
