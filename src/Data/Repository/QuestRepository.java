package Data.Repository;

import Data.DataContext;
import Entities.PCQuestKillTargetProgressEntity;
import Entities.PCQuestStatusEntity;
import Entities.QuestEntity;
import Entities.QuestKillTargetListEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
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

    public List<QuestKillTargetListEntity> GetQuestKillTargetsByQuestID(int questID)
    {
        List<QuestKillTargetListEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(QuestKillTargetListEntity.class)
                    .createAlias("quest", "q")
                    .add(Restrictions.eq("q.questID", questID));
            entities = criteria.list();
        }

        return entities;
    }

    public List<PCQuestKillTargetProgressEntity> GetPlayerKillTargetsByID(String playerID, int npcGroupID)
    {
        List<PCQuestKillTargetProgressEntity> entities;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCQuestKillTargetProgressEntity.class)
                    .createAlias("npcGroup", "n")
                    .add(Restrictions.eq("playerID", playerID))
                    .add(Restrictions.eq("n.npcGroupID", npcGroupID));

            entities = criteria.list();
        }

        return entities;
    }

    public List<Integer> GetAllCompletedQuestIDs(String playerID)
    {
        List<Integer> questIDs;

        try(DataContext context = new DataContext())
        {
            Criteria criteria = context.getSession()
                    .createCriteria(PCQuestStatusEntity.class)
                    .createAlias("quest", "q")
                    .add(Restrictions.eq("playerID", playerID))
                    .add(Restrictions.isNotNull("completionDate"))
                    .setProjection(Projections.distinct(Projections.property("q.questID")));
            questIDs = criteria.list();
        }

        return questIDs;
    }

    public void Save(PCQuestStatusEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Save(PCQuestKillTargetProgressEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Delete(PCQuestKillTargetProgressEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }


}
