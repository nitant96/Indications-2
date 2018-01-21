package gov.cipam.gi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import gov.cipam.gi.R;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.States;

/**
 * Created by NITANT SOOD on 10-01-2018.
 */

public class searchListAdapter extends BaseExpandableListAdapter {
    public Context mContext;
    public ArrayList<String> parentHeaders;
    public Map<String,ArrayList> parentChildListMapping;

    public searchListAdapter(Context mContext, ArrayList<String> myparentHeaders, Map<String, ArrayList> parentChildListMapping) {
        this.mContext = mContext;
        this.parentHeaders = myparentHeaders;
        this.parentChildListMapping = parentChildListMapping;
    }

    @Override
    public int getGroupCount() {
//        Toast.makeText(mContext,parentHeaders.size()+"", Toast.LENGTH_SHORT).show();
        return parentHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        Toast.makeText(mContext, ""+parentChildListMapping.get(parentHeaders.get(groupPosition)).size(), Toast.LENGTH_SHORT).show();
        return (parentChildListMapping.get(parentHeaders.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return parentChildListMapping.get(parentHeaders.get(groupPosition)).get(childPosition);
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
        LayoutInflater inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.serach_list_parent_item,parent,false);
        TextView parentText=convertView.findViewById(R.id.searchListParentName);
        parentText.setText((String)getGroup(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.search_list_child_item,null);
        TextView childText=convertView.findViewById(R.id.searchListChildName);
        String parentName=parentHeaders.get(groupPosition);
        if(parentName.equals(Database.GI_PRODUCT)){
            Product product=(Product) parentChildListMapping.get(parentName).get(childPosition);
            childText.setText(product.getName());
        }
        else if(parentName.equals(Database.GI_CATEGORY)){
            Categories categories=(Categories) parentChildListMapping.get(parentName).get(childPosition);
            childText.setText(categories.getName());
        }
        else if(parentName.equals(Database.GI_STATE)){
            States state=(States) parentChildListMapping.get(parentName).get(childPosition);
            childText.setText(state.getName());
        }
        else if(parentName.equals(Database.GI_SELLER)){
            Seller seller=(Seller) parentChildListMapping.get(parentName).get(childPosition);
            childText.setText(seller.getName());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
