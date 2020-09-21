<?php
    include ("dbConnect.php");
    $coinname= "";
    $action = "";
    
    $coinname = $_REQUEST['coinname'];
    $action = $_REQUEST['action'];
    if($action=="0"){
    
        $stmt = $conn->query("SELECT * FROM coinprediction WHERE coinname = '$coinname' ");
    	if($stmt->num_rows > 0){
    	    $json['success']= 1;
    		$stmt->close();
    	}
    	else
    		$json['success']= 0;
    	echo json_encode($json);
    }
    elseif($action="1"){
        $stmt = $conn->query("SELECT * FROM coinprediction WHERE coinname = '$coinname' ");
        
        if($stmt->num_rows > 0){
    	    while ($row = $stmt->fetch_row()) {
	            $json['success']= 1;
    	        $json['futureprice']=$row[1];    	           
    	        $json['accuracy']=$row[2];
        	 }
    	$stmt->close();
    	}
    	else
    		$json['success']= 0;
    	
        echo json_encode($json);
    }
		
    ?>