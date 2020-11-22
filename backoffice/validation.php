
<?php

$email="admin";
$password="admin";

if($_POST['email'] == $email && $_POST['password'] == $password){
    header( 'Location: listhopitaux.html' );
}else{
    header( 'Location: index.php' );  
}

?>
