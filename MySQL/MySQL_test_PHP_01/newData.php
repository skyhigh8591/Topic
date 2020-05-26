<?php
if (isset($_GET["username"]) && (isset($_GET["num"]))){
require_once("connMysql.php");
date_default_timezone_set('Asia/Taipei');
$sql_query = "INSERT INTO `test_db` (`username`,`num` ,`datetime`) VALUES (";
$sql_query .= "'" . $_GET["username"] . "',";
$sql_query .= "'" . $_GET["num"] . "',";
$sql_query .= "'". date("Y/m/d H:i:s") ."')";
mysqli_query($conn,$sql_query);
mysqli_close($conn);//關閉資料庫連接
echo "新增成功!";
}
?>
