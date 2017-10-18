package Data.Repository;

import Data.DataContext;
import Entities.FameRegionEntity;
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
                    .createAlias("fameRegion", "f")
                    .add(Restrictions.eq("playerID", playerID))
                    .add(Restrictions.eq("f.fameRegionID", regionID))
                    .uniqueResult();

        }

        if(entity == null)
        {
            try(DataContext context = new DataContext())
            {
                Criteria criteria = context.getSession()
                        .createCriteria(FameRegionEntity.class);

                FameRegionEntity fameRegion = (FameRegionEntity) criteria
                        .add(Restrictions.eq("fameRegionID", regionID))
                        .uniqueResult();

                entity = new PCRegionalFameEntity();
                entity.setAmount(0);
                entity.setPlayerID(playerID);
                entity.setFameRegion(fameRegion);
                context.getSession().saveOrUpdate(entity);
            }
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
