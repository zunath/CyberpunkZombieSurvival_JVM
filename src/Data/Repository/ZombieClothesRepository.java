package Data.Repository;

import Data.DataContext;
import Entities.ZombieClothesEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ZombieClothesRepository {

    public List<ZombieClothesEntity> GetRandomClothes()
    {
        List<ZombieClothesEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ZombieClothesEntity.class);

            entities = criteria
                    .add(Restrictions.ne("resref", ""))
                    .add(Restrictions.eq("isActive", true))
                    .list();

        }

        return entities;
    }

}
