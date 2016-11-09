<?php
	$dbhost="fdb14.freehostingeu.com";
	$dbuser="2108487_db";
	$dbpassword="Gc3#dk_bIk";
	$dbname="2108487_db";
	
	$con = mysqli_connect($dbhost,$dbuser,$dbpassword,$dbname);
        
        
	
	// Check connection
	if (mysqli_connect_errno())
	  {
	  echo "Failed to connect to MySQL: " . mysqli_connect_error();
	  };

        $ID= $_REQUEST["ID"];
	$name= $_POST['name'];
        $location= $_REQUEST["location"];
        $number= $_REQUEST["number"];
        $password= $_REQUEST["password"];
	$owner= $_REQUEST["owner"]; 
	
        echo 'I am here '. $name;
	$query = "INSERT INTO user VALUES('$ID','$name','$location','$number', '$password','$owner')";
		$result = mysqli_query($con, $query);
		
		if($result){
			echo 'successfully registered';
		}
		else
		{
            echo 'oops! Please try again!';
		}

?>