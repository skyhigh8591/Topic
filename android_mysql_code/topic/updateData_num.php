<?php
if (isset($_GET["RFID"]) && (isset($_GET["num"]))){
require_once("connMysql.php");
date_default_timezone_set('Asia/Taipei');
$updateRFID= $_GET["RFID"];
$updateNum = $_GET["num"];
$updateTime = date("Y/m/d H:i:s") ;
$sql_query = "UPDATE `test_db` SET `num` = $updateNum  WHERE `RFID` = $updateRFID";
//$sql_query = "UPDATE `test_db` SET `num` = '14' WHERE `RFID` = '1'";
mysqli_query($conn,$sql_query);
mysqli_close($conn);//關閉資料庫連接
echo "OK!";
}
?>
