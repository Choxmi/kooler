<?php

/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// get all products from products table
$result = mysql_query("SELECT * FROM countries") or die(mysql_error());


$json = array();
$total_records = mysql_num_rows($result);

if($total_records >= 1){
  while ($row = mysql_fetch_array($result, MYSQL_ASSOC)){
    $json[] = $row;
  }
}

echo json_encode($json);

?>
