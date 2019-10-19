/*
 * Copyright (C) 2014 The Android Open Source Project
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
 * limitations under the License.
 */

package proep.fhict.work.driver.recyclerview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import proep.fhict.work.driver.APIService;
import proep.fhict.work.driver.APIUtils;
import proep.fhict.work.driver.R;
import proep.fhict.work.driver.activity.RouteActivity;
import proep.fhict.work.driver.middleware.GatewayClient;
import proep.fhict.work.driver.model.Address;
import proep.fhict.work.driver.model.Driver;
import proep.fhict.work.driver.model.Fare;

/**
 * Provide views to RecyclerView with data from arraylist.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static APIService service = APIUtils.getAPIService();
    private static Driver driver;


    // private String[] mDataSet;
    private ArrayList<Fare> arrayList;
    private Context ctx;


    public CustomAdapter( ArrayList<Fare> dataSet, Context ctx, Driver driver ) {
        // mDataSet = dataSet;
        this.arrayList = dataSet;
        this.ctx = ctx;
        notifyDataSetChanged();
        CustomAdapter.driver = driver;


    }

    public void notifyData(ArrayList<Fare> myList) {
        this.arrayList = myList;
        notifyDataSetChanged();
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType ) {
        // Create a new view.
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);


        return new ViewHolder(v, ctx, arrayList);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder( @NonNull ViewHolder viewHolder, final int position ) {
        viewHolder.LEmail.setText(arrayList.get(position).getClient_email());
        Address addressStart = arrayList.get(position).getStart_address();
        String street = addressStart.getStreet();
        String number = addressStart.getNumber();
        String streetAndNumberStart = street + " " + number;
        viewHolder.AddressStart.setText("From: " + streetAndNumberStart);

        Address addressEnd = arrayList.get(position).getDestination_address();
        String streetEnd = addressEnd.getStreet();
        String numberEnd = addressEnd.getNumber();
        String streetAndNumberEnd = streetEnd + " " + numberEnd;
        viewHolder.AddressEnd.setText("To: " + streetAndNumberEnd);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /*    *
     * Provide a reference to the type of views that you are using (custom ViewHolder)*/
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //GatewayClient gatewayClient = new GatewayClient();
        TextView LName;
        TextView LEmail;
        TextView AddressStart;
        TextView AddressEnd;
        private final TextView textView = null;
        Button btn ;


        ArrayList<Fare> arrayList;
        Context ctx;

        public ViewHolder( View itemView, Context ctx, ArrayList<Fare> arrayList ) {
            super(itemView);
            this.arrayList = arrayList;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            LEmail = itemView.findViewById(R.id.email);
            AddressStart = itemView.findViewById(R.id.addressStart);
            AddressEnd = itemView.findViewById(R.id.addressEnd);
            btn = itemView.findViewById(R.id.message_button);
            btn.setOnClickListener(this);
        }

        @Override
        public void onClick( View v ) {
            final int position = getAdapterPosition();
            final Fare fare = this.arrayList.get(position);
            Intent intent = new Intent(this.ctx, RouteActivity.class);
            intent.putExtra("email", fare.getClient_email());
            intent.putExtra("destinationaddress:", fare.getDestination_address());
            intent.putExtra("startaddress:", fare.getStart_address());
            intent.putExtra("Fare", fare);
            intent.putExtra("driver",driver);


            Address startAddress= (fare.getStart_address());
            String city = startAddress.getCity();
            String street = startAddress.getStreet();
            String number = startAddress.getNumber();
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + city +" "+ street + " " +number);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            ctx.startActivity(mapIntent);
            //fare.setDriver_email(driver.getEmail());
            //fare.setTotal_price(/*price per km from car*distance between two points to be calculated*/);
           // Gson gson = new Gson();
            //gatewayClient.sendMessage(gson.toJson(fare));
            arrayList.remove(fare);
            this.ctx.startActivity(intent);
        }
    }

    // END_INCLUDE(recyclerViewSampleViewHolder)

    /*    *
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.*/

}