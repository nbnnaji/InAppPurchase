//package com.androidtalk.shutupandmakeup
//
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.android.billingclient.api.*
//import com.android.billingclient.api.BillingClient.*
//import kotlinx.android.synthetic.main.activity_main.*
//
//
//class MainActivity_Deprecated : AppCompatActivity(), PurchasesUpdatedListener {
//
//    lateinit var adapter: ProductsAdapter
//    //Billing client
//    private var billingClient: BillingClient? = null
//    //Sku list
//    private val skuList = listOf("strengths_five", "strengths_ten")
//    private var mbillingResult: BillingResult? = null
//    private var mIsServiceConnected: Boolean = false
//    private val TAG = "MainActivity_Deprecated"
//
//    private val billingClientResponseCode: Int
//        get() = BILLING_MANAGER_NOT_INITIALIZED
//    val BILLING_MANAGER_NOT_INITIALIZED = -1
//
//    @SuppressLint("WrongConstant")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val linearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        strengths_products.layoutManager = linearLayout
//        strengths_products.adapter
//
//        setupBillingClient()
//
//    }
//
//    private fun setupBillingClient() {
//
//        billingClient = newBuilder(this)
//                .setListener(this)
//                .enablePendingPurchases()
//                .build()
//
//
//        //start connection
//        billingClient!!.startConnection(object : BillingClientStateListener {
//            override fun onBillingServiceDisconnected() {
//                mIsServiceConnected = false
//            }
//
//            override fun onBillingSetupFinished(billingResult: BillingResult?) {
//                Log.d(TAG, "Setup finished. Response code: $billingResult")
//
//                when (billingResult!!.responseCode) {
//                    BillingResponseCode.OK -> {
//                        mIsServiceConnected = true
//
//                    }
//                }
//
//
//            }
//        })
//    }
//
//
//    fun onLoadProductsClicked(view: View) {
//        if (billingClient!!.isReady) {
//            val params = SkuDetailsParams
//                    .newBuilder()
//                    .setSkusList(skuList)
//                    .setType(SkuType.INAPP)
//                    .build()
//
//            billingClient!!.querySkuDetailsAsync(params) { responseCode, skuDetailsList ->
//                when (responseCode!!.responseCode) {
//                    BillingClient.BillingResponseCode.OK -> {
//                        mIsServiceConnected = true
//                        Log.d(TAG, " Load Products clicked: " + responseCode)
//
//                        initProductAdapter(skuDetailsList)
//                    }
//                    else -> {
//                        mIsServiceConnected = false
//                        Log.d(TAG, " Client is not ready!")
//                    }
//                }
//            }
//        }
//    }
//
//    private fun initProductAdapter(skuDetailsList: List<SkuDetails>) {
//
//        val purchaseAdapter = ProductsAdapter(skuDetailsList, this) {
//            val billingFlowParama = BillingFlowParams
//                    .newBuilder()
//                    .setSkuDetails(it)
//                    .build()
//            billingClient!!.launchBillingFlow(this, billingFlowParama)
//
//        }
//        Toast.makeText(this, skuDetailsList.toString(), Toast.LENGTH_LONG).show()
//        strengths_products.adapter = purchaseAdapter
//
//
//    }
//
//
//    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
//        Toast.makeText(this, "Yay! Billing is successful", Toast.LENGTH_LONG).show()
//        // clearHistory method is important if you want to buy same product multiple times.
//        clearHistory()
//    }
//
//    private fun clearHistory() {
//    }
//}
//
//
//
//
//
//
//
////    fun startServiceConnection(final Runnable executeOnSuccess) {
////
////        billingClient = BillingClient
////                .newBuilder(this)
////                .setListener(this)
////                .build()
////        billingClient!!.startConnection(object: BillingClientStateListener{
////            override fun onBillingSetupFinished(billingResult: BillingResult?) {
////                when(billingResult!!.responseCode){
////                    BillingClient.BillingResponseCode.OK ->{
////                        mIsServiceConnected = true
////                        Log.d(TAG, " Set up finished. Response code: " + billingResult)
////                    }
////                    else->{
////                        mIsServiceConnected= false
////                    }
////                }            }
////
////            override fun onBillingServiceDisconnected() {
////                mIsServiceConnected= false            }
////
////        })
////    }
////
////
////    fun startServiceConnection(executeOnSuccess: Runnable?) {
////        billingClient!!.startConnection(object : BillingClientStateListener {
////            override fun onBillingServiceDisconnected() {
////                mIsServiceConnected = false            }
////
////            override fun onBillingSetupFinished(billingResult: BillingResult?) {
////                Log.d(TAG, "Setup finished. Response code: $billingResult")
////
////                when(billingResult!!.responseCode){
////               BillingClient.BillingResponseCode.OK ->{
////                    mIsServiceConnected = true
////                    executeOnSuccess?.run()
////                }
////            }
////
////        }
////    }) }
////
////
////
////
////
////
////    private fun executeServiceRequest(runnable: Runnable) {
////        if (mIsServiceConnected) {
////            runnable.run()
////        } else {
////            // If billing service was disconnected, we try to reconnect 1 time.
////            // (feel free to introduce your retry policy here).
////            startServiceConnection(runnable)
////        }
////    }
//
