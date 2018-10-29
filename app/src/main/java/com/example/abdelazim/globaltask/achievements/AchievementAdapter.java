package com.example.abdelazim.globaltask.achievements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.repository.model.Achievement;
import com.example.abdelazim.globaltask.repository.model.Day;
import com.example.abdelazim.globaltask.repository.model.DayWithAchievements;
import com.example.abdelazim.globaltask.utils.AppFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class AchievementAdapter extends BaseExpandableListAdapter {

    private List<Day> dayList = new ArrayList<>();
    private HashMap<Day, List<Achievement>> dayAchievementsHashMap = new HashMap<>();


    @Override
    public int getGroupCount() {

        return dayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Achievement> achievementList = dayAchievementsHashMap.get(dayList.get(groupPosition));
        if (achievementList == null)
            return 0;
        return achievementList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Achievement> achievementList = dayAchievementsHashMap.get(dayList.get(groupPosition));
        if (achievementList == null)
            return "null";
        return achievementList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // Recycling View
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_achievements_list, parent, false);

        // Expanded header color effect
        if (isExpanded)
            convertView.setBackground(parent.getResources().getDrawable(R.drawable.bg_ach_header_expanded));
        else
            convertView.setBackground(parent.getResources().getDrawable(R.drawable.bg_ach_header));

        // Header ViewHolder
        HeaderViewHolder viewHolder = new HeaderViewHolder(convertView);
        // Get current day
        Day day = (Day) getGroup(groupPosition);
        // Populate UI
        viewHolder.setDate(day.getHeader());
        viewHolder.setDoneNum(String.valueOf(getChildrenCount(groupPosition)));
        viewHolder.setLateNum(String.valueOf(day.getLateNum()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // Recycling
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievements_list, parent, false);

        // Achievement ViewHolder
        ChildViewHolder viewHolder = new ChildViewHolder(convertView);
        // Get current Achievement
        Achievement currentAchievement = (Achievement) getChild(groupPosition, childPosition);
        // Populate UI
        viewHolder.setTitle(currentAchievement.getTitle());
        viewHolder.setTime(AppFormatter.formatTime(currentAchievement.getTime()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    /**
     * Extract dayList and achievement lists from DayWithAchievements List
     *
     * @param dayWithAchievementsList list of object that contains day and its achievementList
     */
    void setDayWithAchievementsList(List<DayWithAchievements> dayWithAchievementsList) {

        List<Day> dayList = new ArrayList<>();
        HashMap<Day, List<Achievement>> dayAchievementsHashMap = new HashMap<>();

        for (int i = 0; i < dayWithAchievementsList.size(); i++) {

            DayWithAchievements currentDayWithAchievements = dayWithAchievementsList.get(i);

            Day currentDay = currentDayWithAchievements.day;
            List<Achievement> currentDayAchievements = currentDayWithAchievements.achievementList;

            dayList.add(currentDay);
            dayAchievementsHashMap.put(currentDay, currentDayAchievements);
        }

        this.dayList = dayList;
        this.dayAchievementsHashMap = dayAchievementsHashMap;
    }


    /**
     * Header ViewHolder
     */
    class HeaderViewHolder {
        TextView dateTextView, lateNumTextView, doneNumTextView;

        HeaderViewHolder(View itemView) {
            dateTextView = itemView.findViewById(R.id.header_date_textView);
            doneNumTextView = itemView.findViewById(R.id.header_done_numbers_textView);
            lateNumTextView = itemView.findViewById(R.id.header_late_numbers_textView);
        }

        void setDate(String date) {
            dateTextView.setText(date);
        }

        void setDoneNum(String doneNum) {
            doneNumTextView.setText(doneNum);
        }

        void setLateNum(String lateNum) {
            lateNumTextView.setText(lateNum);
        }
    }


    /**
     * Child ViewHolder
     */
    class ChildViewHolder {
        TextView achievementTitleTextView, achievementTimeTextView;

        ChildViewHolder(View itemView) {
            achievementTitleTextView = itemView.findViewById(R.id.achievement_title_textView);
            achievementTimeTextView = itemView.findViewById(R.id.achievement_time_textView);
        }


        void setTitle(String title) {
            achievementTitleTextView.setText(title);
        }

        void setTime(String time) {
            achievementTimeTextView.setText(time);
        }
    }

}
