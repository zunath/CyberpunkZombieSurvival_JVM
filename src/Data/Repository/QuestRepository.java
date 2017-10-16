package Data.Repository;

import Data.DataContext;
import Entities.PCQuestStatusEntity;
import Entities.QuestEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class QuestRepository {


    public QuestEntity GetQuestByID(int questID)
    {
        QuestEntity entity;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(QuestEntity.class);

            entity = (QuestEntity) criteria
                    .add(Restrictions.eq("questID", questID))
                    .uniqueResult();

        }

        return entity;
    }

    public PCQuestStatusEntity GetPCQuestStatusByID(String playerID, int questID)
    {
        PCQuestStatusEntity entity;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCQuestStatusEntity.class);

            entity = (PCQuestStatusEntity)criteria
                    .createAlias("quest", "q")
                    .add(Restrictions.eq("playerID", playerID))
                    .add(Restrictions.eq("q.questID", questID))
                    .uniqueResult();

        }

        return entity;
    }

    public List<PCQuestStatusEntity> GetAllPCQuestStatusesByID(String playerID)
    {
        List<PCQuestStatusEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCQuestStatusEntity.class)
                    .add(Restrictions.eq("playerID", playerID));
            entities = criteria.list();
        }

        return entities;
    }


    public void Save(PCQuestStatusEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }


}
