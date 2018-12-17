package com.nokia.omsharma.stafftrial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazonaws.amplify.generated.graphql.CreateKiraMutation;
import com.amazonaws.amplify.generated.graphql.ListKirasQuery;
import com.amazonaws.amplify.generated.graphql.ListTodosQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import type.CreateKiraInput;

public class MainActivity extends AppCompatActivity {
    private AWSAppSyncClient mAWSAppSyncClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        //mutation();
        //query();

    }
    /** Called when the user taps the Send button */
    public void reportIncident(View view) {
        // Do something in response to button
    }


    public void query(){
        mAWSAppSyncClient.query(ListKirasQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(kirasCallback);
    }

    private GraphQLCall.Callback<ListKirasQuery.Data> kirasCallback = new GraphQLCall.Callback<ListKirasQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListKirasQuery.Data> response) {
            Log.i("myResults", response.data().listKiras().items().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("myERROR", e.toString());
        }
    };

    public void mutation(){
        CreateKiraInput createKiraInput = CreateKiraInput.builder().
                title("Voice not working").
                reportedBy("Smita").
                organization("Nokia").
                email("sharsmita@gmail.com").
                phone("07970529108").
                date("2018-12-17Z").
                time("12:00Z").
                iMSI("21324325436457658658").
                iMEI("23213-324234-324324324").
                cGI("23432567-25325-25").
                description("asdjkadskjahdasknsjandsann" +
                "asdasdasdasdasdasd").
                status("OPEN").
                build();
        mAWSAppSyncClient.mutate(CreateKiraMutation.builder().input(createKiraInput).build())
                .enqueue(mutationCallback);
    }

    private GraphQLCall.Callback<CreateKiraMutation.Data> mutationCallback = new GraphQLCall.Callback<CreateKiraMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateKiraMutation.Data> response) {
            Log.i("mutResults", "Record Added" + response.data().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("mutError", e.toString());
        }
    };


}


