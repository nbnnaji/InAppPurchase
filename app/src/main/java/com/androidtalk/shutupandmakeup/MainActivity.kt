package com.androidtalk.shutupandmakeup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PurchasesUpdatedListener {

    private var billingClient: BillingClient? = null
    private val skuList = listOf("clifton_5", "clifton_34")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        strengths_products.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        setupBillingClient()
    }

    private fun setupBillingClient() {
        billingClient = BillingClient
                .newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                //.setListener(this)
                .build()
        println("Trying to start a connection...")


        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult?) {
                when (billingResult!!.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        println("BILLING | startConnection | RESULT OK")
                    }
                    else -> {
                        println("BILLING | startConnection | RESULT: $billingResult")
                    }
                }
            }
            override fun onBillingServiceDisconnected() {
                println("BILLING | onBillingServiceDisconnected | DISCONNECTED")
            }

        })
    }

    fun onLoadProductsClicked(view: View) {
        if (billingClient!!.isReady) {
            val params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build()
            billingClient!!.querySkuDetailsAsync(params) { responseCode, skuList ->
                when (responseCode!!.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        //println("querySkuDetailsAsync, responseCode: $responseCode")
                        this.initProductAdapter(skuList)
                    }
                    else -> {
                       // println("Can't querySkuDetailsAsync, responseCode: $responseCode")
                    }

                }
            }
        }
    }

     fun initProductAdapter(skuDetailsList: List<SkuDetails>) {
        println("---------")
        println(skuDetailsList.toString())
        println("---------")
        val purchaseAdapter = ProductsAdapter(skuDetailsList, this) {
            val billingFlowParams = BillingFlowParams
                    .newBuilder()
                    .setSkuDetails(it)
                    .build()
            billingClient!!.launchBillingFlow(this, billingFlowParams)
        }

        Toast.makeText(this, skuDetailsList.toString(), Toast.LENGTH_LONG).show()

        strengths_products.adapter = purchaseAdapter
    }

    //if billing is successful, responseCode returns 0, else not successful, details: https://developer.android.com/google/play/billing/billing_reference
    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        println("---------------")
        println("---------------")
        println("onPurchasesUpdated: ${purchases.toString()}")
        println("Details: $billingResult")
        println("---------------")
        println("---------------")
        allowMultiplePurchases(purchases)
        // clearHistory method is important if you want to buy same product multiple times.
        clearHistory()
    }

    private fun allowMultiplePurchases(purchases: MutableList<Purchase>?) {
        val purchase = purchases?.first()
        if (purchase != null) {
            billingClient!!.consumeAsync(ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()) { responseCode, purchaseToken ->

                when(responseCode!!.responseCode){
                   BillingClient.BillingResponseCode.OK ->{
                       if (purchaseToken != null){
                           println("AllowMultiplePurchases success, responseCode: $responseCode")
                       }
                   }
                    else ->{ println("Can't allowMultiplePurchases, responseCode: $responseCode")}
                }
                }
            }
        }


    private fun clearHistory() {
        billingClient!!.queryPurchases(BillingClient.SkuType.INAPP).purchasesList
                .forEach {
                    billingClient!!.consumeAsync(ConsumeParams.newBuilder().setPurchaseToken(it.purchaseToken).build()) { responseCode, purchaseToken ->
                        when(responseCode!!.responseCode){
                            BillingClient.BillingResponseCode.OK ->{
                                if (purchaseToken != null){
                                    println("onPurchases Updated consumeAsync, purchases token removed: $purchaseToken")
                                }
                            }
                            else ->{ println("onPurchases some troubles happened: $responseCode")}
                        }
                    }
                }
    }


}
