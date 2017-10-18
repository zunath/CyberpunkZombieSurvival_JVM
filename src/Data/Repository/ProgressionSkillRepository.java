package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.ProgressionLevelEntity;
import Entities.ProgressionSkillEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class ProgressionSkillRepository {

    public ProgressionSkillEntity GetProgressionSkillByID(int skillID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("ProgressionSkill/GetProgressionSkillByID", ProgressionSkillEntity.class,
                    new SqlParameter("skillID", skillID));
        }
    }

}
