<?php

    $link = mysqli_connect("localhost", "root", "", "topic");
    $link -> set_charset("UTF8"); 

	$search = $_GET["search"];

    $result = $link -> query("SELECT * FROM `test_db` WHERE `RFID` LIKE '%$search%' OR `name` LIKE '%$search%' OR `specification` LIKE '%$search%'  OR `num` LIKE '%$search%' OR `field` LIKE '%$search%' OR `remarks` LIKE '%$search%'");

    while ($row = $result->fetch_assoc()) // ��ӫ��O���榳�^��
    {
        $output[] = $row; // �N�v���N�^�Ǫ��F����}�C��
    }

    // �N��ư}�C�ন Json ����ܦb�����W�A�ín�D���⤤��s�� UNICODE
    print(json_encode($output, JSON_UNESCAPED_UNICODE));
    $link -> close(); // ������Ʈw�s�u

?>