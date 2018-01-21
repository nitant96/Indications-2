package gov.cipam.gi.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gov.cipam.gi.R;
import gov.cipam.gi.database.Database;

/**
 * Created by NITANT SOOD on 08-01-2018.
 */

public class SearchCursorAdapter extends android.support.v4.widget.CursorAdapter {

    Context mContext;
    Cursor cursor;
    setOnSuggestionClickListener mListener;
    String mQuery;

    public SearchCursorAdapter(Context context, Cursor c, boolean autoRequery,setOnSuggestionClickListener mListener,String query) {
        super(context, c, autoRequery);
        this.mContext = context;
        this.cursor = c;
        mQuery=query;
        this.mListener = mListener;
    }



    public interface setOnSuggestionClickListener{
        void onSuggestionClickListener(View view, String type, String name);
    }

    public void updateQuery(String query){
        mQuery=query;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.suggestion_item_view,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView suggestion_Name=view.findViewById(R.id.suggestionName);
        ImageView suggestionImage=view.findViewById(R.id.suggestionImage);
        TextView suggestion_Type=view.findViewById(R.id.suggestionType);

        final String suggestion_name=cursor.getString(cursor.getColumnIndex(Database.GI_SEARCH_NAME));
        final String suggestion_type=cursor.getString(cursor.getColumnIndex(Database.GI_SEARCH_TYPE));

        int querySize=mQuery.length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(suggestion_name);
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 0,querySize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0,querySize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        suggestion_Name.setText(sb);
        suggestion_Type.setText("in "+suggestion_type);

        if(suggestion_type.equals(Database.GI_HISTORY)){
            suggestionImage.setImageResource(android.R.drawable.ic_menu_recent_history);
        }
        else{
            suggestionImage.setImageResource(android.R.drawable.ic_menu_info_details);
        }
        view.setTag(cursor.getPosition());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int k = (int) v.getTag();
//                        Toast.makeText(mContext,k+" position", Toast.LENGTH_SHORT).show();
                      if(mCursor.moveToPosition(k)) {
                          String name = mCursor.getString(mCursor.getColumnIndex(Database.GI_SEARCH_NAME));
                          String type = mCursor.getString(mCursor.getColumnIndex(Database.GI_SEARCH_TYPE));

                          mListener.onSuggestionClickListener(v, type, name);
//                          Toast.makeText(mContext, name + "of type :" + type + " clicked", Toast.LENGTH_SHORT).show();
                      }
                    }
                });
    }
}
