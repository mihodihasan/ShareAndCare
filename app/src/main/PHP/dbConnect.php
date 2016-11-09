<?php
 define('HOST','fdb14.freehostingeu.com');
 define('USER','2108487_db');
 define('PASS','Gc3#dk_bIk');
 define('DB','2108487_db');
 
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
 
 if($con) echo "ok";
?>