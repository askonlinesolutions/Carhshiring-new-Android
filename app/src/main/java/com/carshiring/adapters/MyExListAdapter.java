package com.carshiring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import com.carshiring.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class MyExListAdapter extends BaseExpandableListAdapter {
    Context excessProtection;
    List<String> listparent;
    HashMap<String, List<String>> listchild;
    public MyExListAdapter(Context excessProtection, List<String> listparent, HashMap<String, List<String>> listchild) {
        this.excessProtection=excessProtection;
        this.listparent=listparent;
        this.listchild=listchild;
    }

    @Override
    public int getGroupCount() {
        return listparent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listchild.get(listparent.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listparent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listchild.get(listparent.get(groupPosition)).get(childPosition);
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
        String head= (String) getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) excessProtection.getSystemService(excessProtection.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.protection_title,null);
        }
        TextView textView= (TextView) convertView.findViewById(R.id.txt_titile);
        textView.setText(head);
        if(isExpanded)
        {
       //     groupHolder.img.setImageResource(R.drawable.ic_droppro);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child= (String) getChild(groupPosition,childPosition);
        if(convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) excessProtection.getSystemService(excessProtection.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.protection_titledetail,null);
        }
        TextView textView= (TextView) convertView.findViewById(R.id.txt_titile_detail);
        textView.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
