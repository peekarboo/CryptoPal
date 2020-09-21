
<?php
	include_once('databaseconnection.php');
	class Portfolio{

        private $database;
        private $conn;
        private $database_table = "portfoliotracker";
       
        
        public function __construct(){
            $this->database = new DatabaseConnect();
        }
        public function doesCoinExist($coinname){
        
        	 $conn = $this->database->getDb();
            $query = "SELECT * FROM portfoliotracker WHERE coinname ='$coinname'Limit 1";
            $result = $conn->query($query);
            if (mysqli_num_rows($result)>0){
            	mysqli_close($this->database->getDb());
                return true;
            }
            return false;
            $conn->close();
        }
        public function insertcoin($coinname, $amount,$quant, $uid){
            $conn = $this->database->getDb();
            $Valid_Email = $this->doesCoinExist($coinname);
            
                
                if(!$Valid_Email)
                {
                    if(is_null($quant)){
                        echo("help");
                    }
                
                $query = "INSERT INTO portfoliotracker (coinname, amount, Userid, coinquantity) VALUES ('" . $coinname . "','testing','" . $uid . "','" . $quant . "')";
                   
                    if($conn->query($query) === TRUE) {
                        echo("inserted");
                        
                        $json['success'] = "1";
                        $json['message'] = "Successfully registered the user";
                         $conn->close();
                        
                    }
                    else{
                        echo("its not connecting");
                    }
                    
                }
                else
                echo("successhere");
                 $json['success'] = 0;
                
        }
           // $json['message'] = "Added to Portfolio successfully";
        
            /*$conn = $this->database->getDb();
            $coin_exist = $this->doesCoinExist($coinname);
           
            
        	if(!$coin_exist){
        	   $coinquantity= mysql_real_escape_string($quantity);
                    $query = "INSERT INTO portfoliotracker (coinname, amount, quantity, Userid) VALUES ('$coinname', '$amount', '$quant','$uid')";
                $inserted =$conn->query($query);
            
                if($inserted){
                    	$json['success'] = 1;
                        $json['message'] = "Added to Portfolio successfully";
                }
                else{
                        
                    $json['success'] = 0;
                    $json['message'] = "Error";
                    
                }
             $conn->close();
            
        	}
            else{
                $conn = $this->database->getDb();
                $query = "UPDATE portfoliotracker SET quantity='Doe' WHERE Userid=1";
                if ($conn->query($query)) {
                         echo "Record updated successfully";}
                else{
                    echo("i don tire");
                }
        		//"UPDATE portfoliotracker SET quantity = '$quantity' WHERE coinname ='$coinname'";
        		$updated = true;
        		echo($updated);
        		if($updated){
                	$json['success'] = 1;
                    $json['message'] = "Added to Portfolio successfully";
                    
                }else{
                    
                    $json['success'] = 0;
                    $json['message'] = "Error";
                    
                }
                $conn->close();
        	}*/
}
?>