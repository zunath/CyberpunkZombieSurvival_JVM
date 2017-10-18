package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.SpawnTableEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class SpawnTableRepository {

    public SpawnTableEntity GetBySpawnTableID(int spawnTableID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("SpawnTable/GetBySpawnTableID", SpawnTableEntity.class,
                    new SqlParameter("spawnTableID", spawnTableID));
        }
    }

}
