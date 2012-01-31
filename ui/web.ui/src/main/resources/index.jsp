<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>CIBuddy Administration</title>
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">
    </head>
    <body>
        
        <!-- Top Bar -->
        <div class="topbar" data-scrollspy="scrollspy" >
            <div class="topbar-inner">
                <div class="container">
                    <a class="brand" href="#">CIBuddy</a>
                    <ul class="nav">
                        <li class="active"><a href="#overview">Overview</a></li>
                        <li><a href="#about">About</a></li>
                        <li><a href="#servers">Build Servers</a></li>
                        <li></li>
                    </ul>
                </div>
            </div>
        </div>


        <div class="container">

            <h2>Known Server List:</h2>
                <%=com.cibuddy.web.ui.impl.HtmlUtil.prettyPrintServerList();%>
            <h2>Known Build Indicators:</h2>
        </div>
    </body>
</html>