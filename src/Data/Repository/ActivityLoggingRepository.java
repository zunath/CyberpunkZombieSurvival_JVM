package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.ChatChannelEntity;
import Entities.ChatLogEntity;

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
