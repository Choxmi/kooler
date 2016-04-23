<?php

error_reporting( E_ALL );
ini_set( 'display_errors', 'On' );
require_once "../client.php";

$consumer_key = 'ck_ced5767c9a217a2ae0b7933eadb162544545c6fe'; // Add your own Consumer Key here
$consumer_secret = 'cs_561ea8d0c96c167dedc2762b9a766c5a1b6fafa1'; // Add your own Consumer Secret here
$store_url = 'http://kooler.in/shop/'; // Add the home URL to the store you want to connect to here

// Initialize the class
$wc_api = new WC_API_Client( $consumer_key, $consumer_secret, $store_url );

// Get all products
echo json_encode( $wc_api->get_categories() );


