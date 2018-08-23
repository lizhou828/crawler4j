<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date & Time: 2018-08-22 07:28.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate, post-check=0, pre-check=0"
    />
    <meta http-equiv="Connection" content="Close" />
    <script type="text/javascript">
        function stringToHex(str) {
            var val = "";
            for (var i = 0; i < str.length; i++) {
                if (val == "") val = str.charCodeAt(i).toString(16);
                else val += str.charCodeAt(i).toString(16);
            }
            return val;
        }
        function YunSuoAutoJump() {
            var width = screen.width;
            var height = screen.height;
            var screendate = width + "," + height;
            var curlocation = window.location.href;
            if ( - 1 == curlocation.indexOf("security_verify_")) {
                document.cookie = "srcurl=" + stringToHex(window.location.href) + ";path=/;";
            }
            var value = stringToHex(screendate);
            console.info("width=" + width + ",height=" + height + ",value=" + value); //width=1366,height=768,value=313336362c373638
            self.location = "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=JYXT_ZJGG_4465&sitePath&security_verify_data=" + stringToHex(screendate);
        }
    </script>
    <script>
        setTimeout("YunSuoAutoJump()", 2000);
    </script>
</head>
<!--2018-08-22 07:24:46-->

</html>