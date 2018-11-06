package com.abdelazim.globaltask.repository.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class DayWithAchievements {

    @Embedded
    public Day day;

    @Relation(entity = Achievement.class, entityColumn = "day", parentColumn = "day")
    public List<Achievement> achievementList;
}
