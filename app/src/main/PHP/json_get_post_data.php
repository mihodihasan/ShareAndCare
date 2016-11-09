<?php
$server_name='fdb14.freehostingeu.com';
$mysql_user='2108487_db';
$mysql_pass='Gc3#dk_bIk';
$msg='Could not connect';
$db_name='2108487_db';
$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
$sql="select * from comment;";

$result=mysqli_query($con,$sql);

$response=array();

while($row=mysqli_fetch_array($result)){
    array_push($response,array("name"=>$row[0],"des"=>$row[1],"dt"=>$row[2]));
    
}
echo json_encode(array("post_array"=>$response));

mysqli_close($con);
?>