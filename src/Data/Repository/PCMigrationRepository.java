package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCMigrationEntity;
import org.hibernate.criterion.Restrictions;

public class PCMigrationRepository {

    public PCMigrationEntity GetByMigrationID(int pcMigrationID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("PCMigration/GetByMigrationID", PCMigrationEntity.class,
                    new SqlParameter("pcMigrationID", pcMigrationID));
        }
    }


}
