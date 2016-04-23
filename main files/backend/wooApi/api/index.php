<?php

error_reporting( E_ALL );
ini_set( 'display_errors', 'On' );
require_once "../client.php";

$consumer_key = 'ck_7786a179d4f1ce6e9825c461debe38a5'; // Add your own Consumer Key here
$consumer_secret = 'cs_8e56a25ae70c2748795ce1a9db3273d3'; // Add your own Consumer Secret here
$store_url = 'http://kimutai.org/market/'; // Add the home URL to the store you want to connect to here

// Initialize the class
$wc_api = new WC_API_Client( $consumer_key, $consumer_secret, $store_url );

// Get Index
echo json_encode($wc_api->get_index() );

