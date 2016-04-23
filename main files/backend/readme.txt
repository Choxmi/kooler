Step A
1. Login into your wordpress dashboard
2. Go to Users -> Your profile
3. Check the 'generate API key' checkbox
4. Update profile
5. You should get the Consumer Key and secret after the page reloads

Extra info http://www.skyverge.com/blog/using-woocommerce-rest-api-introduction/

STEP B
1. Upload the folder to your server.
2. Setup your database
3. Open /wooApi/db_config.php and enter your database, username, password 
4. Open /wooApi/products.php and enter your Consumer Key and secret and store url.
5. Open /wooApi/categories.php/ and enter your Consumer Key and secret and store url.