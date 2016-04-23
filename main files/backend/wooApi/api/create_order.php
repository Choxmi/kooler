<?php

/*
 * Following code will create a new order row
 * All order details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

         $customer_id            = $_POST['customer_id'];
        
         $company               = $_POST['company'];
         $firstname             = $_POST['firstname'];
         $lastname              = $_POST['lastname'];
         $phone                 = $_POST['phone'];
         $email                 = $_POST['email'];
        
         $ship_company          = $_POST['ship_company'];
         $ship_firstname            = $_POST['ship_firstname'];
         $ship_lastname         = $_POST['ship_lastname'];
         $ship_email            = $_POST['ship_email'];
         $ship_phone            = $_POST['ship_phone'];
         $ship_address1         = $_POST['ship_address1'];
         $ship_address2         = $_POST['ship_address2'];
         $ship_city             = $_POST['ship_city'];
         $ship_zip              = $_POST['ship_zip'];
         $ship_zone             = $_POST['ship_zone'];
         $ship_zone_id          = $_POST['ship_zone_id'];
         $ship_country          = $_POST['ship_country'];
         $ship_country_id       = $_POST['ship_country_id'];
        
         $bill_company          = $_POST['bill_company'];
         $bill_firstname        = $_POST['bill_firstname'];
         $bill_lastname         = $_POST['bill_lastname'];
         $bill_email            = $_POST['bill_email'];
         $bill_phone            = $_POST['bill_phone'];
         $bill_address1         = $_POST['bill_address1'];
         $bill_address2         = $_POST['bill_address2'];
         $bill_city             = $_POST['bill_city'];
         $bill_zip              = $_POST['bill_zip'];
         $bill_zone             = $_POST['bill_zone'];
         $bill_zone_id          = $_POST['bill_zone_id'];
         $bill_country          = $_POST['bill_country'];
         $bill_country_id       = $_POST['bill_country_id'];
        
         $shipping_method       = $_POST['shipping_method'];
         $shipping_price        = $_POST['shipping_price'];
        
         $tax                   = $_POST['tax'];
         $gift_card_discount    = $_POST['gift_card_discount'];
         $coupon_discount       = $_POST['coupon_discount'];
         $cart_subtotal         = $_POST['cart_subtotal'];
         $cart_total            = $_POST['cart_total'];
        
         $product_ids           = $_POST['product_ids'];
         $quantities            = $_POST['quantities'];
         $options           = $_POST['options'];
        
         
        
         $payment_description   = $_POST['payment_description'];
    
        
         $referral              = $_POST['referral'];
         $shipping_notes        = $_POST['shipping_notes'];
         
         $product_id = $_POST['product_id'];
        $quantity = $_POST['quantity'];

        $order_number = round(microtime(true));
        $status = 'Order Placed';
        $ordered_on = date('Y-m-d H:i:s');
   

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();
    
    $test = "";
     $message = '';

    if( is_array( $product_ids) ) {
        for ($i = 0; $i < sizeof($product_ids) ; $i++) {

            $product_id = $product_ids[$i];
            $quantity= $quantities[$i];
            $option= $options[$i];

            $test =  $test . $product_id . "\n";
           $result = mysql_query("INSERT INTO order_items( order_id, product_id, quantity, attr) VALUES('$order_number', '$product_id', '$quantity', '$option')") or die(mysql_error()); 

            if($result){
                $message = $message . "success";
            }else{
                $message = $message . mysql_error();
            }

        }
    } else {
          $result = mysql_query("INSERT INTO order_items( order_id, product_id, quantity, attr) VALUES('$order_number', '$product_ids', '$quantities', '$options' )") or die(mysql_error()); 

        if($result){
                $message = $message . "success";
            }else{
                $message = $message . mysql_error();
            }
    }

   

   



    // mysql inserting a new row
    $result = mysql_query("INSERT INTO orders(   
        order_number,    
        customer_id, 
        status,  
        ordered_on,  
       
        tax,
        total,   
        subtotal ,   
        gift_card_discount, 
        coupon_discount, 
        shipping,    
        shipping_notes,  
        shipping_method,
        notes,   
        payment_info,    
        referral,    
        
        company, 
        firstname,   
        lastname,    
        phone,   
        email, 

        ship_company,    
        ship_firstname,  
        ship_lastname,   
        ship_email,  
        ship_phone,  
        ship_address1,   
        ship_address2,   
        ship_city,   
        ship_zip,    
        ship_zone,   
        ship_zone_id,    
        ship_country,    
        ship_country_code,   
        ship_country_id, 

        bill_company,    
        bill_firstname,  
        bill_lastname,   
        bill_email,  
        bill_phone,  
        bill_address1,   
        bill_address2,   
        bill_city,   
        bill_zip,    
        bill_zone,   
        bill_zone_id,    
        bill_country,    
        bill_country_code,   
        bill_country_id 

        ) VALUES(
        
        '$order_number', 
        '$customer_id', 
        '$status',
        '$ordered_on',
        
        '$tax',
        '$cart_total',
        '$cart_subtotal',
        '$gift_card_discount',
        '$coupon_discount',
        '$shipping',
        '$shipping_notes',
        '$shipping_method',
        '$notes',
        '$payment_description',
        '$referral',

        '$company', 
        '$firstname',   
        '$lastname',    
        '$phone',   
        '$email', 

        '$ship_company',    
        '$ship_firstname',  
        '$ship_lastname',   
        '$ship_email',  
        '$ship_phone',  
        '$ship_address1',   
        '$ship_address2',   
        '$ship_city',   
        '$ship_zip',    
        '$ship_zone',   
        '$ship_zone_id',    
        '$ship_country',    
        '$ship_country_code',   
        '$ship_country_id', 

        '$bill_company',    
        '$bill_firstname',  
        '$bill_lastname',   
        '$bill_email',  
        '$bill_phone',  
        '$bill_address1',   
        '$bill_address2',   
        '$bill_city',   
        '$bill_zip',    
        '$bill_zone',   
        '$bill_zone_id',    
        '$bill_country',    
        '$bill_country_code',   
        '$bill_country_id' 

        )") or die(mysql_error());

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Order has been successfully made. adsfadsf" . "\n";
        $response["test"] = $message;
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["test"] = $message;
        $response["message"] = "Oops! An error occurred. " . "\n" . mysql_error();
        
        // echoing JSON response
        echo json_encode($response);
    }
?>