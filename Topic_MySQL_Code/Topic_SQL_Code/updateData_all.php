<?php
if (isset($_GET["RFID"])){
require_once("connMysql.php");
date_default_timezone_set('Asia/Taipei');
$updateRFID= $_GET["RFID"];
$updateName= $_GET["name"];
$updateSpecification= $_GET["specification"];
$updateNum = $_GET["num"];
$updateField= $_GET["field"];
$updateRemarks= $_GET["remarks"];
$updateTime = date("Y/m/d H:i:s");
$sql_query = "UPDATE `test_db` SET `name` = $updateName, `specification` = $updateSpecification,
 `num` = $updateNum, `field` = $updateField, `remarks` = $updateRemarks WHERE `RFID` = $updateRFID";
mysqli_query($conn,$sql_query);
mysqli_close($conn);//關閉資料庫連接
echo "OK!";
}
?>
