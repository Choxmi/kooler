package com.gilo.soko.config;

import android.net.Uri;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class Config {
	
	/**
     * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
     * 
     * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
	
	public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    public static final String CONFIG_CLIENT_ID = "ARlyVRAyiKq4n8X8OOHYXYcMwiRtu8Gsq27dHpOLtWN_HLVEDMPj8ZhSwWyv";

    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    public static final int REQUEST_CODE_PROFILE_SHARING = 3;
    public static final int REQUEST_CODE_SET_PLACE = 4;

    public static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Soko")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.gilo.me/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.gilo.me/legal"));
    
    /**
     * Defines the text rolling at the start screen. Use this to convey a message to 
     * the customer about the services you provide
     * 
     * TO-DO
     * This will move to the admin backend
     */
    
    public static String[] marquee_texts = {
			"Free Shipping and Returns", 
			"24/7 Customer Service at 1-123-234-5589", 
			"365 Day Return Policy",
			"The Biggest Collection of Items"
	};
    
    /**
     * Set the STORE_URL to the location of your backend
     * The other URLs will be generated from that
     * If however you change the api end points you have to change the these too.
     */
    
    
    public static String STORE_URL = "http://kimutai.org/market/";
    
    public static String API_URL = "http://kimutai.org/wooApi/api/";
    public static String IMAGE_FULL ="http://kimutai.org/wooApi/img/";
    
    
    
    public static String CATEGORIES_URL = API_URL + "categories.php";
    public static String PRODUCTS_URL = API_URL + "products.php";
    
    public static String BANNERS_URL = API_URL + "banners.php";
    public static String COUNTRIES_URL = API_URL + "countries.php";
    public static String ZONES_URL = API_URL + "zones.php";
    public static String OPTIONS_URL = API_URL + "/options/";
    public static String ORDER_URL = API_URL + "/order/";
    public static String COMMENTS_URL = API_URL + "/comments/";
    public static String ADD_REVIEW_URL = API_URL + "/add_comment/";
    public static String UPLOADS_URL = STORE_URL + "/uploads/";
    
    
//    public static String IMAGE_MEDIUM = UPLOADS_URL + "images/medium/";
//    public static String IMAGE_SMALL = UPLOADS_URL + "images/small/";
//    public static String IMAGE_THUMBNAIL = UPLOADS_URL + "images/thumbnails/";
   
    
    //will display higher quality images but trading speed and data consumption
    public static String IMAGE_MEDIUM = "";
    public static String IMAGE_SMALL = "";
    public static String IMAGE_THUMBNAIL = "";

    
    
    /*
     * This is the cost of shipping
     * 
     */
    
    public static float FLATRATE_SHIPPING = 15;
    
    /**
     * Set the STORE_URL to the location of your backend
    
     */
    
    public static Boolean PAYMENT_PAYPAL = true;
    public static Boolean PAYMENT_COD = true;
    public static Boolean PAYMENT_PICK_FROM_SHOP = true;
    
    public static String ADMIN_USERNAME = "admin";
    public static String ADMIN_PASSWORD = "password";
    
    
    
}
