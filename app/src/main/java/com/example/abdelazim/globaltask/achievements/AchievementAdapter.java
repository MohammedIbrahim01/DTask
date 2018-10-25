package com.example.abdelazim.globaltask.achievements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.repository.model.Achievement;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class AchievementAdapter extends BaseExpandableListAdapter {

    private List<String> headerList = new ArrayList<>();
    private HashMap<String, List<Achievement>> listHashMap = new HashMap<>();


    public void setHeaderList(List<String> headerList) {
        this.headerList = headerList;
    }

    public void setListHashMap(HashMap<String, List<Achievement>> listHashMap) {
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Achievement> taskList = listHashMap.get(headerList.get(groupPosition));
        if (taskList == null)
            return 0;
        return taskList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Achievement> AchievementList = listHashMap.get(headerList.get(groupPosition));
        if (AchievementList == null)
            return null;
        return AchievementList.get(childPosition);
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

        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_achievements_list, parent, false);
        if (isExpanded)
            convertView.setBackgroundColor(parent.getResources().getColor(R.color.colorAccent));
        else
            convertView.setBackgroundColor(parent.getResources().getColor(android.R.color.transparent));

        String headerTitle = (String) getGroup(groupPosition);
        HeaderViewHolder viewHolder = new HeaderViewHolder(convertView);

        viewHolder.setDate(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievements_list, parent, false);
        ChildViewHolder viewHolder = new ChildViewHolder(convertView);
        Achievement currentAchievement = (Achievement) getChild(groupPosition, childPosition);
        viewHolder.setTitle(currentAchievement.getTitle());
        viewHolder.setDescription(currentAchievement.getDescription());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class HeaderViewHolder {
        TextView dateTextView;

        public HeaderViewHolder(View itemView) {
            dateTextView = itemView.findViewById(R.id.header_date_textView);
        }

        public void setDate(String time) {
            dateTextView.setText(time);
        }
    }

    public class ChildViewHolder {
        TextView achievementTitleTextView, achievementDescriptionTextView;

        public ChildViewHolder(View itemView) {
            achievementTitleTextView = itemView.findViewById(R.id.achievement_title_textView);
            achievementDescriptionTextView = itemView.findViewById(R.id.achievement_description_textView);
        }

        public void setDescription(String description) {
            achievementDescriptionTextView.setText(description);
        }

        public void setTitle(String title) {
            achievementTitleTextView.setText(title);
        }
    }

}
