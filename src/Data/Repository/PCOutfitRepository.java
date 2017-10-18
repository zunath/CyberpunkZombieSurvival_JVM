package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCMigrationEntity;
import Entities.PCOutfitEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("UnusedDeclaration")
public class PCOutfitRepository {

    public PCOutfitEntity GetByUUID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("PCMigration/GetByUUID", PCOutfitEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public void Save(PCOutfitEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
