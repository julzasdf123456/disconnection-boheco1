package com.lopez.julz.disconnection.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lopez.julz.disconnection.objects.DiscoGroup;

import java.util.List;

@Dao
public interface DisconnectionListDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DisconnectionList... disconnectionLists);

    @Update
    void updateAll(DisconnectionList... disconnectionLists);

    @Query("SELECT * FROM DisconnectionList WHERE UploadStatus='Uploadable'")
    List<DisconnectionList> getUploadable();

    @Query("SELECT * FROM DisconnectionList WHERE id=:id")
    DisconnectionList getOne(String id);

    @Query("SELECT AccountNumber, ConsumerName, MeterNumber, ConsumerAddress FROM DisconnectionList WHERE UploadStatus IS NULL AND (AccountNumber LIKE :regex OR ConsumerName LIKE :regex OR MeterNumber LIKE :regex) AND ScheduleId= :schedId GROUP BY AccountNumber, ConsumerName, MeterNumber, ConsumerAddress ORDER BY ConsumerName")
    List<DiscoListGrouped> getSearch(String regex, String schedId);

    @Query("SELECT AccountNumber, ConsumerName, MeterNumber, ConsumerAddress FROM DisconnectionList WHERE UploadStatus IS NULL AND ScheduleId= :schedId GROUP BY AccountNumber, ConsumerName, MeterNumber, ConsumerAddress ORDER BY ConsumerName")
    List<DiscoListGrouped> getAllFromSched(String schedId);

    @Query("SELECT AccountNumber, ConsumerName, MeterNumber, ConsumerAddress FROM DisconnectionList WHERE UploadStatus='Uploadable' AND ScheduleId= :schedId GROUP BY AccountNumber, ConsumerName, MeterNumber, ConsumerAddress ORDER BY ConsumerName")
    List<DiscoListGrouped> getGroupedUploadable(String schedId);

    @Query("SELECT * FROM DisconnectionList WHERE AccountNumber = :acctNo AND ScheduleId = :schedId")
    List<DisconnectionList> getConsumerDiscoData(String acctNo, String schedId);

    @Query("SELECT * FROM DisconnectionList WHERE ScheduleId= :schedId")
    List<DisconnectionList> getAllMapView(String schedId);

    @Query("SELECT COUNT(id) FROM DisconnectionList WHERE ScheduleId= :schedId AND UploadStatus IS NULL")
    int getCountBySched(String schedId);

    @Query("SELECT AccountNumber FROM DisconnectionList WHERE ScheduleId= :schedId AND Status='Disconnected' AND (UploadStatus IS NULL OR UploadStatus='Uploadable') GROUP BY AccountNumber")
    List<String> getDisconnectedCountBySched(String schedId);

    @Query("SELECT * FROM DisconnectionList WHERE ScheduleId= :schedId AND (UploadStatus IS NULL OR UploadStatus='Uploadable')")
    List<DisconnectionList> getCollected(String schedId);

    @Query("SELECT AccountNumber FROM DisconnectionList WHERE ScheduleId= :schedId AND Status='Paid' AND (UploadStatus IS NULL OR UploadStatus='Uploadable') GROUP BY AccountNumber")
    List<String> getPaidAccounts(String schedId);
}
