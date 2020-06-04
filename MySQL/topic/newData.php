<?php
if (isset($_GET["RFID"])){
require_once("connMysql.php");
date_default_timezone_set('Asia/Taipei');
$sql_query = "INSERT INTO `test_db` (`RFID`,`name`,`specification`,`num`,`field`,`remarks`,`datetime`) VALUES (";
$sql_query .= "'" . $_GET["RFID"] . "',";
$sql_query .= "'" . $_GET["name"] . "',";
$sql_query .= "'" . $_GET["specification"] . "',";
$sql_query .= "'" . $_GET["num"] . "',";
$sql_query .= "'" . $_GET["field"] . "',";
$sql_query .= "'" . $_GET["remarks"] . "',";
$sql_query .= "'". date("Y/m/d H:i:s") ."')";
mysqli_query($conn,$sql_query);
mysqli_close($conn);//關閉資料庫連接
echo "added successfully!";
}
?>
