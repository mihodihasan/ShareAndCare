<?php
$server_name='fdb14.freehostingeu.com';
$mysql_user='2108487_db';
$mysql_pass='Gc3#dk_bIk';
$msg='Could not connect';
$db_name='2108487_db';

$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);

if($con){
    //echo 'Connected!';
}else
    die($msg).mysqli_connect_error();
?>