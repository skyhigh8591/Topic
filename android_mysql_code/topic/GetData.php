<?php
    // �]�w MySQL ���s�u��T�ö}�ҳs�u
    // ��Ʈw��m�B�ϥΪ̦W�١B�ϥΪ̱K�X�B��Ʈw�W��
    $link = mysqli_connect("localhost", "root", "", "topic");
    $link -> set_charset("UTF8"); // �]�w�y�t�קK�ýX

    // SQL ���O
    $result = $link -> query("SELECT * FROM `test_db`");
    while ($row = $result->fetch_assoc()) // ��ӫ��O���榳�^��
    {
        $output[] = $row; // �N�v���N�^�Ǫ��F����}�C��
    }

    // �N��ư}�C�ন Json ����ܦb�����W�A�ín�D���⤤��s�� UNICODE
    print(json_encode($output, JSON_UNESCAPED_UNICODE));
    $link -> close(); // ������Ʈw�s�u

?>