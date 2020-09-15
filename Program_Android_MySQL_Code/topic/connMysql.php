<?php
//設定帳號與密碼
$servername = "localhost";
$username = "root";
$password = "";
$database = "topic";

// Create connection
$conn=mysqli_connect($servername,$username,$password,$database);

// Check connection
if (mysqli_connect_errno())
{
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

/* change db to world db */
//mysqli_select_db($conn, "test");


//可拿掉試試看看
mysqli_query($conn, "SET NAMES utf8");////資料庫中文亂碼問題   

?>