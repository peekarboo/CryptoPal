<?php
    include ("dbConnect.php");
    $uid ="";
    $coinname= "";
    $quantity="";
    $amount ="";
    $img_url="";
    
    $coinname = $_REQUEST['coinname'];
    
    $uid = $_REQUEST['Userid'];
    
    $amount = $_REQUEST['amount'];
    
    $quantity=$_REQUEST['coinquantity'];
    
    $action = $_REQUEST['action'];
    
    $img_url= $_REQUEST['imgurl'];
    
    
    //if 1 insert into database and if 0 get from database
    
    if ($action=="0"){
        
        $stmt = $conn->query("SELECT * FROM portfoliotracker WHERE Userid = '$uid' ");
		if($stmt->num_rows > 0){
		    $json['sucesss']= 1;
		    while ($row = $stmt->fetch_row()) {
		            
		                
		               
		            $results["prediction"][]= $row;
		            //$results["coinname"]["amount"][]= $row[1];
		            //$results[]["userid"] = $row[2];
		            //$results[]["coinquantity"] = $row[3];
		       
		        
		    }
		    $stmt->close();
		     echo json_encode($results);
		}
		else
		    echo json_encode("Portfolio Empty, Please Add to Portfolio");
		
    }
    elseif($action=="1"){
        $quantity = intval($quantity);
        
        //check if the coin exists if it does update else insert
        $stmt = $conn->prepare("SELECT coinname FROM portfoliotracker WHERE coinname = '$coinname' AND Userid = $uid");
		$stmt->execute();
		$stmt->store_result();
		if($stmt->num_rows > 0){
			$stmt->close();
			$stmt = $conn->prepare("UPDATE  portfoliotracker SET coinquantity= coinquantity +'$quantity'  WHERE coinname = '$coinname'");
			if($stmt->execute()==TRUE){
    		    $json['success'] = "1";
                $json['message'] = "Added to portfolio";
    		}
    		else{
    		    $json['success'] = "0";
                $json['message'] = "you literally suck";
    		    
    		}
    		echo json_encode($json);
    	
			
		}
		else{
		    var_dump($img_url);
            $stmt = $conn->prepare("INSERT INTO portfoliotracker (coinname, amount, Userid, coinquantity,imgurl) VALUES (?, ?, ?, ?, ?)");
    		$stmt->bind_param("sssss", $coinname, $amount, $uid, $quantity, $img_url);
    		if($stmt->execute()==TRUE){
    		    $json['success'] = "1";
                $json['message'] = "Added to portfolio";
    		}
    		else{
    		    $json['success'] = "0";
                $json['message'] = "you literally suck";
    		    
    		}
    		echo json_encode($json);
        }
    }
    elseif($action =="2"){
        $stmt = $conn->prepare("DELETE FROM portfoliotracker WHERE coinname = ? AND Userid= ?");
    	$stmt->bind_param("ss", $coinname, $uid);
    	if($stmt->execute()==TRUE){
    	    $json['success'] = "1";
            $json['message'] = "DELETED";    		
    	    
    	}
    	else{
    	    $json['success'] = "0";
            $json['message'] = "ERROR";
    	}
    		echo json_encode($json);
        
    }
    ?>