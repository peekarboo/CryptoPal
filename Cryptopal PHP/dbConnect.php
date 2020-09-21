<?

//to debug add to portfolio
$servername = "108.167.137.46";
$username = "etoilero_root";
$password = "new123";
$database = "etoilero_cryptopal";
 
 
//creating a new connection object using mysqli 
$conn = mysqli_connect($servername, $username, $password, $database);
 
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}