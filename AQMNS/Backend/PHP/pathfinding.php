<?php

$host = 'localhost';
$user = 'root';
$pass = '';
$db = 'south_jakarta';
 
  $con = mysqli_connect($host,$user,$pass,$db);
  if (isset($_GET['src'])) {
    $awal= $_GET['src'];
    
}
if (isset($_GET['dst'])){
    $tujuan= $_GET['dst'];
}


$result = mysqli_query($con,"SELECT * FROM edge");
$maks=mysqli_query($con,"SELECT MAX(node_id) FROM node");
$data = mysqli_fetch_row($maks);
$mak=$data[0];

$graph= array();
$route= array();

while($row = mysqli_fetch_array($result))
{
$nodea=$row['node_a'] ;

$nodeb=$row['node_b'] ;

$node_a = filter_var($nodea, FILTER_SANITIZE_NUMBER_INT);
$node_b = filter_var($nodeb, FILTER_SANITIZE_NUMBER_INT);

$indek=$row['index'] ;

$distance=$row['distance'] ;
$graph[$node_a][$node_b]=$distance;
//$graf[]=array("$node_a" => $distance,"$node_b" => $distance); 
//$graf[$node_a][$node_b]=$distance;

}

$INT_MAX = 99999999;


function Dijkstra($graph, $source, $verticesCount,$tj)
{
	global $INT_MAX;
	$distance = array();
	$cost = array();
	$path=array();
	$visited=array();
	$pred=array();
	$jalur=array();
	$berat=array();
	$shortestPathTreeSet = array();
$dest=$tj+1;
	for ($i = 0; $i < $verticesCount; ++$i){
		for($j=0; $j<$verticesCount;++$j){
		if($graph[$i][$j]==0){
                $cost[$i][$j]=$INT_MAX;
                }
            else{
                $cost[$i][$j]=$graph[$i][$j];
                }
		}}
	 
	  for($i=0;$i<$verticesCount;++$i)
    {
        $distance[$i]=$cost[$source][$i];
        $pred[$i]=$source;
        $visited[$i]=0;
    }
    $distance[$source]=0;
    $visited[$source]=1;
    $count=1;
    	
    	  while($count<$verticesCount-1)
    {
        $mindistance=$INT_MAX;
        
        //nextnode gives the node at minimum distance
        for($i=0;$i<$verticesCount;++$i){
            if($distance[$i]<$mindistance&&!$visited[$i])
            {
                $mindistance=$distance[$i];
                $nextnode=$i;
            }}
            
            //check if a better path exists through nextnode            
            $visited[$nextnode]=1;
            for($i=0;$i<$verticesCount;++$i){
                if(!$visited[$i]){
                    if($mindistance+$cost[$nextnode][$i]<$distance[$i])
                    {
                        $distance[$i]=$mindistance+$cost[$nextnode][$i];
                        $pred[$i]=$nextnode;
                       
                    }}}
        ++$count;
    }
 
    	#for($i=0;$i<$verticesCount;++$i){
    	#echo $distance[$i];
    	#}
    	$a=0;
    	for($i=0;$i<$dest;++$i){  
        if($i!=$source)
        {
        	#echo "<br />The length is ".$distance[$i];
        	
        	$berat[$i]=$distance[$i];
        	
            #printf("\nDistance of node%d=%d",i,distance[i]);
           # printf("\nPath=%d",i);
          	# echo "<br />Path ".$i;
          	 #$path[0]=$i;
            
            $j=$i;
            for($j=$i;$j<$dest;++$j){
            
            }
            while($j!=$source)
            {
                $j=$pred[$j];
                #printf("<-%d",j);
                $path[$a]=$j;
               # echo "<- ".$j;
                ++$a;
                
            }
    }
	}
	
	#########parsing
	$besar=$berat[$dest-1];
	$jalur=array_unique($path);
	#for($i=0;$i<$a;++$i){
	#echo $jalur[$i];
	#}
	
	echo json_encode($jalur, JSON_FORCE_OBJECT);
	#echo "</br>".$besar;
	echo "</br> {\"cost\":\"".$besar;
	echo "\"}";
}


if($awal<$tujuan){
$aw= $awal;
$tu=$tujuan;
}
else{
$aw= $tujuan;
$tu= $awal;
}

Dijkstra($graph,$aw,$mak,$tu);

?>