<?php
  define('HOST','localhost');
  define('USER','root');
  define('PASS','');
  define('DB','south_jakarta');
 
  $con = mysqli_connect(HOST,USER,PASS,DB);
 
  $sql = "select * from node";
 
  $res = mysqli_query($con,$sql);
  $result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,
array('node_id'=>$row[0],
'node_name'=>$row[1],
'latitude'=>$row[2],
'longitude'=>$row[3]
));
}
 
echo json_encode(array("node"=>$result));
 
mysqli_close($con);
  ?>