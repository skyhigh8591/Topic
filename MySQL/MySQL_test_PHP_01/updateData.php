<?php
if (isset($_GET["username"]) && (isset($_GET["num"]))){
require_once("connMysql.php");
date_default_timezone_set('Asia/Taipei');
$updateUsername = $_GET["username"];
$updateNum = $_GET["num"];
$updateTime = date("Y/m/d H:i:s") ;
$sql_query = "UPDATE `test_db` SET `num` = $updateNum  WHERE `username` = $updateUsername" ;
//$sql_query = "UPDATE `test_db` SET `datetime` = $updateTime  WHERE `username` = $updateUsername" ;
mysqli_query($conn,$sql_query);
mysqli_close($conn);//關閉資料庫連接
echo "OK!";
echo "\n";
echo $updateTime;
}
?>
