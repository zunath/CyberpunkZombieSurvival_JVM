package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.ChatChannelEntity;
import Entities.ChatLogEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ActivityLoggingRepository {

    public ChatChannelEntity GetChatChannelByID(int chatChannelID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("ActivityLogging/GetChatChannelByID", ChatChannelEntity.class,
                    new SqlParameter("chatChannelID", chatChannelID));
        }
    }


    public void Save(ChatLogEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
