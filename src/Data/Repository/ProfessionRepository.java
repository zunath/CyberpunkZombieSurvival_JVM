package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PortraitEntity;
import Entities.ProfessionEntity;
import Enumerations.ProfessionType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ProfessionRepository {

    public List<ProfessionEntity> GetActiveProfessions()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Profession/GetActiveProfessions", ProfessionEntity.class);
        }
    }
}
