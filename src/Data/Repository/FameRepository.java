package Data.Repository;

import Data.DataContext;
import Entities.PCQuestStatusEntity;
import Entities.PCRegionalFameEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class FameRepository {

    public PCRegionalFameEntity GetPCFameByID(String playerID, int regionID)
    {
        PCRegionalFameEntity entity;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCRegionalFameEntity.class);

            entity = (PCRegionalFameEntity) criteria
                    .add(Restrictions.eq("playerID", playerID))
                    .add(Restrictions.eq("fameRegionID", regionID))
                    .uniqueResult();

        }

        return entity;
    }



    public void Save(PCRegionalFameEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
