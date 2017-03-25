<?php
/*
Anak_TK
Projek AQMNS( Air Quality Monitoring and Navigation System)
by : Virbyansah Achmadan N.
file name insert2.php
Description : This source code use for getting  CO sensor data and GPS from arduino to 
database. The input data for parameter are $sensor -> contents of CO, $lat -> latitude coordinate,
& $lng -> longitude coordinate. For serching the closest coordinate between GPS from arduino and coordinate
from database, we use haversine method.   
*/
 $db=mysqli_connect("localhost", "root", "", "koor_sby"); // Connect to database 'koor_sby'
  $sensor=$_GET['sensor']; // Get sensor
  $lat=$_GET['lat']; //Get Latitude
  $lng=$_GET['lng']; // Get longitude

  function haversine($lat, $lng, $lat_ambil,$lng_ambil){ //haversine 
	  $earth_radius= 6371000; // default in meters
	  $dlat= deg2rad($lat); //convert to raddian
	  $dlng=deg2rad($lng); //convert to raddian
	  $dlat_ambil=deg2rad($lat_ambil); //convert to raddian
	  $dlng_ambil=deg2rad($lng_ambil); //convert to raddian
	  
	  $latDelta=$dlat_ambil-$dlat; // find delta
	  $lngDelta=$dlng_ambil-$dlng; //find delta
	  
	  $angle=2*asin(sqrt(pow(sin($latDelta/2),2)+ cos ($dlat) * cos ($dlat_ambil) * pow (sin($lngDelta/2),2 ) ) ); //formula for angle of delta
	  return $angle*$earth_radius;  // return value
  }
  function findMinIdx(array $dist){ //function for the closest distance between every id
	  if(empty($dist)|| count($dist)==0) { // check empty 
		  return -1; // return value
		  }
	  $minVal=$dist[0]; // start from array 0
	  $minIdx=0; start from index 0
	  for($idx=1;$idx<count($dist);$idx++){ //looping for check the closest distance
		  if($dist[$idx]<$minVal){ //check the closest distance
			  $minVal=$dist[$idx]; //switch minval
			  $minIdx=$idx; //switch idx
		  }
	  }
	  return $minIdx; //return value
	  
  }
   
   function dekat($lat,$lng,$db){ // function for get data latitude and longitude from database
	   $dist=array(); // array variable
	   $ambil="SELECT * FROM `node`"; //select all data from table node
	   $count=0; //start from zerro for array count 
	   $hasil=mysqli_query($db,$ambil); //execute the query 
	   if (mysqli_num_rows($hasil) > 0) { //check query 
		// output data of each row
			while($row = mysqli_fetch_assoc($hasil)) { // change into array data from database
			$lat_ambil=$row["latitude"]; //latitude variable
			$lng_ambil=$row["longitude"]; //longitude variable
			$dist[$count] = haversine($lat,$lng,$lat_ambil,$lng_ambil); //collect haversine return value into $dist array 
			$count++; //count increment
			}
			return $mins=findMinIdx($dist); // return value
		}
	}
   
  $min=dekat($lat,$lng,$db); // execute function dekat
  $query="UPDATE `node` SET `sensor`='$sensor' WHERE `node_id`=$min"; // query for set sensor CO value  with $min 
  mysqli_query($db,$query); //execute the query
  $query_middle="SELECT `sensor` FROM  `node` WHERE `node_id`=$min"; // querry for select sensor value with $min
  $hasil1=mysqli_query($db,$query_middle); //check query 
  if(mysqli_num_rows($hasil1)){ // check query 
	  while ($row=mysqli_fetch_assoc ($hasil1) ){ //change into array data from database
		  $index=$row["sensor"]; // insert CO value from database into $index
	  }
  }
 $query_edge= "UPDATE `edge`, `node` SET `index`=$index WHERE `node_id`= $min AND (`node_name`=`node_a` OR `node_name`=`node_b`)"; // update index data from edge table with the latest sensor data from node table
 mysqli_query($db,$query_edge); // execute the query
?>