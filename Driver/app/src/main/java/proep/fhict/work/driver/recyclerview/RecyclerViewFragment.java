/** Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.*/



package proep.fhict.work.driver.recyclerview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.ArrayList;

import proep.fhict.work.driver.R;
import proep.fhict.work.driver.middleware.GatewayClient;
import proep.fhict.work.driver.model.Address;
import proep.fhict.work.driver.model.DateTime;
import proep.fhict.work.driver.model.Driver;
import proep.fhict.work.driver.model.Fare;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.*/


public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private Driver mDriver;
    private static int count = 0;


    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Fare> mDataset;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDriver = (Driver) bundle.getSerializable("driver");
        }
        initDataset();
        final Channel channel = gatewayClient.getChannel();
        gatewayClient.receiveMessage(new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                // (process the message components here ...)
                String message = new String(body);

                Fare fare = gson.fromJson(message,Fare.class);

                Log.d("TAGMessage"+ count++, message);
                if(!fare.isTrip_completed()){mDataset.add(fare);}
                mAdapter.notifyData(mDataset);
                mRecyclerView.smoothScrollToPosition(mDataset.size()-2);


//                mAdapter.notifyItemRangeChanged(0, mDataset.size());
//                mAdapter.notifyDataSetChanged();
//                mAdapter.notifyItemRangeInserted(curSize, mDataset.size());
//                mAdapter.notifyItemInserted(3);

                //channel.basicAck(deliveryTag, false);
            }
        });
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new CustomAdapter(mDataset, getContext(),mDriver);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)
        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 1;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {

            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    final Gson gson = new Gson();
    GatewayClient gatewayClient = new GatewayClient();
    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */

    private void initDataset() {


        Address address1 = new Address("Heezerweg", "114", "Eindhoven", "5614HH", 37.406577, -122.078006);
        Address address2 = new Address("Hweg", "136", "Eindhoven", "5614HH", 37.429113, -122.122666);
        DateTime date = new DateTime("monday","october","1997");

            Fare fare1= new Fare(date, "1997", "1998", address1, address2, 1, 10, "bob@mail.com", false, "driver@hotmail.com");
            Fare fare2= new Fare(date, "1997", "1998", address1, address2, 1, 10, "bob@mail.com", false, "driver@hotmail.com");
            Fare fare3= new Fare(date, "1997", "1998", address1, address2, 1, 10, "bob@mail.com",false, "driver@hotmail.com");


        mDataset= new ArrayList<>();
       // mDataset.add(fare1);
       // mDataset.add(fare2);
      //  mDataset.add(fare3);
       // mDataset = new String[DATASET_COUNT];
       // for (int i = 0; i < DATASET_COUNT; i++) {
         //   mDataset[i] = "This is element #" + i;
        }
    }

