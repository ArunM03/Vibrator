package com.health.android.vibrator.ui.fragments.others

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.health.android.vibrator.MainActivity
import com.health.android.vibrator.R
import com.health.android.vibrator.databinding.FragmentBecomepremiumBinding
import com.health.android.vibrator.others.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BecomePremiumFragment : Fragment(R.layout.fragment_becomepremium) {

    private lateinit var billingClient: BillingClient
    lateinit var binding : FragmentBecomepremiumBinding
    lateinit var productDetailsForLaunchFlow: ProductDetails
    lateinit var sharedPref : SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBecomepremiumBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        setUpBilling()

        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun setUpBilling() {

        val purchaseUpdateListener =  PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null){
                for (purchase in purchases){
                    CoroutineScope(Dispatchers.IO).launch {
                        handlePurchase(purchase)
                    }
                }
            }else if(billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
                //Toast.makeText(requireContext(), "Purchase cancelled", Toast.LENGTH_SHORT).show()
            }else{

            }
        }

        billingClient = BillingClient.newBuilder(requireContext())
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()

        sharedPref = SharedPref(requireContext())


        startConnection()

        binding.fabUnlockallpatterns.setOnClickListener {
            if (this::productDetailsForLaunchFlow.isInitialized){
                launchPurchaseFlow(productDetailsForLaunchFlow)
            }else{
                Toast.makeText(requireContext(), "Please wait...its Loading", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val params = QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.INAPP)
                        val purchasesResult = billingClient.queryPurchasesAsync(params.build())
                        if(purchasesResult.purchasesList.isEmpty()){
                            processPurchases()
                        }
                    }
                } else {
                    //      Toast.makeText(requireContext(),billingResult.debugMessage + "error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onBillingServiceDisconnected() {
                //     Toast.makeText(requireContext(),"Billing service disconnected", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    suspend fun processPurchases() {

        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList( listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("vibrator_123")
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
          )
        )

        // leverage queryProductDetails Kotlin extension function
        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }


        try{

            val productDetails = productDetailsResult.productDetailsList!![0]


            withContext(Dispatchers.Main){
                binding.progressbar.visibility = View.GONE
                val formattedPrice = productDetails.oneTimePurchaseOfferDetails!!.formattedPrice
                binding.tvPrice.text = formattedPrice
                val acutalPrice = formattedPrice.substring(1).replace(",","").toFloat() * 3.33f
                binding.tvActualprice.text = "Actual Price : ${formattedPrice[0]}$acutalPrice"
                binding.tvActualprice.strike = true
            }

            productDetailsForLaunchFlow = productDetails

        }catch (e : Exception){

            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), "Something went wrong , Please try again", Toast.LENGTH_SHORT).show()
            }

            FirebaseCrashlytics.getInstance().recordException(e)

        }

    }

    fun launchPurchaseFlow(productDetails: ProductDetails) {
        // An activity reference from which the billing flow will be launched.

        val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                    .setProductDetails(productDetails)
                    // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                    // for a list of offers that are available to the user
                    .build()
        )
        /*        val productDetailsParamsList = listOf(
            productDetails.subscriptionOfferDetails?.get(0)?.let {
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                    .setProductDetails(productDetails)
                    // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                    // for a list of offers that are available to the user
                    .setOfferToken(it.offerToken)
                    .build()
            }
        )*/

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

            billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
// Launch the billing flow

    }

    inline var TextView.strike: Boolean
        set(visible) {
            paintFlags = if (visible) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG == Paint.STRIKE_THRU_TEXT_FLAG



    suspend fun handlePurchase(purchase: Purchase) {

        if (purchase.purchaseState === Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult = withContext(Dispatchers.IO) {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()){ billingResult ->
                        sharedPref.setPremiumStatus()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                }
            }
        }
    }

}