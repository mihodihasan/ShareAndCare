<?php
require "init.php";
$user_name=@$_POST["login_name"];
$user_pass=@$_POST["login_pass"];
$id;
$sql_query="select name from user where name like '$user_name' and password like '$user_pass';";
$result=mysqli_query($con,$sql_query);
if(mysqli_num_rows($result)>0){
	$row=mysqli_fetch_assoc($result);
	
	echo "Login Succesful ...";
	//$id=$row["id"];
}else{
	echo "No Man... U are Not Allowed....reg first";
}
?>
