<?php
    
    include_once 'config.php';
    
    class DatabaseConnect{
        
        private $connection;
        
        public function __construct(){
            
            $this->connection = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
            
            if (mysqli_connect_errno($this->connection)){
                echo "Unable to connect to  Database: " . mysqli_connect_error();
            }
        }
        
        public function getDb(){
            return $this->connection;
        }
    }
    ?>