package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.LootTableEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class LootTableRepository {

    public LootTableEntity GetByLootTableID(int lootTableID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("LootTable/GetByLootTableID", LootTableEntity.class,
                    new SqlParameter("lootTableID", lootTableID));
        }
    }
}
