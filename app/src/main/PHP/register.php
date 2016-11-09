<?php
require "init.php";
$id_app=@$_POST["id_app"];
$username=@$_POST["username"];
$location=@$_POST["location"];
$contact=@$_POST["contact"];
$password=@$_POST["password"];
$type=@$_POST["type"];
$sql_query="insert into user (ID,name,location,number,password,owner) values('$id_app','$username','$location','$contact','$password','$type');";
if(mysqli_query($con,$sql_query)){
	echo "<h3>Registration Success.......!</h3>";
}else{
	echo "Data Not Inserted".mysqli_error($con);
}

?>