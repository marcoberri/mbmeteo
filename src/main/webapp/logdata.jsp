<%@page import="it.marcoberri.mbmeteo.helper.ConfigurationHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<%
String js="logdata.js";

    if (request.getParameterMap().get("type") != null) {
        js="logdata.js";
    }
%>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="utf-8">
         <link href="css/ext-all.css" rel="stylesheet">
    </head>

    <body>
     
        <div id="wait">Loading....... <img src="img/default/grid/loading.gif"/></div>
        <div id="grid"></div>

        <script src="js/jquery-1.8.3.min.js"></script>
        <script src="js/ext-all.js"></script>     
        <script src="js/<%=js%>"></script>
        <script type="text/javascript">
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', '<%=ConfigurationHelper.prop.getProperty("analytics.code")%>']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();
        </script>

    </body>
</html>


