package com.di.inappv4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

                    }
                })

                .build();
        connectToGoogleBilling();*/

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null){
                            for (Purchase purchase: list){
                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED &&
                                !purchase.isAcknowledged()){

                                   // verifyPurchase(purchase);



                                }

                            }
                        }

                    }

                  /*  @Override
                    void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                        if (billingResult.getResponseCode() == BillingResponseCode.OK
                                && purchases != null) {
                            for (Purchase purchase : purchases) {
                                handlePurchase(purchase);
                            }
                        } else if (billingResult.getResponseCode() == BillingResponseCode.USER_CANCELED) {
                            // Handle an error caused by a user cancelling the purchase flow.
                        } else {
                            // Handle any other error codes.
                        }
                    }*/


                }).build();
        connectToGoogleBilling();

    }

    public void connectToGoogleBilling() {
        billingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingServiceDisconnected() {

                        connectToGoogleBilling();

                    }

                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            getProductDetails();

                        }

                    }
                }
        );
    }

   /* private void verifyPurchase(Purchase purchase) {
        String requestUrl = "https://us-central1-playbillingtutorial.cloudfunctions.net/verifyPurchases?" +
                "purchaseToken=" +"assddfffghhjkklldgdhj" + "&" +
                "purchaseTime=" + "10:00 PM" + "&" +
                "orderId=" + "dghhjttyjjds";

        Activity activity = this;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject purchaseInfoFromServer = new JSONObject(response);
                            if(purchaseInfoFromServer.getBoolean("isValid")) {

                                *//*AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                                billingClient.acknowledgePurchase(
                                        acknowledgePurchaseParams,
                                        new AcknowledgePurchaseResponseListener() {
                                            @Override
                                            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                                                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                    Toast.makeText(activity, "Consumed!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                );*//*
                                ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                                billingClient.consumeAsync(
                                        consumeParams,
                                        new ConsumeResponseListener() {
                                            @Override
                                            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                                                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                    Toast.makeText(activity, "Consumed!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                );
                            }
                        } catch (Exception err) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);

    }*/

    public void getProductDetails() {
        List<String> productIds = new ArrayList<>();
        productIds.add("skill_upper");
        SkuDetailsParams getProductDeatailsQuery = SkuDetailsParams
                .newBuilder()
                .setSkusList(productIds)
                .setType(BillingClient.SkuType.INAPP)
                .build();

        Activity activity = this;

        billingClient.querySkuDetailsAsync(
                getProductDeatailsQuery, new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK &&
                                list != null) {

                            TextView itemName = findViewById(R.id.tv1);
                            Button itemPrice = findViewById(R.id.priceid);

                            SkuDetails itemInfo = list.get(0);
                            itemName.setText(itemInfo.getTitle());
                            itemPrice.setText(itemInfo.getPrice());
                            itemPrice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    billingClient.launchBillingFlow(
                                            activity,
                                            BillingFlowParams.newBuilder().setSkuDetails(itemInfo).build()
                                    );

                                  /*  Intent send = new Intent(MainActivity.this, PaymentActivity.class);
                                    startActivity(send);*/

                                }
                            });

                        }
                    }
                }
        );
    }
}

