package Data.Repository;

import Data.DataContext;
import Entities.ProfessionEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ProfessionRepository {

    public List<ProfessionEntity> GetActiveProfessions()
    {
        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(ProfessionEntity.class)
                    .add(Restrictions.eq("isActive", true));

            return criteria.list();
        }
    }
}
