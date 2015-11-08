package de.htw_berlin.maph_g5;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private MyAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView = (ListView) findViewById(R.id.main_actions_listview);
        mListAdapter = new MyAdapter(this, getResources().getStringArray(R.array.listActions), getResources().getStringArray(R.array.listDescriptions));
        listView.setAdapter(mListAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<String> actions;
        private ArrayList<String> descriptions;


        public MyAdapter(Context context, String[] actions,
                         String[] descriptions) {
            this.context = context;
            this.actions = new ArrayList<>(Arrays.asList(actions));
            this.descriptions = new ArrayList<>(Arrays.asList(descriptions));
        }

        @Override
        public int getCount() {
            return actions.size();
        }

        @Override
        public Object getItem(int position) {
            return actions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // You should fetch the LayoutInflater once in your constructor
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_activities, null);
                holder = new ViewHolder();
                assert convertView != null;
                holder.textView1 = (TextView) convertView.findViewById(R.id.text1);
                holder.textView2 = (TextView) convertView.findViewById(R.id.text2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView1.setText(actions.get(position));
            holder.textView2.setText(descriptions.get(position));
            return convertView;
        }

        private class ViewHolder {
            TextView textView1;
            TextView textView2;
        }
    }
}

