package com.example.selvam.searchtest;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String[] SUGGESTIONS = {
            "B:auru", "S:ao Paulo", "Rio de Janeiro",
            "Bahia", "M:ato Grosso", "Minas Ger:ais",
            "Toc:antins", "Rio Gr:ande do Sul"
    };
    private SimpleCursorAdapter mAdapter;
    SearchView searchView;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        String[] columnNames = {"_id","cityName"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        String[] temp = new String[2];
        int id = 0;
        for(String item : SUGGESTIONS){
            temp[0] = Integer.toString(id++);
            temp[1] = item;
            cursor.addRow(temp);
        }

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.simple_list_item_1,
                cursor,
                from,
                to,
                CursorAdapter.IGNORE_ITEM_VIEW_TYPE);

        /*mAdapter.setFilterQueryProvider(new FilterQueryProvider() {

            @Override
            public Cursor runQuery(CharSequence constraint) {
                String partialValue = constraint.toString();

                return null;

            }
        });*/


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.search));
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(true);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        searchView.setSearchableInfo(searchableInfo);

        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                Toast.makeText(getActivity(), "Hello selected:" + position, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("enter qry:", s);
                //s = ":"+s;
                if (s.length() > 0) {
                    populateAdapter(s);
                }
                return false;
            }
        });

       // AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)searchView.
         //       findViewById(getResources().getIdentifier("search_src_text", "id", getContext().getPackageName()));
        //autoCompleteTextView.setThreshold(1);
        //searchView.
        //searchView.auo

    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });

       // mAdapter.getFilter().filter(query.toString());

       // searchView.setSuggestionsAdapter(mAdapter);
        for (int i=1; i<SUGGESTIONS.length; i++) {
            Log.d("query", SUGGESTIONS[i]+":"+query);
            if (SUGGESTIONS[i].toLowerCase().contains(query.toLowerCase())) {
                Log.d(" contains >>:",SUGGESTIONS[i].toLowerCase());
                c.addRow(new Object[]{i, SUGGESTIONS[i]});
            }
        }
        Log.d("final :", c.getCount() + "");
        mAdapter.changeCursor(c);
        mAdapter.notifyDataSetChanged();
      //  searchView.setSuggestionsAdapter(mAdapter);

    }

}
