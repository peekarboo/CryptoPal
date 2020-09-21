
<?php
    
    include_once ('databaseconnection.php');
    
    class User{
        
        private $database;
        
        private $database_table = "users";
        //private $id ;
        
        public function __construct(){
            $this->database = new DatabaseConnect();
        }
        
        public function LoginExist($username, $password){
            $conn = $this->database->getDb();
            
            $query = "SELECT * from users where username = '$username' AND password = '$password' Limit 1";
            $result = $conn->query($query);
            
            //$result = mysqli_query($this->database->getDb(), $query);
        
            
            if (mysqli_num_rows($result)>0){
                
                while($row = $result->fetch_assoc()){
                    global $id;
                    $id = $row['ID'];
                   
                
                mysqli_close($this->database->getDb());
                
                
                return true;
                
            }
            
            }
            
            mysqli_close($this->database->getDb());
            
            return false;
            
        }
        
        public function isdetails_Exist($username, $email){
             $conn = $this->database->getDb();
          
            $query = "SELECT * FROM  users where username = '$username' AND email = '$email'";
            
            $result = $conn->query($query);
            
           if (mysqli_num_rows($result)> 0){
                
                mysqli_close($this->database->getDb());
                
                return true;
                
            }
             return false;
            
        }
        
        public function EmailValid($email){
            return filter_var($email, FILTER_VALIDATE_EMAIL) !== false;
        }
        
        public function createNewUser($username, $password, $email){
              
            $Existing = $this->isdetails_Exist($username, $email);
            
            if($Existing){
                
                $json['success'] = 0;
                $json['message'] = "Error in registering. Username/email already exists";
            }
            
            else{
            $Valid_Email = $this->EmailValid($email);
                
                if($Valid_Email)
                {
                $query = "insert into ".$this->database_table." (username, password, email) values ('$username', '$password', '$email')";
                
                $inserted = mysqli_query($this->database->getDb(), $query);
                
                if($inserted == 1){
                    
                    $json['success'] = "1";
                    $json['message'] = "Successfully registered the user";
                    
                }else{
                    
                    $json['success'] = 0;
                    $json['message'] = "Error in registering. Probably the username/email already exists";
                    
                }
                
                mysqli_close($this->database->getDb());
                }
                else{
                    $json['success'] = 0;
                    $json['message'] = "Error in registering. Email Address is not valid";
                }
                
            }
            
            return $json;
            
        }
        
        public function UsersLogin($username, $password){
            $conn = $this->database->getDb();
            $json = array();
            $can_UserLogin = $this->LoginExist($username, $password);
            
            if($can_UserLogin){
                global $id;
                $json['success'] = 1;
                $json['message'] = "Successfully logged in";
                $json['id'] = $id;
        
            }
            else{
                $json['success'] = 0;
                $json['message'] = "Incorrect details";
            }
            return $json;
        }
    }
    ?>
