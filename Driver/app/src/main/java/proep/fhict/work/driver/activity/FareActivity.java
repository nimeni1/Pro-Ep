/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package proep.fhict.work.driver.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;

import proep.fhict.work.driver.R;
import proep.fhict.work.driver.activity.SampleActivityBase;
import proep.fhict.work.driver.model.Driver;
import proep.fhict.work.driver.model.Name;
import proep.fhict.work.driver.recyclerview.RecyclerViewFragment;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class FareActivity extends AppCompatActivity/*extends SampleActivityBase */{

    public static final String TAG = "FareActivity";

    // Whether the Log Fragment is currently shown
    //private boolean mLogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        Driver driver = (Driver) getIntent().getSerializableExtra("driver");
        //if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("driver", driver);
            //bundle.putSerializable("driver", new Driver(new Name("emil","karami"),"2313","@mail","000","123",true));
            fragment.setArguments(bundle);
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
       // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       // MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
      //  logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
     //   logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }


}
