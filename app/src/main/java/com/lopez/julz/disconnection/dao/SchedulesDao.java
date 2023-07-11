package com.lopez.julz.disconnection.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SchedulesDao {
    @Query("SELECT * FROM Schedules WHERE Status IS NULL")
    List<Schedules> getActiveSchedules();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Schedules... schedules);

    @Update
    void updateAll(Schedules... schedules);
}
