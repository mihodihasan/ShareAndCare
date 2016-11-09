<?php
$server_name='fdb14.freehostingeu.com';
$mysql_user='2108487_db';
$mysql_pass='Gc3#dk_bIk';
$msg='Could not connect';
$db_name='2108487_db';
$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
$sql="select * from user where owner='Orphanage';";

$result=mysqli_query($con,$sql);

$response=array();

while($row=mysqli_fetch_array($result)){
    array_push($response,array("name"=>$row[1],"location"=>$row[2],"contact"=>$row[3],"id"=>$row[5]));
    
}
echo json_encode(array("orp_array"=>$response));

mysqli_close($con);
?>