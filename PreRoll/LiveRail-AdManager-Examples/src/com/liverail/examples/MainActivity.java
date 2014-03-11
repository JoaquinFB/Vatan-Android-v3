package com.liverail.examples;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainActivity extends Activity {

	/**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the list of samples and set its adapter
        ListView samplesList = (ListView)findViewById(R.id.samples_list);
        samplesList.setAdapter(new SamplesAdapter());

        // Determine which sample will be displayed when the user picks
        // an entry from the sample list
        samplesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            	Intent intent = null;
                
                Bundle b = new Bundle();
                b.putString("LR_PUBLISHER_ID", "1331"); 
                b.putString("LR_LAYOUT_SKIN_MESSAGE", "Advertisement | {COUNTDOWN} seconds.");
                b.putString("LR_TAGS", "android, demo, test");
                //b.putString("LR_ADMAP", "in::0;in::0");
                
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, Example1.class);
                        break;

                    case 1:
                        intent = new Intent(MainActivity.this, Example2.class);
                        break;
                    
                    case 2:
                        intent = new Intent(MainActivity.this, Example3.class);
                        break;    

                    case 3:
                        intent = new Intent(MainActivity.this, Example4.class);
                        
                        // Required for Midroll / Postroll ads!
                        b.putString("LR_VIDEO_POSITION", "5"); // time in seconds or use "100%" for postroll ads
                        b.putString("LR_VIDEO_DURATION", "36"); 
                        
                        break;

                    default:
                        break;
                }                

                intent.putExtras(b); 
                MainActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * Adapter class used to fill the samples list that will be displayed
     * to the user to pick from.
     */
    class SamplesAdapter extends BaseAdapter {

        public int getCount() {
            return 4;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View view, ViewGroup viewGroup) {
            View resultView;

            if (view == null) {
                // A view couldn't be recycled, so we'll create a new one
                // by inflating the right layout
                resultView = getLayoutInflater().inflate(R.layout.main_list_entry, null);
            } else {
                // Recycled view will be used in this case
                resultView = view;
            }

            // Obtain references to the elements from the layout of the current list entry
            TextView sampleName = (TextView) resultView.findViewById(R.id.sample_name);

            // Depending on the position in the list, each entry will contain different information
            switch (position) {
                case 0:
                    sampleName.setText(R.string.example_interstitial_fullscreen);
                    break;

                case 1:
                    sampleName.setText(R.string.example_interstitial_custom);
                    break;
                    
                case 2:
                    sampleName.setText(R.string.example_preroll);
                    break;

                case 3:
                    sampleName.setText(R.string.example_midroll);
                    break;

                default:
                    break;
            }

            return resultView;
        }
    } 

}
