
<?php
    include ("user.php");
    $username ="";
    $password = "";
    $email = "";
    
    $username = $_REQUEST['username'];
            
    $password = $_REQUEST['password'];
    
    $email = $_REQUEST['email'];
    
    $userObject = new user();
        
    // Registration
        
    if($email=="one"){
        $hashed_password = md5($password);
        $json_array = $userObject->UsersLogin($username, $hashed_password);
            
        echo json_encode($json_array);
            
        }
        
        // Login
        
    else{
        $hashed_password = md5($password);
        
        $json_registration = $userObject->createNewUser($username, $hashed_password, $email);
            
        echo json_encode($json_registration);
            
        
        }
        
    ?>
